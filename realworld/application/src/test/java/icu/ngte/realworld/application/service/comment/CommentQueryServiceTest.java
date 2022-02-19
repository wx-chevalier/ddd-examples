package icu.ngte.realworld.application.service.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import icu.ngte.realworld.application.service.ApplicationTestConfig;
import icu.ngte.realworld.application.service.CommentQueryService;
import icu.ngte.realworld.domain.article.ArticleDO;
import icu.ngte.realworld.domain.article.ArticleRepository;
import icu.ngte.realworld.domain.comment.CommentDO;
import icu.ngte.realworld.domain.comment.CommentRepository;
import icu.ngte.realworld.domain.user.FollowRelationVO;
import icu.ngte.realworld.domain.user.UserDO;
import icu.ngte.realworld.domain.user.UserRepository;
import icu.ngte.realworld.infra.dmr.data.CommentData;
import java.util.List;
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
public class CommentQueryServiceTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentQueryService commentQueryService;

    @Autowired
    private ArticleRepository articleRepository;

    private UserDO user;

    @BeforeEach
    public void setUp() {
        user = new UserDO("aisensiy@test.com", "aisensiy", "123", "", "");
        userRepository.saveUser(user);
    }

    @Test
    @Rollback
    public void should_read_comment_success() {
        CommentDO comment = new CommentDO("content", user.getId(), 123L);
        commentRepository.saveComment(comment);

        Optional<CommentData> optional = commentQueryService.findById(comment.getId(), user);
        assertTrue(optional.isPresent());
        CommentData commentData = optional.get();
        assertEquals(commentData.getProfileData().getUsername(), user.getUsername());
    }

    @Test
    @Rollback
    public void should_read_comments_of_article() {
        ArticleDO article = new ArticleDO("title", "desc", "body", new String[]{"java"}, user.getId());
        articleRepository.saveArticle(article);

        UserDO user2 = new UserDO("user2@email.com", "user2", "123", "", "");
        userRepository.saveUser(user2);
        userRepository.saveRelation(new FollowRelationVO(user.getId(), user2.getId()));

        CommentDO comment1 = new CommentDO("content1", user.getId(), article.getId());
        commentRepository.saveComment(comment1);
        CommentDO comment2 = new CommentDO("content2", user2.getId(), article.getId());
        commentRepository.saveComment(comment2);

        List<CommentData> comments = commentQueryService.findByArticleId(article.getId(), user);
        assertEquals(comments.size(), 2);

    }
}