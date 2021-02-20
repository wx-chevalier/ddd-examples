package com.udma.core.tools.lang;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArrayUtils {

  public static <T> List<T> toList(T[] data, List<T> defaultValue) {
    if (data == null || data.length == 0) {
      return defaultValue;
    }
    return Arrays.asList(data);
  }

  public static <T> List<T> toList(T[] data) {
    return ArrayUtils.toList(data, Collections.emptyList());
  }

  public static <T> Set<T> toSet(T[] data, Set<T> defaultValue) {
    if (data == null || data.length == 0) {
      return defaultValue;
    }
    return Sets.newHashSet(data);
  }

  public static <T> Set<T> toSet(T[] data) {
    return Sets.newHashSet(toSet(data, Collections.emptySet()));
  }
}
