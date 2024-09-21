package icu.ngte.udma.core.infra.mybatis.handler;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import icu.ngte.udma.core.type.pageable.PageNumBasedPageData;
import icu.ngte.udma.core.type.pageable.PageNumBasedPageLink;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 针对 BaseMapper 进行适当包装
 * @param <T> 包裹的 Entity 类型
 */
public interface ExtendedMapper<T> extends BaseMapper<T> {

  /**
   * 使用 mapper 获取给定条件数据的数目
   *
   * @param cond 其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @return 查询结果
   */
  default int selectCount(Consumer<LambdaQueryWrapper<T>> cond) {
    return MyBatisHelper.selectCount(this, cond);
  }

  /**
   * 使用 mapper 判断给定条件的数据是否存在
   *
   * @param cond 其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @return 查询结果
   */
  default boolean exists(Consumer<LambdaQueryWrapper<T>> cond) {
    return MyBatisHelper.exists(this, cond);
  }

  /**
   * 使用 mapper 根据给定条件查询一个结果
   *
   * @param cond 其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @return 查询结果
   */
  default Option<T> selectOne(Consumer<LambdaQueryWrapper<T>> cond) {
    return MyBatisHelper.selectOne(this, cond);
  }

  /**
   * 使用 mapper 根据给定条件查询一组结果
   *
   * @param cond 其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @return 查询结果
   */
  default List<T> selectList(Consumer<LambdaQueryWrapper<T>> cond) {
    return MyBatisHelper.selectList(this, cond);
  }

  /**
   * 同 {@link #selectList(Consumer)} 功能一样，区别在于 {@code returnsEmpty} 参数
   *
   * @param returnsEmpty 如果为 true，将不做任何查询直接返回空列表
   */
  default List<T> selectList(Consumer<LambdaQueryWrapper<T>> cond, boolean returnsEmpty) {
    return MyBatisHelper.selectList(this, cond, returnsEmpty);
  }

  /**
   * 同 {@link #selectList(Consumer)} 功能一样，区别在于 {@code returnsEmpty} 参数
   *
   * @param returnsEmpty 如果该 supplier 执行返回 true，将不做任何查询直接返回空列表
   */
  default List<T> selectList(Consumer<LambdaQueryWrapper<T>> cond, Supplier<Boolean> returnsEmpty) {
    return MyBatisHelper.selectList(this, cond, returnsEmpty);
  }

  /**
   * @param cond 定制查询条件
   * @param pageLink 分页信息
   * @return 分页数据
   */
  default PageNumBasedPageData<T> selectPage(
      Consumer<LambdaQueryWrapper<T>> cond,
      PageNumBasedPageLink pageLink,
      Supplier<Boolean> returnsEmpty) {
    return MyBatisHelper
        .selectPage(this, cond, pageLink, returnsEmpty);
  }

  default PageNumBasedPageData<T> selectPage(
      Consumer<LambdaQueryWrapper<T>> cond, PageNumBasedPageLink pageLink) {
    return MyBatisHelper.selectPage(this, cond, pageLink);
  }

  /**
   * @param cond 查询条件
   * @param pageLink 分页数据
   * @return 分页数据
   */
  default PageNumBasedPageData<T> selectPage(
      LambdaQueryWrapper<T> cond, PageNumBasedPageLink pageLink) {
    return MyBatisHelper.selectPage(this, cond, pageLink);
  }

  /**
   * 更新
   *
   * @param update 更新条件及更新数据
   */
  default int update(Consumer<LambdaUpdateWrapper<T>> update) {
    return MyBatisHelper.update(this, update);
  }

  /**
   * @param data 非空字段将用于更新
   * @param updateCond 用于构造更新条件
   */
  default int update(T data, Consumer<LambdaQueryWrapper<T>> updateCond) {
    return MyBatisHelper.update(this, data, updateCond);
  }

  /** @param removeCond 用于构造删除条件 */
  default int delete(Consumer<LambdaQueryWrapper<T>> removeCond) {
    return MyBatisHelper.delete(this, removeCond);
  }

  static <T> ExtendedMapper<T> extend(BaseMapper<T> mapper) {
    return new ExtendedMapper<T>() {
      @Override
      public int insert(T entity) {
        return mapper.insert(entity);
      }

      @Override
      public int deleteById(Serializable id) {
        return mapper.deleteById(id);
      }

      @Override
      public int deleteByMap(Map<String, Object> columnMap) {
        return mapper.deleteByMap(columnMap);
      }

      @Override
      public int delete(Wrapper<T> wrapper) {
        return mapper.delete(wrapper);
      }

      @Override
      public int deleteBatchIds(Collection<? extends Serializable> idList) {
        return mapper.deleteBatchIds(idList);
      }

      @Override
      public int updateById(T entity) {
        return mapper.updateById(entity);
      }

      @Override
      public int update(T entity, Wrapper<T> updateWrapper) {
        return mapper.update(entity, updateWrapper);
      }

      @Override
      public T selectById(Serializable id) {
        return mapper.selectById(id);
      }

      @Override
      public java.util.List<T> selectBatchIds(Collection<? extends Serializable> idList) {
        return mapper.selectBatchIds(idList);
      }

      @Override
      public java.util.List<T> selectByMap(Map<String, Object> columnMap) {
        return mapper.selectByMap(columnMap);
      }

      @Override
      public T selectOne(Wrapper<T> queryWrapper) {
        return mapper.selectOne(queryWrapper);
      }

      @Override
      public Integer selectCount(Wrapper<T> queryWrapper) {
        return mapper.selectCount(queryWrapper);
      }

      @Override
      public java.util.List<T> selectList(Wrapper<T> queryWrapper) {
        return mapper.selectList(queryWrapper);
      }

      @Override
      public java.util.List<Map<String, Object>> selectMaps(Wrapper<T> queryWrapper) {
        return mapper.selectMaps(queryWrapper);
      }

      @Override
      public java.util.List<Object> selectObjs(Wrapper<T> queryWrapper) {
        return mapper.selectObjs(queryWrapper);
      }

      @Override
      public <E extends IPage<T>> E selectPage(E page, Wrapper<T> queryWrapper) {
        return mapper.selectPage(page, queryWrapper);
      }

      @Override
      public <E extends IPage<Map<String, Object>>> E selectMapsPage(E page,
          Wrapper<T> queryWrapper) {
        return mapper.selectMapsPage(page, queryWrapper);
      }

    };
  }
}
