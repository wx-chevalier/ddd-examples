package icu.ngte.udma.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import icu.ngte.udma.core.type.model.HasId;
import icu.ngte.udma.core.type.model.HasTimeFields;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "id")
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class BaseEntity<Id extends EntityId> implements HasTimeFields, HasId<Id> {

  private Id id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
