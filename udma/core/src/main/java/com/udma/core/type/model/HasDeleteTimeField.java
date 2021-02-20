package com.udma.core.type.model;

import java.time.LocalDateTime;

public interface HasDeleteTimeField {
  LocalDateTime getDeletedAt();
}
