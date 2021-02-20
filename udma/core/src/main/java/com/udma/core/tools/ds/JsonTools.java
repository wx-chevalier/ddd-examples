package com.udma.core.tools.ds;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vavr.jackson.datatype.VavrModule;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

// toXXX 转换为 JSON 对象/JSON 字符串；可以定制 null 对象返回的字符串，默认返回 null 值序列化的结果，也即 "null"
// fromXXX 从 JSON 字符串/JSON 对象转换回指定类型，转换异常抛出
// fromXXX(..., defaultValue) 同 fromXXX，如果异常，则返回 defaultValue
// fromXXXOrGet(..., defaultValueGetter) 同 fromXXX，如果异常，计算 defaultValueGetter 返回
// - from 系列函数，输入 null 返回 null
public class JsonTools {

  public static final ObjectMapper JSON =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.DEFAULT)
          .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
          .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
          .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
          .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
          .registerModule(new Jdk8Module())
          .registerModule(new JavaTimeModule())
          .registerModule(new VavrModule());

  public static void registerModules(ObjectMapper objectMapper) {
    objectMapper
        .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.DEFAULT)
        .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule())
        .registerModule(new VavrModule());
  }

  public static ObjectNode newObjectNode() {
    return JSON.createObjectNode();
  }

  public static ObjectNode newObjectNode(Consumer<ObjectNode> nodeConsumer) {
    ObjectNode objectNode = JSON.createObjectNode();
    nodeConsumer.accept(objectNode);
    return objectNode;
  }

  public static ArrayNode newArrayNode() {
    return JSON.createArrayNode();
  }

  public static ArrayNode newArrayNode(Consumer<ArrayNode> nodeConsumer) {
    ArrayNode arrayNode = JSON.createArrayNode();
    nodeConsumer.accept(arrayNode);
    return arrayNode;
  }

  public static boolean isNull(JsonNode node) {
    return node == null || node.isNull();
  }

  public static JsonNode toJsonNode(Object object) {
    return JSON.valueToTree(object);
  }

  // node == null -> null
  @Nullable
  public static <T> T fromJsonNode(@Nullable JsonNode node, Class<T> clazz) {
    return JSON.convertValue(node, clazz);
  }

  // ignore error and calculate default value
  @Nullable
  public static <T> T fromJsonNodeOrGet(
      @Nullable JsonNode node, Class<T> clazz, Supplier<T> defaultValGetter) {
    try {
      return fromJsonNode(node, clazz);
    } catch (Throwable t) {
      return defaultValGetter.get();
    }
  }

  // ignore error and return default value
  @Nullable
  public static <T> T fromJsonNode(@Nullable JsonNode node, Class<T> clazz, T defaultValue) {
    return fromJsonNodeOrGet(node, clazz, () -> defaultValue);
  }

  // node == null -> null
  @Nullable
  public static <T> T fromJsonNode(@Nullable JsonNode node, TypeReference<T> typeReference) {
    return JSON.convertValue(node, typeReference);
  }

  // ignore error and calculate default value
  @Nullable
  public static <T> T fromJsonNodeOrGet(
      @Nullable JsonNode node, TypeReference<T> typeReference, Supplier<T> defaultValGetter) {
    try {
      return fromJsonNode(node, typeReference);
    } catch (Throwable t) {
      return defaultValGetter.get();
    }
  }

  // ignore error and return default value
  @Nullable
  public static <T> T fromJsonNode(
      @Nullable JsonNode node, TypeReference<T> typeReference, T defaultValue) {
    return fromJsonNodeOrGet(node, typeReference, () -> defaultValue);
  }

  @Nullable
  public static JsonNode fromBytes(@Nullable byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    try {
      return JSON.readTree(bytes);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static JsonNode fromBytesOrGet(byte[] bytes, Supplier<JsonNode> defaultValueGetter) {
    try {
      return fromBytes(bytes);
    } catch (Throwable t) {
      return defaultValueGetter.get();
    }
  }

  public static JsonNode fromBytes(byte[] bytes, JsonNode defaultValue) {
    return fromBytesOrGet(bytes, () -> defaultValue);
  }

  public static JsonNode fromString(String data) {
    if (data == null) {
      return null;
    }
    try {
      return JSON.readTree(data);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static JsonNode fromStringOrGet(String data, Supplier<JsonNode> defaultValueGetter) {
    try {
      return fromString(data);
    } catch (Throwable t) {
      return defaultValueGetter.get();
    }
  }

  public static JsonNode fromString(String data, JsonNode defaultValue) {
    return fromStringOrGet(data, () -> defaultValue);
  }

  @Nullable
  public static <V> V fromString(String json, TypeReference<V> typeReference) {
    if (json == null) {
      return null;
    }
    try {
      return JSON.readValue(json, typeReference);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Nullable
  public static <V> V fromString(String json, Class<V> clazz) {
    if (json == null) {
      return null;
    }
    try {
      return JSON.readValue(json, clazz);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static <V> V fromString(String json, TypeReference<V> typeReference, V defaultValue) {
    return fromStringOrGet(json, typeReference, () -> defaultValue);
  }

  public static <V> V fromString(String json, Class<V> clazz, V defaultValue) {
    return fromStringOrGet(json, clazz, () -> defaultValue);
  }

  public static <V> V fromStringOrGet(
      String json, TypeReference<V> typeReference, Supplier<V> defaultValueGetter) {
    try {
      return JSON.readValue(json, typeReference);
    } catch (Exception e) {
      return defaultValueGetter.get();
    }
  }

  public static <V> V fromStringOrGet(
      String json, TypeReference<V> typeReference, Function<String, V> defaultValueGetter) {
    try {
      return JSON.readValue(json, typeReference);
    } catch (Exception e) {
      return defaultValueGetter.apply(json);
    }
  }

  public static <V> V fromStringOrGet(String json, Class<V> clazz, Supplier<V> defaultValueGetter) {
    try {
      return JSON.readValue(json, clazz);
    } catch (Exception e) {
      return defaultValueGetter.get();
    }
  }

  public static String toStringOrGetNullString(Object object, Supplier<String> nullStringGetter) {
    if (object == null) {
      return nullStringGetter.get();
    }
    return toString(object);
  }

  public static String toString(Object object, String nullString) {
    return toStringOrGetNullString(object, () -> nullString);
  }

  public static String toString(Object object) {
    try {
      return JSON.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String toString(JsonNode node) {
    try {
      return JSON.writeValueAsString(node);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static byte[] toBytes(JsonNode node) {
    try {
      return JSON.writeValueAsBytes(node);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }
}
