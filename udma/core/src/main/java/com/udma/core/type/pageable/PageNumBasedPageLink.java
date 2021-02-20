package com.udma.core.type.pageable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class PageNumBasedPageLink {

  // zero based page number
  private final int pageNum;

  private final int pageSize;

  @Nullable private final SortOrder sortOrder;

  public PageNumBasedPageLink(Integer pageNum, Integer pageSize, @Nullable SortOrder sortOrder) {
    this.pageNum = pageNum == null ? 0 : pageNum;
    this.pageSize = pageSize == null ? 10 : pageSize;
    this.sortOrder = sortOrder;
  }

  public PageNumBasedPageLink(Integer pageNum, Integer pageSize) {
    this(pageNum, pageSize, null);
  }

  public PageNumBasedPageLink() {
    this(0, 10);
  }

  @JsonIgnore
  public PageNumBasedPageLink nextPage() {
    return new PageNumBasedPageLink(getPageNum() + 1, getPageSize(), sortOrder);
  }
}
