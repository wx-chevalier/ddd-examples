package icu.ngte.realworld.infra.dmr.readservice;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagReadService {
  List<String> all();
}
