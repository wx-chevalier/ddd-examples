package icu.ngte.realworld.infra.dmr.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.ngte.realworld.domain.article.ArticleDO;
import icu.ngte.realworld.domain.article.ArticleRepository;
import icu.ngte.realworld.domain.article.TagDO;
import icu.ngte.realworld.infra.dmr.mapper.ArticleMapper;
import icu.ngte.realworld.infra.dmr.mapper.TagMapper;
import io.vavr.collection.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleRepositoryImpl extends ServiceImpl<ArticleMapper, ArticleDO> implements
    ArticleRepository {

  @Autowired
  TagMapper tagMapper;

  /**
   * 插入文章，注意，这里需要分两步依次插入文章与标签
   *
   * @param article
   * @return
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean saveArticle(ArticleDO article) {
    this.saveOrUpdate(article);

    List<Long> tagIds = List.ofAll(article.getTags()).map(tag -> {
      TagDO tagDO = Optional.ofNullable(this.tagMapper.selectOne(
          new QueryWrapper<TagDO>().eq("name", tag.getName())
      )).orElseGet(() -> {
        // 这里会赋予新的 id
        tagMapper.insert(tag);
        this.baseMapper.insertArticleTagRelation(article.getId(), tag.getId());
        return tag;
      });
      return tagDO.getId();
    });

    return tagIds.length() == article.getTags().size();
  }

  @Override
  public Optional<ArticleDO> findById(Long id) {
    return Optional.ofNullable(this.baseMapper.findById(id));
  }

  @Override
  public Optional<ArticleDO> findBySlug(String slug) {
    return Optional.ofNullable(this.baseMapper.findBySlug(slug));
  }

  @Override
  public boolean removeArticle(ArticleDO article) {
    return this.removeById(article.getId());
  }
}
