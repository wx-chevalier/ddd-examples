package com.msjc.realworld.application.service.article;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.msjc.realworld.application.service.ApplicationTestConfig;
import com.msjc.realworld.application.service.ArticleQueryService;
import com.msjc.realworld.domain.article.ArticleDO;
import com.msjc.realworld.domain.article.ArticleRepository;
import com.msjc.realworld.domain.favorite.ArticleFavoriteRepository;
import com.msjc.realworld.domain.favorite.ArticleFavoriteVO;
import com.msjc.realworld.domain.user.FollowRelationVO;
import com.msjc.realworld.domain.user.UserDO;
import com.msjc.realworld.domain.user.UserRepository;
import com.msjc.realworld.infra.dmr.data.ArticleData;
import com.msjc.realworld.infra.dmr.data.ArticleDataList;
import com.udma.core.type.pageable.Page;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ApplicationTestConfig.class})
public class ArticleQueryServiceTest {
  @Autowired private ArticleQueryService queryService;

  @Autowired private ArticleRepository articleRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ArticleFavoriteRepository articleFavoriteRepository;

  private UserDO user;
  private ArticleDO article;

  @BeforeEach
  public void setUp() {
    user = new UserDO("aisensiy@gmail.com", "aisensiy", "123", "", "");
    userRepository.saveUser(user);
    article =
        new ArticleDO(
            "test",
            "desc",
            "body",
            new String[] {"java", "spring"},
            user.getId(),
            LocalDateTime.now());
    articleRepository.saveArticle(article);
  }

  @Test
  @Rollback
  public void should_fetch_article_success() {
    Optional<ArticleData> optional = queryService.findById(article.getId(), user);
    assertTrue(optional.isPresent());

    ArticleData fetched = optional.get();
    assertEquals(fetched.getFavoritesCount(), 0);
    assertFalse(fetched.isFavorited());
    assertNotNull(fetched.getCreatedAt());
    assertNotNull(fetched.getUpdatedAt());
    assertTrue(fetched.getTagList().contains("java"));
  }

  @Test
  @Rollback
  public void should_get_article_with_right_favorite_and_favorite_count() {
    UserDO anotherUser = new UserDO("other@test.com", "other", "123", "", "");
    userRepository.saveUser(anotherUser);
    articleFavoriteRepository.saveArticleFavorite(
        new ArticleFavoriteVO(article.getId(), anotherUser.getId()));

    Optional<ArticleData> optional = queryService.findById(article.getId(), anotherUser);
    assertTrue(optional.isPresent());

    ArticleData articleData = optional.get();
    assertEquals(articleData.getFavoritesCount(), 1);
    assertTrue(articleData.isFavorited());
  }

  @Test
  @Rollback
  public void should_get_default_article_list() {
    ArticleDO anotherArticle =
        new ArticleDO(
            "new article",
            "desc",
            "body",
            new String[] {"test"},
            user.getId(),
            LocalDateTime.now().minusHours(1));
    articleRepository.saveArticle(anotherArticle);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(null, null, null, new Page(), user);
    assertEquals(recentArticles.getCount(), 2);
    assertEquals(recentArticles.getArticleDatas().size(), 2);
    assertEquals(recentArticles.getArticleDatas().get(0).getId(), article.getId());

    ArticleDataList nodata =
        queryService.findRecentArticles(null, null, null, new Page(2, 10), user);
    assertEquals(nodata.getCount(), 2);
    assertEquals(nodata.getArticleDatas().size(), 0);
  }

  @Test
  @Rollback
  public void should_query_article_by_author() {
    UserDO anotherUser = new UserDO("other@email.com", "other", "123", "", "");
    userRepository.saveUser(anotherUser);

    ArticleDO anotherArticle =
        new ArticleDO("new article", "desc", "body", new String[] {"test"}, anotherUser.getId());
    articleRepository.saveArticle(anotherArticle);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(null, user.getUsername(), null, new Page(), user);
    assertEquals(recentArticles.getArticleDatas().size(), 1);
    assertEquals(recentArticles.getCount(), 1);
  }

  @Test
  @Rollback
  public void should_query_article_by_favorite() {
    UserDO anotherUser = new UserDO("other@email.com", "other", "123", "", "");
    userRepository.saveUser(anotherUser);

    ArticleDO anotherArticle =
        new ArticleDO("new article", "desc", "body", new String[] {"test"}, anotherUser.getId());
    articleRepository.saveArticle(anotherArticle);

    ArticleFavoriteVO articleFavorite = new ArticleFavoriteVO(article.getId(), anotherUser.getId());
    articleFavoriteRepository.saveArticleFavorite(articleFavorite);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(
            null, null, anotherUser.getUsername(), new Page(), anotherUser);
    assertEquals(recentArticles.getArticleDatas().size(), 1);
    assertEquals(recentArticles.getCount(), 1);
    ArticleData articleData = recentArticles.getArticleDatas().get(0);
    assertEquals(articleData.getId(), article.getId());
    assertEquals(articleData.getFavoritesCount(), 1);
    assertTrue(articleData.isFavorited());
  }

  @Test
  @Rollback
  public void should_query_article_by_tag() {
    ArticleDO anotherArticle =
        new ArticleDO("new article", "desc", "body", new String[] {"test"}, user.getId());
    articleRepository.saveArticle(anotherArticle);

    ArticleDataList recentArticles =
        queryService.findRecentArticles("spring", null, null, new Page(), user);
    assertEquals(recentArticles.getArticleDatas().size(), 1);
    assertEquals(recentArticles.getCount(), 1);
    assertEquals(recentArticles.getArticleDatas().get(0).getId(), article.getId());

    ArticleDataList notag = queryService.findRecentArticles("notag", null, null, new Page(), user);
    assertEquals(notag.getCount(), 0);
  }

  @Test
  @Rollback
  public void should_show_following_if_user_followed_author() {
    UserDO anotherUser = new UserDO("other@email.com", "other", "123", "", "");
    userRepository.saveUser(anotherUser);

    FollowRelationVO followRelation = new FollowRelationVO(anotherUser.getId(), user.getId());
    userRepository.saveRelation(followRelation);

    ArticleDataList recentArticles =
        queryService.findRecentArticles(null, null, null, new Page(), anotherUser);
    assertEquals(recentArticles.getCount(), 1);
    ArticleData articleData = recentArticles.getArticleDatas().get(0);
    assertTrue(articleData.getProfileData().isFollowing());
  }

  @Test
  @Rollback
  public void should_get_user_feed() {
    UserDO anotherUser = new UserDO("other@email.com", "other", "123", "", "");
    userRepository.saveUser(anotherUser);

    FollowRelationVO followRelation = new FollowRelationVO(anotherUser.getId(), user.getId());
    userRepository.saveRelation(followRelation);

    ArticleDataList userFeed = queryService.findUserFeed(user, new Page());
    assertEquals(userFeed.getCount(), 0);

    ArticleDataList anotherUserFeed = queryService.findUserFeed(anotherUser, new Page());
    assertEquals(anotherUserFeed.getCount(), 1);
    ArticleData articleData = anotherUserFeed.getArticleDatas().get(0);
    assertTrue(articleData.getProfileData().isFollowing());
  }
}
