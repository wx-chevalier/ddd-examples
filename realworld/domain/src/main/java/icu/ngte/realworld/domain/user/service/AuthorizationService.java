package icu.ngte.realworld.domain.user.service;


import icu.ngte.realworld.domain.article.ArticleDO;
import icu.ngte.realworld.domain.comment.CommentDO;
import icu.ngte.realworld.domain.user.UserDO;

public class AuthorizationService {

  public static boolean canWriteArticle(UserDO user, ArticleDO article) {
    return user.getId().equals(article.getUserId());
  }

  public static boolean canWriteComment(UserDO user, ArticleDO article, CommentDO comment) {
    return user.getId().equals(article.getUserId()) || user.getId().equals(comment.getUserId());
  }
}
