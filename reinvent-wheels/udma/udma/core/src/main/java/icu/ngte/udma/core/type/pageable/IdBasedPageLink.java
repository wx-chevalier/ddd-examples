package icu.ngte.udma.core.type.pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class IdBasedPageLink<Id> {

  // exclusively: 不包含该 id 的数据
  @Nullable private final Id idOffset;

  // 小于 0，不限制大小
  private final int limit;

  private final boolean ascOrder;

  public <UId extends Comparable<UId>> IdBasedPageLink<UId> map(Function<Id, UId> idMapper) {
    return new IdBasedPageLink<>(idMapper.apply(idOffset), limit, ascOrder);
  }
}
