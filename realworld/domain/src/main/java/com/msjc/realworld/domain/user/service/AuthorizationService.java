package com.msjc.realworld.domain.user.service;


import com.msjc.realworld.domain.article.ArticleDO;
import com.msjc.realworld.domain.comment.CommentDO;
import com.msjc.realworld.domain.user.UserDO;

public class AuthorizationService {

  public static boolean canWriteArticle(UserDO user, ArticleDO article) {
    return user.getId().equals(article.getUserId());
  }

  public static boolean canWriteComment(UserDO user, ArticleDO article, CommentDO comment) {
    return user.getId().equals(article.getUserId()) || user.getId().equals(comment.getUserId());
  }
}
