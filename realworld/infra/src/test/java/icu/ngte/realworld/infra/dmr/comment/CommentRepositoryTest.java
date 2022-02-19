package icu.ngte.realworld.infra.dmr.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import icu.ngte.realworld.domain.comment.CommentDO;
import icu.ngte.realworld.domain.comment.CommentRepository;
import icu.ngte.realworld.infra.dmr.MybatisTestConfig;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MybatisTestConfig.class})
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void should_create_and_fetch_comment_success() {
        CommentDO comment = new CommentDO("content", 123L, 456L);
        commentRepository.saveComment(comment);

        Optional<CommentDO> optional = commentRepository.findById(456L, comment.getId());
        assertTrue(optional.isPresent());
        assertEquals(optional.get(), comment);
    }
}