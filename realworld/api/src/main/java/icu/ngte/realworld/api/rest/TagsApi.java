package icu.ngte.realworld.api.rest;

import icu.ngte.realworld.application.service.TagsQueryService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "tags")
public class TagsApi {
    private TagsQueryService tagsQueryService;

    @Autowired
    public TagsApi(TagsQueryService tagsQueryService) {
        this.tagsQueryService = tagsQueryService;
    }

    @GetMapping
    public ResponseEntity getTags() {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("tags", tagsQueryService.allTags());
        }});
    }
}
