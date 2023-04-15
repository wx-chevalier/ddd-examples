package icu.ngte.realworld.domain.favorite;

import icu.ngte.udma.core.infra.mybatis.basedo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleFavoriteVO extends BaseVO<ArticleFavoriteVO> {

  private Long articleId;
  private Long userId;
}
