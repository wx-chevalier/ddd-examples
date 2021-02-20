package com.msjc.realworld.infra.dmr.readservice;

import com.msjc.realworld.infra.dmr.data.*;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentReadService {
  CommentData findById(@Param("id") Long id);

  List<CommentData> findByArticleId(@Param("articleId") Long articleId);
}
