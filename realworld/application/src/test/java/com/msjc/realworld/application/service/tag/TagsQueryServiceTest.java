package com.msjc.realworld.application.service.tag;

import static org.junit.Assert.assertTrue;
import com.msjc.realworld.application.service.ApplicationTestConfig;
import com.msjc.realworld.application.service.TagsQueryService;
import com.msjc.realworld.domain.article.ArticleDO;
import com.msjc.realworld.domain.article.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ApplicationTestConfig.class})
public class TagsQueryServiceTest {
    @Autowired
    private TagsQueryService tagsQueryService;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void should_get_all_tags() {
        articleRepository.saveArticle(new ArticleDO("test", "test", "test", new String[]{"java"}, 123L));
        assertTrue(tagsQueryService.allTags().contains("java"));
    }
}