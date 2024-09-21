package icu.ngte.udma.core.tools.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

public class MapUtils {

  /** 从Map中根据多个KEY过滤数据 */
  public static <K, V> List<V> get(Map<K, V> source, Collection<K> targetKeys) {
    if (CollectionUtils.isEmpty(targetKeys) || CollectionUtils.isEmpty(source)) {
      return Collections.emptyList();
    }
    List<V> valueList = new ArrayList<>(targetKeys.size());
    for (K key : targetKeys) {
      V value = source.get(key);
      if (value == null) {
        continue;
      }
      valueList.add(value);
    }
    return valueList;
  }
}
