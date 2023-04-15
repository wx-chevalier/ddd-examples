package icu.ngte.realworld.domain.article;

import java.util.Optional;

public interface ArticleRepository {

    boolean saveArticle(ArticleDO article);

    Optional<ArticleDO> findById(Long id);

    Optional<ArticleDO> findBySlug(String slug);

    boolean removeArticle(ArticleDO article);
}
