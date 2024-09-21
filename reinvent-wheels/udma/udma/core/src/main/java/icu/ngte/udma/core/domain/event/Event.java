package icu.ngte.udma.core.domain.event;

import java.io.Serializable;
import java.time.Instant;

public interface Event extends Serializable {

  Instant getTriggerAt();
}
