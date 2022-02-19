package icu.ngte.udma.core.type.model;

import java.time.LocalDateTime;

public interface HasDeleteTimeField {
  LocalDateTime getDeletedAt();
}
