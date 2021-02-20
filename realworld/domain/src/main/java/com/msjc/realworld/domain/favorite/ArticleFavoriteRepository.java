package com.msjc.realworld.domain.favorite;

import java.util.Optional;

public interface ArticleFavoriteRepository {

  void saveArticleFavorite(ArticleFavoriteVO articleFavorite);

  Optional<ArticleFavoriteVO> find(Long articleId, Long userId);

  void remove(ArticleFavoriteVO favorite);
}
