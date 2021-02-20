package com.msjc.realworld.domain.comment;

import java.util.Optional;

public interface CommentRepository {

  boolean saveComment(CommentDO comment);

  Optional<CommentDO> findById(Long articleId, Long id);

  boolean remove(CommentDO comment);
}
