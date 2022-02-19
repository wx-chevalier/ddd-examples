package icu.ngte.udma.core.type.exception;

import com.google.common.base.Strings;
import icu.ngte.udma.core.domain.entity.EntityId;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseBizException {

  @Getter private String entityType;

  @Getter private Collection<EntityId> entityIds = Collections.emptyList();

  public static Supplier<NotFoundException> of(String entityType, String message) {
    return () -> new NotFoundException(entityType, message);
  }

  public static Supplier<NotFoundException> of(String entityType, String template, Object... args) {
    return () -> new NotFoundException(entityType, template, args);
  }

  public static Supplier<NotFoundException> of(EntityId entityId) {
    return () -> new NotFoundException(entityId);
  }

  public static Supplier<NotFoundException> of(EntityId entityId, String message) {
    return () -> new NotFoundException(entityId, message);
  }

  public static Supplier<NotFoundException> of(EntityId entityId, String template, Object... args) {
    return () -> new NotFoundException(entityId, template, args);
  }

  public static Supplier<NotFoundException> of(Collection<EntityId> entityIds) {
    return () -> new NotFoundException(entityIds);
  }

  public static Supplier<NotFoundException> of(Collection<EntityId> entityIds, String message) {
    return () -> new NotFoundException(entityIds, message);
  }

  public static Supplier<NotFoundException> of(
      Collection<EntityId> entityIds, String template, Object... args) {
    return () -> new NotFoundException(entityIds, template, args);
  }

  private static String toMessage(
      @Nullable String entityType,
      Collection<EntityId> entityIds,
      @Nullable String template,
      Object... args) {
    String entityPart;

    if (entityType != null) {
      entityPart = String.format("%s %s", entityType, entityIds);
    } else {
      entityPart = String.format("%s", entityIds);
    }

    if (template != null) {
      return String.format("%s not found: %s", entityPart, Strings.lenientFormat(template, args));
    } else {
      return String.format("%s not found", entityPart);
    }
  }

  public NotFoundException(
      @Nullable String entityType,
      Collection<EntityId> entityIds,
      @Nullable String template,
      Object... args) {
    super(toMessage(entityType, entityIds, template, args), HttpStatus.NOT_FOUND);
    this.entityIds = entityIds;
    if (entityType != null) {
      this.entityType = entityType;
    } else if (entityIds.size() != 0) {
      this.entityType = entityIds.iterator().next().getEntityType();
    }
  }

  public NotFoundException(EntityId entityId, String template, Object... args) {
    this(entityId.getEntityType(), Collections.singletonList(entityId), template, args);
  }

  public NotFoundException(EntityId entityId, String message) {
    this(entityId.getEntityType(), Collections.singletonList(entityId), message);
  }

  public NotFoundException(Collection<EntityId> entityIds, String template, Object... args) {
    this(null, entityIds, template, args);
  }

  public NotFoundException(Collection<EntityId> entityIds, String message) {
    this(null, entityIds, message);
  }

  public NotFoundException(EntityId entityId) {
    this(null, Collections.singletonList(entityId), null);
  }

  public NotFoundException(Collection<EntityId> entityIds) {
    this(null, entityIds, null);
  }

  public NotFoundException(String entityType, String template, Object... args) {
    this(entityType, Collections.emptyList(), template, args);
  }

  public NotFoundException(String entityType, String message) {
    this(entityType, Collections.emptyList(), message);
  }
}
