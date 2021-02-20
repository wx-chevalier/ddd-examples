package com.msjc.realworld.infra.dmr.mapper;

import com.msjc.realworld.domain.article.TagDO;
import com.udma.core.infra.mybatis.handler.ExtendedMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends ExtendedMapper<TagDO> {

}
