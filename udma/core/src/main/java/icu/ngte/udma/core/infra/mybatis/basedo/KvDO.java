package icu.ngte.udma.core.infra.mybatis.basedo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 本DO对象用户自定义SQL语句中使用，一般用户查询统计
 *
 * @param <T>
 * @param <V>
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KvDO<T, V> {

  private T key;

  private V value;
}
