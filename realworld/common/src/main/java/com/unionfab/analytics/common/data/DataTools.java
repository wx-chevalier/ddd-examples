package com.msjc.analytics.common.data;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.msjc.domain.EntityId.DEFAULT_MOST_SIGNIFICANT_BYTES;

import com.msjc.domain.EntityId;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class DataTools {

  static final ConcurrentMap<Class<?>, Function<UUID, EntityId>> entityIdUUIDConstructor =
      new ConcurrentHashMap<>();
  static final ConcurrentMap<Class<?>, Function<Long, EntityId>> entityIdLongConstructors =
      new ConcurrentHashMap<>();

  @SuppressWarnings({"unchecked", "ConstantConditions"})
  public static <T extends EntityId> Function<Long, T> createLongConstructorForEntityId(
      Class<?> clazz) {
    Function<Long, EntityId> tmp =
        entityIdLongConstructors.computeIfAbsent(
            clazz,
            k ->
                id -> {
                  Function<UUID, EntityId> uuidIdConstructor = getUUIDConstructor(clazz);
                  UUID uuid = new UUID(DEFAULT_MOST_SIGNIFICANT_BYTES, id);
                  Object obj = checkNotNull(uuidIdConstructor.apply(uuid));
                  checkState(
                      obj instanceof EntityId, "%s: (UUID)->EntityId: %s -> %s", clazz, id, obj);
                  return (T) obj;
                });
    return (Function<Long, T>) tmp;
  }

  @SuppressWarnings("unchecked")
  public static <T extends EntityId> Function<UUID, T> getUUIDConstructor(Class<?> clazz) {
    Function<UUID, EntityId> uuidObjectFunction =
        entityIdUUIDConstructor.computeIfAbsent(
            clazz,
            k -> {
              try {
                @SuppressWarnings("rawtypes")
                Constructor constructor =
                    checkNotNull(clazz.getConstructor(UUID.class), "%s: (UUID)->EntityId", clazz);
                return id -> {
                  if (id == null) {
                    return null;
                  }
                  try {
                    Object obj = checkNotNull(constructor.newInstance(id));
                    checkState(
                        obj instanceof EntityId, "%s: (UUID)->EntityId: %s -> %s", clazz, id, obj);
                    return (EntityId) obj;
                  } catch (InstantiationException
                      | IllegalAccessException
                      | InvocationTargetException e) {
                    throw new RuntimeException(e);
                  }
                };
              } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
              }
            });
    return (Function<UUID, T>) uuidObjectFunction;
  }
}
