package com.udma.core.infra.mybatis.basedo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.udma.core.type.model.HasDeleteTimeField;
import com.udma.core.type.model.HasId;
import com.udma.core.type.model.HasTimeFields;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "deletedAt"})
@SuppressWarnings("unchecked")
public abstract class BaseVO<ConcreteBaseDO extends BaseVO<ConcreteBaseDO>>
    implements Serializable, HasId<Long>, HasTimeFields, HasDeleteTimeField {

  @TableId(value = FieldNames.ID, type = IdType.AUTO)
  protected Long id;

  @TableField(value = FieldNames.CREATED_AT)
  protected LocalDateTime createdAt;

  @TableField(value = FieldNames.UPDATED_AT)
  protected LocalDateTime updatedAt;

  @TableField(value = FieldNames.DELETED_AT)
  protected LocalDateTime deletedAt;

  public ConcreteBaseDO setId(Long id) {
    this.id = id;
    return (ConcreteBaseDO) this;
  }

  public ConcreteBaseDO setTenantId(Long tenantId) {
    return (ConcreteBaseDO) this;
  }

  public ConcreteBaseDO setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return (ConcreteBaseDO) this;
  }

  public ConcreteBaseDO setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return (ConcreteBaseDO) this;
  }

  public ConcreteBaseDO setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
    return (ConcreteBaseDO) this;
  }
}
