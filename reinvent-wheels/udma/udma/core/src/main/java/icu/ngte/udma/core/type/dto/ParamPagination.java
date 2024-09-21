package icu.ngte.udma.core.type.dto;

import icu.ngte.udma.core.type.pageable.PageNumBasedPageLink;
import icu.ngte.udma.core.type.pageable.SortOrder;
import icu.ngte.udma.core.type.pageable.SortOrder.Direction;
import io.vavr.API;
import lombok.Data;

@Data
public class ParamPagination {

  Integer pageNum = 0;

  Integer pageSize = 10;

  // 按默认排序字段升序
  Boolean asc = null;

  public PageNumBasedPageLink toPageLink(String ascProperty) {
    SortOrder sortOrder =
        API.Option(asc)
            .map(v -> v ? Direction.ASC : Direction.DESC)
            .map(v -> new SortOrder(ascProperty, v))
            .getOrNull();
    return new PageNumBasedPageLink(pageNum, pageSize, sortOrder);
  }

  public PageNumBasedPageLink toPageLink() {
    return toPageLink("id");
  }
}
