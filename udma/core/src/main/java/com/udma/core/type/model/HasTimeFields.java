package com.udma.core.type.model;

import java.time.LocalDateTime;

public interface HasTimeFields {

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();
}
