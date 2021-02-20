package com.msjc.realworld.domain.favorite;

import com.udma.core.infra.mybatis.basedo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleFavoriteVO extends BaseVO<ArticleFavoriteVO> {

  private Long articleId;
  private Long userId;
}
