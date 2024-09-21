package icu.ngte.realworld.infra.dmr.mapper;

import icu.ngte.realworld.domain.article.TagDO;
import icu.ngte.udma.core.infra.mybatis.handler.ExtendedMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends ExtendedMapper<TagDO> {

}
