package com.msjc.realworld.infra.dmr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msjc.realworld.domain.favorite.ArticleFavoriteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleFavoriteMapper extends BaseMapper<ArticleFavoriteVO> {

  void insertArticleFavorite(@Param("articleFavorite") ArticleFavoriteVO articleFavorite);

  void deleteArticleFavorite(@Param("favorite") ArticleFavoriteVO favorite);

  ArticleFavoriteVO find(@Param("articleId") Long articleId, @Param("userId") Long userId);
}
