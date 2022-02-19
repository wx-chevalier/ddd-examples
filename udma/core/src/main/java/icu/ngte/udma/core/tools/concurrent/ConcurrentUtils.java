package icu.ngte.udma.core.tools.concurrent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.SettableFuture;
import io.vavr.concurrent.Future;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class ConcurrentUtils {

  public static ListenableFuture<?> sequenceRun(
      List<Supplier<ListenableFuture<?>>> runners,
      Supplier<ListenableFuture<?>> pause,
      Executor executor) {
    ListenableFuture<?> future = pause.get();
    for (Supplier<ListenableFuture<?>> runner : runners) {
      future = Futures.transformAsync(future, r -> runner.get(), executor);
      future = Futures.transformAsync(future, r -> pause.get(), executor);
    }
    return future;
  }

  // returns: cancel created timeout action & reschedule a new timeout
  public static BiConsumer<Integer, Runnable> createTimeoutScheduler(
      SettableFuture<?> future, ListeningScheduledExecutorService scheduledExecutor) {

    AtomicReference<ListenableScheduledFuture<?>> timeoutFuture = new AtomicReference<>();

    return (newTimeoutSeconds, onTimeout) -> {
      ListenableScheduledFuture<?> prevSchedule = timeoutFuture.get();

      if (prevSchedule != null) {
        prevSchedule.cancel(true);
      }

      if (!future.isDone() && newTimeoutSeconds > 0) {
        ListenableScheduledFuture<?> newSchedule =
            scheduledExecutor.schedule(onTimeout, newTimeoutSeconds, TimeUnit.SECONDS);

        timeoutFuture.set(newSchedule);

        future.addListener(() -> newSchedule.cancel(true), scheduledExecutor);
      }
    };
  }

  public static BiConsumer<Integer, String> createTimeoutReschedulerAcceptsTimeoutMessage(
      SettableFuture<?> future, ListeningScheduledExecutorService scheduledExecutor) {
    BiConsumer<Integer, Runnable> timeoutRescheduler =
        ConcurrentUtils.createTimeoutScheduler(future, scheduledExecutor);

    return (timeoutSeconds, timeoutMessage) -> {
      timeoutRescheduler.accept(
          timeoutSeconds, () -> future.setException(new TimeoutException(timeoutMessage)));
    };
  }

  public static <T> void listenOnFuture(
      ListenableFuture<T> future,
      @Nullable Consumer<T> onSuccess,
      @Nullable Consumer<Throwable> onError,
      Executor executor) {
    Futures.addCallback(
        future,
        new FutureCallback<T>() {
          @Override
          public void onSuccess(@Nullable T result) {
            Optional.ofNullable(onSuccess).ifPresent(v -> v.accept(result));
          }

          @Override
          public void onFailure(@NotNull Throwable t) {
            Optional.ofNullable(onError).ifPresent(v -> v.accept(t));
          }
        },
        executor);
  }

  public static void listenOnAllFutures(
      List<ListenableFuture<?>> futures,
      @Nullable Runnable onSuccess,
      @Nullable Consumer<Throwable> onError,
      Executor executor) {
    Futures.addCallback(
        Futures.allAsList(futures),
        new FutureCallback<Object>() {
          @Override
          public void onSuccess(@Nullable Object result) {
            Optional.ofNullable(onSuccess).ifPresent(Runnable::run);
          }

          @Override
          public void onFailure(@NotNull Throwable t) {
            Optional.ofNullable(onError).ifPresent(v -> v.accept(t));
          }
        },
        executor);
  }

  /** @param command returns isFinished */
  public static ListenableScheduledFuture<?> scheduleAtFixedRateUntilFinished(
      Supplier<Boolean> command,
      long initialDelay,
      long period,
      TimeUnit unit,
      ListeningScheduledExecutorService executor) {
    AtomicReference<ListenableScheduledFuture<?>> future = new AtomicReference<>();
    future.set(
        executor.scheduleAtFixedRate(
            () -> {
              if (command.get()) {
                Optional.ofNullable(future.get())
                    .filter(v -> !v.isDone())
                    .ifPresent(v -> v.cancel(true));
              }
            },
            initialDelay,
            period,
            unit));
    return future.get();
  }

  public static void abortConsequentOnCause(
      ListenableFuture<?> cause, ListenableFuture<?> consequent, Executor executor) {
    Runnable noAction = () -> {};
    abortConsequentOnCause(cause, consequent, noAction, noAction, executor);
  }

  public static void abortConsequentOnCause(
      ListenableFuture<?> cause, Future<?> consequent, Executor executor) {
    Runnable noAction = () -> {};
    abortConsequentOnCause(cause, consequent, noAction, noAction, executor);
  }

  public static void abortConsequentOnCause(
      ListenableFuture<?> cause,
      ListenableFuture<?> consequent,
      Runnable onSuccessCancel,
      Runnable onFailureCancel,
      Executor executor) {
    Futures.addCallback(
        cause,
        new FutureCallback<Object>() {
          @Override
          public void onSuccess(@Nullable Object result) {
            if (!consequent.isDone()) {
              consequent.cancel(true);
              onSuccessCancel.run();
            }
          }

          @Override
          public void onFailure(@NotNull Throwable t) {
            if (!consequent.isDone()) {
              consequent.cancel(true);
              onFailureCancel.run();
            }
          }
        },
        executor);
  }

  public static void abortConsequentOnCause(
      ListenableFuture<?> cause,
      Future<?> consequent,
      Runnable onSuccessCancel,
      Runnable onFailureCancel,
      Executor executor) {
    Futures.addCallback(
        cause,
        new FutureCallback<Object>() {
          @Override
          public void onSuccess(@Nullable Object result) {
            if (!consequent.isCompleted()) {
              consequent.cancel(true);
              onSuccessCancel.run();
            }
          }

          @Override
          public void onFailure(@NotNull Throwable t) {
            if (!consequent.isCompleted()) {
              consequent.cancel(true);
              onFailureCancel.run();
            }
          }
        },
        executor);
  }

  public static void fastCancelOnResultDone(
      ListenableFuture<?> res, ListenableFuture<?> execution, Executor executor) {
    res.addListener(
        () -> {
          if (!execution.isDone()) {
            execution.cancel(true);
          }
        },
        executor);
  }

  public static void connectAnyResultAndExecutionFuture(
      SettableFuture<?> res, ListenableFuture<?> execution, Executor executor) {
    Futures.addCallback(
        execution,
        new FutureCallback<Object>() {
          @Override
          public void onSuccess(@Nullable Object result) {
            if (!res.isDone()) {
              res.set(null);
            }
          }

          @Override
          public void onFailure(@NotNull Throwable t) {
            if (!res.isDone()) {
              res.setException(t);
            }
          }
        },
        executor);

    res.addListener(
        () -> {
          if (!execution.isDone()) {
            execution.cancel(true);
          }
        },
        executor);
  }

  public static <T> void connectResultAndExecutionFuture(
      SettableFuture<T> res, ListenableFuture<T> execution, Executor executor) {

    Futures.addCallback(
        execution,
        new FutureCallback<T>() {
          @Override
          public void onSuccess(@Nullable T result) {
            if (!res.isDone()) {
              res.set(result);
            }
          }

          @Override
          public void onFailure(@NotNull Throwable t) {
            if (!res.isDone()) {
              res.setException(t);
            }
          }
        },
        executor);

    res.addListener(
        () -> {
          if (!execution.isDone()) {
            execution.cancel(true);
          }
        },
        executor);
  }
}
