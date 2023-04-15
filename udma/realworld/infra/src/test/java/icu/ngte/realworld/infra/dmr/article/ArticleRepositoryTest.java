package icu.ngte.realworld.infra.dmr.article;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import icu.ngte.realworld.domain.article.ArticleDO;
import icu.ngte.realworld.domain.article.ArticleRepository;
import icu.ngte.realworld.domain.article.TagDO;
import icu.ngte.realworld.domain.user.UserDO;
import icu.ngte.realworld.domain.user.UserRepository;
import icu.ngte.realworld.infra.dmr.MybatisTestConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MybatisTestConfig.class})
public class ArticleRepositoryTest {

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private UserRepository userRepository;

  private UserDO user;
  private ArticleDO article;

  @BeforeEach
  public void setUp() {
    if (this.user == null) {
      this.user = new UserDO("aisensiy@gmail.com", "aisensiy", "123", "bio", "default");
      userRepository.saveUser(user);
    }

    this.article = new ArticleDO("test", "desc", "body", new String[]{"java", "spring"},
        this.user.getId());
  }

  @Test
  @Rollback
  public void should_create_and_fetch_article_success() {
    articleRepository.saveArticle(article);

    Optional<ArticleDO> optional = articleRepository.findById(article.getId());

    assertTrue(optional.isPresent());
    assertEquals(optional.get().getTitle(), article.getTitle());

    List<TagDO> tags = optional.get().getTags();

    assertTrue(tags.contains(new TagDO("java")));
    assertTrue(tags.contains(new TagDO("spring")));
  }

  @Test
  @Rollback
  public void should_update_and_fetch_article_success() {
    articleRepository.saveArticle(article);

    String newTitle = "new test 2";
    article.update(newTitle, "", "");
    articleRepository.saveArticle(article);
    System.out.println(article.getSlug());

    Optional<ArticleDO> optional = articleRepository.findBySlug(article.getSlug());
    assertTrue(optional.isPresent());
    ArticleDO fetched = optional.get();
    assertEquals(fetched.getTitle(), newTitle);
    assertNotEquals(fetched.getBody(), "");
  }

  @Test
  @Rollback
  public void should_delete_article() {
    articleRepository.saveArticle(article);

    articleRepository.removeArticle(article);
    assertFalse(articleRepository.findById(article.getId()).isPresent());
  }
}