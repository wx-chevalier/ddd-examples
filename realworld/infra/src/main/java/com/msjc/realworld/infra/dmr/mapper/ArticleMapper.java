package com.msjc.realworld.infra.dmr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msjc.realworld.domain.article.ArticleDO;
import com.msjc.realworld.domain.article.TagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDO> {

  ArticleDO findById(@Param("id") Long id);

  TagDO findTag(@Param("tagName") String tagName);

  void insertTag(@Param("tag") TagDO tag);

  void insertArticleTagRelation(@Param("articleId") Long articleId, @Param("tagId") Long tagId);

  ArticleDO findBySlug(@Param("slug") String slug);
}
