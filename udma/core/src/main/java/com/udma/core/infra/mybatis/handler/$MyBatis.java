package com.udma.core.infra.mybatis.handler;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.udma.core.domain.entity.EntityId;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

class $MyBatis {

  public static long DEFAULT_MOST_SIGNIFICANT_BYTES = 424242424242424242L;

  @SuppressWarnings({"unchecked", "ConstantConditions"})
  static <T extends EntityId> Function<Long, T> createLongConstructorForEntityId(Class<?> clazz) {
    return id -> {
      Function<UUID, EntityId> uuidIdConstructor = getUUIDIdConstructor(clazz);
      UUID uuid = new UUID(DEFAULT_MOST_SIGNIFICANT_BYTES, id);
      Object obj = checkNotNull(uuidIdConstructor.apply(uuid));
      checkState(obj instanceof EntityId, "%s: (UUID)->EntityId: %s -> %s", clazz, id, obj);
      return (T) obj;
    };
  }

  @SuppressWarnings("unchecked")
  static <T extends EntityId> Function<UUID, T> getUUIDIdConstructor(Class<?> clazz) {
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
          checkState(obj instanceof EntityId, "%s: (UUID)->EntityId: %s -> %s", clazz, id, obj);
          return (T) obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      };
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  static byte[] uuidToBytes(@Nullable UUID uuid) {
    if (uuid != null) {
      ByteBuffer wrap = ByteBuffer.wrap(new byte[16]);
      wrap.putLong(uuid.getMostSignificantBits());
      wrap.putLong(uuid.getLeastSignificantBits());
      return wrap.array();
    }
    return null;
  }

  static UUID bytesToUUID(@Nullable byte[] bytes) {
    if (bytes != null) {
      ByteBuffer bb = ByteBuffer.wrap(bytes);
      long firstLong = bb.getLong();
      long secondLong = bb.getLong();
      return new UUID(firstLong, secondLong);
    }
    return null;
  }
}
