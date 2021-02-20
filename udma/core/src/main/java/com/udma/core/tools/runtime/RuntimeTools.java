package com.udma.core.tools.runtime;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class RuntimeTools {

  private static final ConcurrentHashMap<Process, ListenableFuture<?>> processListened =
      new ConcurrentHashMap<>();

  @Getter
  private static final ListeningExecutorService executorService =
      MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

  public static Process executeCmd(String... cmd) {

    log.debug("Executing:\n    {}", toCmdString(cmd));

    try {
      return Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      throw new UncheckedIOException("Error executing: " + toCmdString(cmd), e);
    }
  }

  public static boolean checkCommand(Collection<String> cmd, int timeoutSeconds) {
    return checkCommand(cmd, timeoutSeconds, false);
  }

  public static boolean checkCommand(String[] cmd, int timeoutSeconds) {
    return checkCommand(cmd, timeoutSeconds, false);
  }

  public static boolean checkCommand(
      Collection<String> cmd, int timeoutSeconds, boolean printLogOnError) {
    return checkCommand(cmd.toArray(new String[] {}), timeoutSeconds, printLogOnError);
  }

  public static boolean checkCommand(String[] cmd, int timeoutSeconds, boolean printLogOnError) {
    StringBuilder sb = new StringBuilder();

    try {
      Process process = executeCmd(cmd);
      if (printLogOnError) {
        listenOnLines(
            process, l -> sb.append("    ").append(l), l -> sb.append("    ").append(l), null);
      }

      if (!process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
        process.destroyForcibly();
      }
      if (process.exitValue() != 0) {
        if (printLogOnError) {
          log.warn(
              "Check command failed[exitValue={}]: {}\n{}",
              process.exitValue(),
              toCmdString(cmd),
              sb);
        }
        return false;
      } else {
        return true;
      }
    } catch (Throwable t) {
      log.warn(
          "Check command failed: {} - [{}] {}\n{}",
          toCmdString(cmd),
          t.getClass(),
          t.getMessage(),
          sb);
      return false;
    }
  }

  public static ListenableFuture<?> listenOnLines(
      Process process,
      @Nullable Consumer<String> stdOutLineConsumer,
      @Nullable Consumer<String> stdErrLineConsumer,
      @Nullable Runnable onExit) {

    Consumer<InputStream> stdOutStreamConsumer = null;
    Consumer<InputStream> stdErrStreamConsumer = null;

    Function<Consumer<String>, Consumer<InputStream>> lineConsumerToStreamConsumer =
        (lineConsumer) ->
            inputStream -> {
              try (BufferedReader reader =
                  new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                  lineConsumer.accept(line);
                }
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            };

    if (stdOutLineConsumer != null) {
      stdOutStreamConsumer = lineConsumerToStreamConsumer.apply(stdOutLineConsumer);
    }
    if (stdErrLineConsumer != null) {
      stdErrStreamConsumer = lineConsumerToStreamConsumer.apply(stdErrLineConsumer);
    }

    return listenOnStream(process, stdOutStreamConsumer, stdErrStreamConsumer, onExit);
  }

  // 暂时一个进程只能一次 listen
  // onExit 会确保在所有 stream consumer 执行完之后、且进程退出时执行
  // @returns 会等待进程结束以及所有的 stream consumer 结束
  public static ListenableFuture<?> listenOnStream(
      Process process,
      @Nullable Consumer<InputStream> stdOutStreamConsumer,
      @Nullable Consumer<InputStream> stdErrStreamConsumer,
      @Nullable Runnable onExit) {

    return processListened.compute(
        process,
        (k, prevFuture) -> {
          checkArgument(prevFuture == null, "Process stream already listened: %s", process);

          List<ListenableFuture<?>> streamFutures = new ArrayList<>();

          if (stdOutStreamConsumer != null) {
            streamFutures.add(
                executorService.submit(
                    () -> stdOutStreamConsumer.accept(process.getInputStream())));
          }

          if (stdErrStreamConsumer != null) {
            streamFutures.add(
                executorService.submit(
                    () -> stdErrStreamConsumer.accept(process.getErrorStream())));
          }

          SettableFuture<Process> future = SettableFuture.create();

          executorService.submit(
              () -> {
                try {
                  process.waitFor();
                } catch (InterruptedException e) {
                  log.info("interrupted", e);
                } finally {
                  processListened.remove(process);
                  future.set(process);
                  Futures.allAsList(streamFutures)
                      .addListener(
                          () -> Optional.ofNullable(onExit).ifPresent(Runnable::run),
                          executorService);
                }
              });

          List<ListenableFuture<?>> futures = new ArrayList<>(streamFutures);
          futures.add(future);
          return Futures.allAsList(futures);
        });
  }

  public static void logExecutionError(
      ListenableFuture<?> future, String[] cmd, StringBuilder stdErrOutBuilder) {
    Futures.addCallback(
        future,
        new FutureCallback<Object>() {
          @Override
          public void onSuccess(@Nullable Object result) {}

          @Override
          public void onFailure(@NotNull Throwable t) {
            log.warn(
                "Error executing\n    {}:\n{}",
                toCmdString(cmd),
                stdErrOutBuilder.toString().replaceAll("\n", "\n    "),
                t);
          }
        },
        executorService);
  }

  public static String toCmdString(String[] cmd) {
    if (cmd.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cmd.length; i++) {
      if (i == 0) {
        sb.append(cmd[i]);
      } else {
        sb.append(" '").append(cmd[i]).append("'");
      }
    }
    return sb.toString();
  }
}
