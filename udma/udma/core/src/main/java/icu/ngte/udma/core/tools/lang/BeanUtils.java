package icu.ngte.udma.core.tools.lang;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class BeanUtils {

  public static <A, B> void copyProperties(A src, B dst, Class<?> editable) {
    org.springframework.beans.BeanUtils.copyProperties(src, dst, editable);
  }

  public static <A, B> void copyProperties(A src, B dst) {
    org.springframework.beans.BeanUtils.copyProperties(src, dst);
  }

  public static <A, B> void copyPropertiesExcludes(A src, B dst, String... excludeProperties) {
    org.springframework.beans.BeanUtils.copyProperties(src, dst, excludeProperties);
  }

  public static <A, B> void copyPropertiesIncludes(A src, B dst, String... properties) {
    Set<String> props = new HashSet<>(Arrays.asList(properties));
    String[] excludedProperties =
        Arrays.stream(org.springframework.beans.BeanUtils.getPropertyDescriptors(src.getClass()))
            .map(PropertyDescriptor::getName)
            .filter(name -> !props.contains(name))
            .toArray(String[]::new);
    org.springframework.beans.BeanUtils.copyProperties(src, dst, excludedProperties);
  }

  public static <A, B> B convertNullable(A t, Function<A, B> mapper) {
    return Optional.ofNullable(t).map(mapper).orElse(null);
  }

  public static <A, B> void copyTimeFields(A a, B b) {
    copyPropertiesIncludes(a, b, "createdAt", "updatedAt");
  }
}
