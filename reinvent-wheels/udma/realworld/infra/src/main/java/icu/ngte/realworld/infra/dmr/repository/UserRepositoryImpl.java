package icu.ngte.realworld.infra.dmr.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.ngte.realworld.domain.user.FollowRelationVO;
import icu.ngte.realworld.domain.user.UserDO;
import icu.ngte.realworld.domain.user.UserRepository;
import icu.ngte.realworld.infra.dmr.mapper.UserMapper;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserDO> implements UserRepository {

  @Override
  public boolean saveUser(UserDO user) {
    return this.saveOrUpdate(user);
  }

  @Override
  public Optional<UserDO> findById(String id) {
    return Optional.ofNullable(this.getById(id));
  }

  @Override
  public Optional<UserDO> findByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      return Optional.empty();
    }

    Wrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>().eq(UserDO::getUsername, username);

    return Optional.ofNullable(getOne(wrapper));
  }

  @Override
  public Optional<UserDO> findByEmail(String email) {
    return Optional.ofNullable(this.baseMapper.findByEmail(email));
  }

  @Override
  public void saveRelation(FollowRelationVO followRelation) {
    this.baseMapper.saveRelation(followRelation);
  }

  @Override
  public Optional<FollowRelationVO> findRelation(Long userId, Long targetId) {
    return Optional.ofNullable(this.baseMapper.findRelation(userId, targetId));
  }

  @Override
  public void removeRelation(FollowRelationVO followRelation) {
    this.baseMapper.deleteRelation(followRelation);
  }
}
