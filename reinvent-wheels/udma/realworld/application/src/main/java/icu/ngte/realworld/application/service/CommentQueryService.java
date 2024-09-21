package icu.ngte.realworld.application.service;

import icu.ngte.realworld.domain.user.UserDO;
import icu.ngte.realworld.infra.dmr.data.CommentData;
import icu.ngte.realworld.infra.dmr.readservice.CommentReadService;
import icu.ngte.realworld.infra.dmr.readservice.UserRelationshipReadService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommentQueryService {
  private CommentReadService commentReadService;
  private UserRelationshipReadService userRelationshipReadService;

  public CommentQueryService(
      CommentReadService commentReadService,
      UserRelationshipReadService userRelationshipReadService) {
    this.commentReadService = commentReadService;
    this.userRelationshipReadService = userRelationshipReadService;
  }

  public Optional<CommentData> findById(Long id, UserDO user) {
    CommentData commentData = commentReadService.findById(id);
    if (commentData == null) {
      return Optional.empty();
    } else {
      commentData
          .getProfileData()
          .setFollowing(
              userRelationshipReadService.isUserFollowing(
                  user.getId(), commentData.getProfileData().getId()));
    }
    return Optional.ofNullable(commentData);
  }

  public List<CommentData> findByArticleId(Long articleId, UserDO user) {
    List<CommentData> comments = commentReadService.findByArticleId(articleId);
    if (comments.size() > 0 && user != null) {
      Set<String> followingAuthors =
          userRelationshipReadService.followingAuthors(
              user.getId(),
              comments.stream()
                  .map(commentData -> commentData.getProfileData().getId())
                  .collect(Collectors.toList()));
      comments.forEach(
          commentData -> {
            if (followingAuthors.contains(commentData.getProfileData().getId())) {
              commentData.getProfileData().setFollowing(true);
            }
          });
    }
    return comments;
  }
}
