package icu.ngte.realworld.application.service;

import icu.ngte.realworld.infra.dmr.readservice.TagReadService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagsQueryService {
  private TagReadService tagReadService;

  public TagsQueryService(TagReadService tagReadService) {
    this.tagReadService = tagReadService;
  }

  public List<String> allTags() {
    return tagReadService.all();
  }
}
