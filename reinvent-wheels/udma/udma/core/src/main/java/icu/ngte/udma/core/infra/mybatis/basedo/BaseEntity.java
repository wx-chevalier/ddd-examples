package icu.ngte.udma.core.infra.mybatis.basedo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class BaseEntity<ConcreteBaseDO extends BaseEntity<ConcreteBaseDO>> extends
    BaseVO<ConcreteBaseDO> {

  @TableField(value = FieldNames.UUID)
  protected String uuid;

  public ConcreteBaseDO setUuid(String uuid) {
    this.uuid = uuid;
    return (ConcreteBaseDO) this;
  }
}
