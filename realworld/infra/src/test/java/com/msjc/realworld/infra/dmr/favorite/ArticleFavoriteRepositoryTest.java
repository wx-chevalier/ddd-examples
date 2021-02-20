package com.msjc.realworld.infra.dmr.favorite;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import com.msjc.realworld.domain.favorite.ArticleFavoriteRepository;
import com.msjc.realworld.domain.favorite.ArticleFavoriteVO;
import com.msjc.realworld.infra.dmr.MybatisTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MybatisTestConfig.class})
public class ArticleFavoriteRepositoryTest {
    @Autowired
    private ArticleFavoriteRepository articleFavoriteRepository;

    @Test
    public void should_save_and_fetch_articleFavorite_success() {
        ArticleFavoriteVO articleFavorite = new ArticleFavoriteVO(123L, 456L);
        articleFavoriteRepository.saveArticleFavorite(articleFavorite);
        assertNotNull(articleFavoriteRepository.find(articleFavorite.getArticleId(), articleFavorite.getUserId()));
    }

    @Test
    public void should_remove_favorite_success() {
        ArticleFavoriteVO articleFavorite = new ArticleFavoriteVO(123L, 456L);
        articleFavoriteRepository.saveArticleFavorite(articleFavorite);
        articleFavoriteRepository.remove(articleFavorite);
        assertFalse(articleFavoriteRepository.find(123L, 456L).isPresent());
    }
}