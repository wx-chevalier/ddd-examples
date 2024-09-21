package icu.ngte.udma.core.type.pageable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.API;
import io.vavr.collection.List;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@JsonInclude(Include.NON_NULL)
public class PageNumBasedPageData<T> {

  @Getter private final List<T> data;
  @Getter private final PageNumBasedPageLink pageLink;
  @Getter private final Integer totalPage;
  @Getter private final Integer totalElements;

  public PageNumBasedPageData(PageNumBasedPageLink pageLink) {
    this.data = API.List();
    this.pageLink = pageLink;
    this.totalPage = 0;
    this.totalElements = 0;
  }

  public PageNumBasedPageData(
      List<T> data, PageNumBasedPageLink pageLink, Integer totalPage, Integer totalElements) {
    this.data = data;
    this.pageLink = pageLink;
    this.totalPage = totalPage;
    this.totalElements = totalElements;
  }

  public PageNumBasedPageData(
      List<T> data, PageNumBasedPageLink pageLink, long totalPage, long totalElements) {
    this.data = data;
    this.pageLink = pageLink;
    this.totalPage = (int) totalPage;
    this.totalElements = (int) totalElements;
  }

  public PageNumBasedPageData(
      List<T> data, PageNumBasedPageLink pageLink, Long totalPage, Long totalElements) {
    this.data = data;
    this.pageLink = pageLink;
    this.totalPage = totalPage.intValue();
    this.totalElements = totalElements.intValue();
  }

  @JsonProperty("nextPageLink")
  @Nullable
  public PageNumBasedPageLink getNextPageLink() {
    if (pageLink != null && pageLink.getPageNum() < totalPage) {
      return pageLink.nextPage();
    } else {
      return null;
    }
  }

  public <U> PageNumBasedPageData<U> map(Function<T, U> mapper) {
    return new PageNumBasedPageData<>(data.map(mapper), pageLink, totalPage, totalElements);
  }

  public PageNumBasedPageData<T> peek(Consumer<T> consumer) {
    data.forEach(consumer);
    return this;
  }

  public <U> PageNumBasedPageData<U> mapa(Function<List<T>, List<U>> mapper) {
    return new PageNumBasedPageData<>(mapper.apply(data), pageLink, totalPage, totalElements);
  }

  public PageNumBasedPageData<T> peeka(Consumer<List<T>> consumer) {
    consumer.accept(data);
    return this;
  }
}
