package com.msjc.realworld.domain.user;

import com.udma.core.infra.mybatis.basedo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FollowRelationVO extends BaseVO<FollowRelationVO> {

  private Long userId;
  private Long targetId;
}
