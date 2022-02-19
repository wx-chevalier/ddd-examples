package icu.ngte.realworld.infra.dmr.readservice;

import icu.ngte.realworld.infra.dmr.data.*;
import icu.ngte.realworld.infra.dmr.data.ArticleData;
import icu.ngte.udma.core.type.pageable.Page;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleReadService {
  ArticleData findById(@Param("id") Long id);

  ArticleData findBySlug(@Param("slug") String slug);

  List<Long> queryArticles(
      @Param("tag") String tag,
      @Param("author") String author,
      @Param("favoritedBy") String favoritedBy,
      @Param("page") Page page);

  int countArticle(
      @Param("tag") String tag,
      @Param("author") String author,
      @Param("favoritedBy") String favoritedBy);

  List<ArticleData> findArticles(@Param("articleIds") List<Long> articleIds);

  List<ArticleData> findArticlesOfAuthors(
      @Param("authors") List<String> authors, @Param("page") Page page);

  int countFeedSize(@Param("authors") List<String> authors);
}
