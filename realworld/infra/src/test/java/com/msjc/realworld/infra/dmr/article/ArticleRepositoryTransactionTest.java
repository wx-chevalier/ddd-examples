package com.msjc.realworld.infra.dmr.article;

import static org.junit.Assert.assertNull;
import com.msjc.realworld.domain.article.ArticleDO;
import com.msjc.realworld.domain.article.ArticleRepository;
import com.msjc.realworld.domain.user.UserDO;
import com.msjc.realworld.domain.user.UserRepository;
import com.msjc.realworld.infra.dmr.MybatisTestConfig;
import com.msjc.realworld.infra.dmr.mapper.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MybatisTestConfig.class})
public class ArticleRepositoryTransactionTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void transactional_test() {
        UserDO user = new UserDO("aisensiy@gmail.com", "aisensiy", "123", "bio", "default");
        userRepository.saveUser(user);

        ArticleDO article = new ArticleDO("test", "desc", "body", new String[]{"java", "spring"}, user.getId());
        articleRepository.saveArticle(article);

        ArticleDO anotherArticle = new ArticleDO("test", "desc", "body", new String[]{"java", "spring", "other"}, user.getId());

        try {
            articleRepository.saveArticle(anotherArticle);
        } catch (Exception e) {
            assertNull(articleMapper.findTag("other"));
        }
    }

}
