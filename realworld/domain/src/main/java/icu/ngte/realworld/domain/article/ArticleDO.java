package icu.ngte.realworld.domain.article;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import icu.ngte.udma.core.infra.mybatis.basedo.BaseEntity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"id"})
@TableName("articles")
public class ArticleDO extends BaseEntity<ArticleDO> {

  private Long userId;
  private String slug;
  private String title;
  private String description;
  private String body;

  // 该属性不直接插入
  @TableField(exist = false)
  private List<TagDO> tags;

  public ArticleDO(String title, String description, String body, String[] tagList, Long userId) {
    this(title, description, body, tagList, userId, LocalDateTime.now());
  }

  public ArticleDO(String title, String description, String body, String[] tagList, Long userId,
      LocalDateTime createdAt) {
    this.uuid = UUID.randomUUID().toString();
    this.slug = toSlug(title);
    this.title = title;
    this.description = description;
    this.body = body;
    this.tags = Arrays.stream(tagList).collect(toSet()).stream().map(TagDO::new).collect(toList());
    this.userId = userId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
  }

  public static String toSlug(String title) {
    return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
  }

  public void update(String title, String description, String body) {
    if (!"".equals(title)) {
      this.title = title;
      this.slug = toSlug(title);
    }
    if (!"".equals(description)) {
      this.description = description;
    }
    if (!"".equals(body)) {
      this.body = body;
    }
    this.updatedAt = LocalDateTime.now();
  }
}
