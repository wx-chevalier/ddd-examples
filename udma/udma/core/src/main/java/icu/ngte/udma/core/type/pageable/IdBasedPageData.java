package icu.ngte.udma.core.type.pageable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import icu.ngte.udma.core.type.model.HasId;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@JsonInclude(Include.NON_NULL)
public class IdBasedPageData<Id, T extends HasId<Id>> {

  @Getter private final List<T> data;
  @Getter private final IdBasedPageLink<Id> pageLink;

  public IdBasedPageData(@JsonProperty("data") List<T> data, IdBasedPageLink<Id> pageLink) {
    this.data = data;
    this.pageLink = pageLink;
  }

  @JsonProperty("nextPageLink")
  @Nullable
  public IdBasedPageLink<Id> getNextPageLink() {
    if (pageLink == null || data.isEmpty() || data.size() < pageLink.getLimit()) {
      return null;
    } else {
      return new IdBasedPageLink<>(
          pageLink.isAscOrder() ? data.get(data.size() - 1).getId() : data.get(0).getId(),
          pageLink.getLimit(),
          pageLink.isAscOrder());
    }
  }

  public <UId extends Comparable<UId>, U extends HasId<UId>> IdBasedPageData<UId, U> map(
      Function<T, U> mapper, Function<Id, UId> idMapper) {
    return new IdBasedPageData<>(
        data.stream().map(mapper).collect(Collectors.toList()), pageLink.map(idMapper));
  }
}
