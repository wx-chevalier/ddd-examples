package icu.ngte.realworld.domain.user;

import icu.ngte.udma.core.infra.mybatis.basedo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FollowRelationVO extends BaseVO<FollowRelationVO> {

  private Long userId;
  private Long targetId;
}
