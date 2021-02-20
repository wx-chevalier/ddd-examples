package com.msjc.realworld.infra.dmr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msjc.realworld.domain.comment.CommentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper extends BaseMapper<CommentDO> {

  CommentDO findById(@Param("articleId") Long articleId, @Param("id") Long id);
}
