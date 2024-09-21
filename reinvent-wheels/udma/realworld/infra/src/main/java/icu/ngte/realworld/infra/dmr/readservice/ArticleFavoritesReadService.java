package icu.ngte.realworld.infra.dmr.readservice;

import icu.ngte.realworld.domain.user.UserDO;
import icu.ngte.realworld.infra.dmr.data.ArticleFavoriteCount;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleFavoritesReadService {
  boolean isUserFavorite(@Param("userId") Long userId, @Param("articleId") Long articleId);

  int articleFavoriteCount(@Param("articleId") Long articleId);

  List<ArticleFavoriteCount> articlesFavoriteCount(@Param("ids") List<Long> ids);

  Set<String> userFavorites(
      @Param("ids") List<Long> ids, @Param("currentUser") UserDO currentUser);
}
