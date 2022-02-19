package icu.ngte.realworld.infra.dmr.readservice;

import icu.ngte.realworld.infra.dmr.data.*;
import icu.ngte.realworld.infra.dmr.data.CommentData;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentReadService {
  CommentData findById(@Param("id") Long id);

  List<CommentData> findByArticleId(@Param("articleId") Long articleId);
}
