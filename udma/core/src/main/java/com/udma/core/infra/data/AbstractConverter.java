package com.udma.core.infra.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.udma.core.tools.ds.JsonTools;
import com.udma.core.tools.lang.BeanUtils;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractConverter<T, S> {

  protected AbstractConverter() {}

  public S convertTo(T t) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public T convertFrom(S s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  // A 和 B 都需要实现 editable 接口，将只拷贝该接口对应字段
  protected <A, B> void copyProperties(A src, B dst, Class<?> editable) {
    BeanUtils.copyProperties(src, dst, editable);
  }

  protected <A, B> void copyProperties(A src, B dst) {
    BeanUtils.copyProperties(src, dst);
  }

  protected <A, B> void copyPropertiesExcludes(A src, B dst, String... excludeProperties) {
    BeanUtils.copyPropertiesExcludes(src, dst, excludeProperties);
  }

  protected <A, B> void copyPropertiesIncludes(A src, B dst, String... properties) {
    BeanUtils.copyPropertiesIncludes(src, dst, properties);
  }

  protected <A, B> B convertNullable(A t, Function<A, B> mapper) {
    return Optional.ofNullable(t).map(mapper).orElse(null);
  }

  protected <A, B> void copyTimeFields(A a, B b) {
    copyPropertiesIncludes(a, b, "createdAt", "updatedAt");
  }

  protected ZonedDateTime convertNullableInstant(Instant instant) {
    return convertNullable(instant, v -> v.atZone(ZoneId.systemDefault()));
  }

  protected Instant convertNullableZonedDateTime(ZonedDateTime zonedDateTime) {
    return convertNullable(zonedDateTime, ChronoZonedDateTime::toInstant);
  }

  protected String toString(Object object) {
    return JsonTools.toString(object);
  }

  protected <V> V fromString(String json, TypeReference<V> typeReference) {
    return JsonTools.fromString(json, typeReference);
  }

  protected <V> V fromString(String json, Class<V> clazz) {
    return JsonTools.fromString(json, clazz);
  }

  protected <V> V fromStringOrGet(
      String json, TypeReference<V> typeReference, Supplier<V> defaultValueGetter) {
    return JsonTools.fromStringOrGet(json, typeReference, defaultValueGetter);
  }

  protected <V> V fromStringOrGet(String json, Class<V> clazz, Supplier<V> defaultValueGetter) {
    return JsonTools.fromStringOrGet(json, clazz, defaultValueGetter);
  }

  protected <V> V fromString(String json, TypeReference<V> typeReference, V defaultValue) {
    return JsonTools.fromString(json, typeReference, defaultValue);
  }

  protected <V> V fromString(String json, Class<V> clazz, V defaultValue) {
    return JsonTools.fromString(json, clazz, defaultValue);
  }
}
