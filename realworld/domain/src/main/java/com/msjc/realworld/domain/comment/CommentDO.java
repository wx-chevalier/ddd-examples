package com.msjc.realworld.domain.comment;

import com.baomidou.mybatisplus.annotation.TableName;
import com.udma.core.infra.mybatis.basedo.BaseEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"body", "userId", "articleId"})
@TableName("comments")
public class CommentDO extends BaseEntity<CommentDO> {

  private String body;
  private Long userId;
  private Long articleId;
  private LocalDateTime createdAt;

  public CommentDO(String body, Long userId, Long articleId) {
    this.uuid = UUID.randomUUID().toString();

    this.body = body;
    this.userId = userId;
    this.articleId = articleId;
    this.createdAt = LocalDateTime.now();
  }
}
