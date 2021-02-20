package com.udma.core.type.dto;

import com.google.common.base.Strings;
import com.udma.core.type.pageable.SortOrder;
import com.udma.core.type.pageable.SortOrder.Direction;
import io.vavr.API;
import io.vavr.collection.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;

@Data
public class ParamSortOrders {

  private static final Pattern SORT_BY_PATTERN = Pattern.compile("^(asc|desc)\\(\\s*(.+)\\s*\\)$");

  // 逗号分割的 asc(property) 或者 desc(property) 列表
  String sortBy;

  public List<SortOrder> getSortOrders() {
    if (sortBy == null) {
      return API.List();
    } else {
      List<String> items =
          API.List(sortBy.split(","))
              .map(v -> Strings.emptyToNull(v.trim()))
              .filter(Objects::nonNull);
      return items.map(ParamSortOrders::parseSortBy);
    }
  }

  private static SortOrder parseSortBy(String sortBy) {
    sortBy = sortBy.trim();
    Matcher m = SORT_BY_PATTERN.matcher(sortBy);
    if (m.find()) {
      return new SortOrder(m.group(2), Direction.valueOf(m.group(1).toUpperCase()));
    } else {
      throw new IllegalArgumentException("无效排序字段，合法格式为 asc(property) 或 desc(property): " + sortBy);
    }
  }
}
