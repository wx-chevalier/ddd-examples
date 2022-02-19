package icu.ngte.realworld.infra.dmr.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.ngte.realworld.domain.favorite.ArticleFavoriteRepository;
import icu.ngte.realworld.domain.favorite.ArticleFavoriteVO;
import icu.ngte.realworld.infra.dmr.mapper.ArticleFavoriteMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleFavoriteRepositoryImpl extends
    ServiceImpl<ArticleFavoriteMapper, ArticleFavoriteVO> implements ArticleFavoriteRepository {

  @Override
  public void saveArticleFavorite(ArticleFavoriteVO articleFavorite) {
    if (this.baseMapper.find(articleFavorite.getArticleId(), articleFavorite.getUserId()) == null) {
      this.baseMapper.insertArticleFavorite(articleFavorite);
    }
  }

  @Override
  public Optional<ArticleFavoriteVO> find(Long articleId, Long userId) {
    return Optional.ofNullable(this.baseMapper.find(articleId, userId));
  }

  @Override
  public void remove(ArticleFavoriteVO favorite) {
      this.baseMapper.deleteArticleFavorite(favorite);
  }
}
