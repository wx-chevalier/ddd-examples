package icu.ngte.realworld.infra.dmr.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.ngte.realworld.domain.comment.CommentDO;
import icu.ngte.realworld.domain.comment.CommentRepository;
import icu.ngte.realworld.infra.dmr.mapper.*;
import icu.ngte.realworld.infra.dmr.mapper.CommentMapper;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CommentRepositoryImpl extends ServiceImpl<CommentMapper, CommentDO> implements
    CommentRepository {

  @Override
  public boolean saveComment(CommentDO comment) {
    return this.saveOrUpdate(comment);
  }

  @Override
  public Optional<CommentDO> findById(Long articleId, Long id) {
    return Optional.ofNullable(this.baseMapper.findById(articleId, id));
  }

  @Override
  public boolean remove(CommentDO comment) {
    return this.removeById(comment.getId());
  }
}
