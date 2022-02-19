package icu.ngte.realworld.domain.article;

import com.baomidou.mybatisplus.annotation.TableName;
import icu.ngte.udma.core.infra.mybatis.basedo.BaseEntity;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"name"})
@TableName("tags")
public class TagDO extends BaseEntity<TagDO> {
    private String name;

    public TagDO(String name) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
    }
}
