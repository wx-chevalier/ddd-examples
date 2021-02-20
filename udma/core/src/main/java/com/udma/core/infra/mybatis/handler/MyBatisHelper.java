package com.udma.core.infra.mybatis.handler;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.udma.core.type.dto.Response.Pagination;
import com.udma.core.type.pageable.PageNumBasedPageData;
import com.udma.core.type.pageable.PageNumBasedPageLink;
import com.udma.core.type.pageable.SortOrder;
import com.udma.core.type.pageable.SortOrder.Direction;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Slf4j
public class MyBatisHelper {

  public static SqlSessionFactory createMyBatisSqlSessionFactory(
      DataSource dataSource, @Nullable GlobalConfig globalConfig, Interceptor... plugins)
      throws Exception {
    MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
    sqlSessionFactory.setDataSource(dataSource);
    sqlSessionFactory.setMapperLocations(
        new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));

    // mybatis configuration
    MybatisConfiguration configuration = new MybatisConfiguration();
    configuration.setJdbcTypeForNull(JdbcType.NULL);
    configuration.setMapUnderscoreToCamelCase(true);
    configuration.setDefaultExecutorType(ExecutorType.REUSE);
    // Default enum handler
    configuration.setDefaultEnumTypeHandler(EnumOrdinalTypeHandler.class);

    sqlSessionFactory.setConfiguration(configuration);

    // plugins & mybatis plus global configuration
    if (globalConfig != null) {
      sqlSessionFactory.setGlobalConfig(globalConfig);
    }
    sqlSessionFactory.setPlugins(plugins);

    return sqlSessionFactory.getObject();
  }

  public static <D> Tuple2<List<D>, Pagination> doQuery(
      Option<PageNumBasedPageLink> pageLink, Function<Page<D>, IPage<D>> pageQuery) {

    Page<D> page =
        pageLink
            .map(link -> new Page<D>(link.getPageNum() + 1, link.getPageSize()))
            .getOrElse(() -> new Page<D>(0L, -1L, -1L));

    IPage<D> pagedResult = pageQuery.apply(page);

    return Tuple.of(
        List.ofAll(pagedResult.getRecords()),
        pageLink.isEmpty()
            ? Pagination.singlePage(pagedResult.getTotal())
            : new Pagination(
                pageLink.get().getPageNum(), pagedResult.getTotal(), (int) pagedResult.getPages()));
  }


  /**
   * 扩展 MyBatis mapper，添加 MyBatisTools 中给出的方法
   *
   * @param mapper MyBatis mapper
   * @param <T>    Map 的对象
   */
  public static <T> ExtendedMapper<T> extend(BaseMapper<T> mapper) {
    return ExtendedMapper.extend(mapper);
  }

  /**
   * @param pageLink  分页数据
   * @param pageQuery 给定分页对象，查询数据到该分页对象
   * @param <T>       查询的对象
   * @return 分页数据
   */
  public static <T> PageNumBasedPageData<T> selectPage(
      PageNumBasedPageLink pageLink, Consumer<Page<T>> pageQuery) {
    Page<T> page = pageNumBasedPageLinkToPageLink(pageLink);
    pageQuery.accept(page);
    List<T> data = List.ofAll(page.getRecords());
    long pages = page.getPages();
    long total = page.getTotal();
    return new PageNumBasedPageData<T>(data, pageLink, pages, total);
  }

  /**
   * 使用 mapper 获取给定条件数据的数目
   *
   * @param mapper MyBatis mapper
   * @param cond   其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @param <T>    Map 的对象
   * @return 查询结果
   */
  public static <T> int selectCount(BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> cond) {
    LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
    cond.accept(w);
    return mapper.selectCount(w);
  }

  /**
   * 使用 mapper 判断给定条件的数据是否存在
   *
   * @param mapper MyBatis mapper
   * @param cond   其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @param <T>    Map 的对象
   * @return 查询结果
   */
  public static <T> boolean exists(BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> cond) {
    LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
    cond.accept(w);
    return mapper.selectCount(w) != 0;
  }

  /**
   * 使用 mapper 根据给定条件查询一个结果
   *
   * @param mapper MyBatis mapper
   * @param cond   其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @param <T>    Map 的对象
   * @return 查询结果
   */
  public static <T> Option<T> selectOne(
      BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> cond) {
    LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
    cond.accept(w);
    List<T> data = List.ofAll(mapper.selectList(w));
    if (data.size() > 1) {
      log.warn("got multiple result when try select one: {} - {}", mapper, cond);
    }
    return data.headOption();
  }

  /**
   * 使用 mapper 根据给定条件查询一组结果
   *
   * @param mapper MyBatis mapper
   * @param cond   其中可以定制查询条件，如可以传递 {@code w -> { w.eq(T::getId, 1); }}
   * @param <T>    Map 的对象
   * @return 查询结果
   */
  public static <T> List<T> selectList(BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> cond) {
    LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
    cond.accept(w);
    return List.ofAll(mapper.selectList(w));
  }

  /**
   * 同 {@link #selectList(BaseMapper, Consumer)} 功能一样，区别在于 {@code returnsEmpty} 参数
   *
   * @param returnsEmpty 如果为 true，将不做任何查询直接返回空列表
   */
  public static <T> List<T> selectList(
      BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> cond, boolean returnsEmpty) {
    if (returnsEmpty) {
      return API.List();
    } else {
      return selectList(mapper, cond);
    }
  }

  /**
   * 同 {@link #selectList(BaseMapper, Consumer)} 功能一样，区别在于 {@code returnsEmpty} 参数
   *
   * @param returnsEmpty 如果该 supplier 执行返回 true，将不做任何查询直接返回空列表
   */
  public static <T> List<T> selectList(
      BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> cond, Supplier<Boolean> returnsEmpty) {
    if (returnsEmpty.get()) {
      return API.List();
    } else {
      return selectList(mapper, cond);
    }
  }

  /**
   * @param mapper   MyBatis mapper
   * @param cond     定制查询条件
   * @param pageLink 分页信息
   * @param <T>      Map 的对象
   * @return 分页数据
   */
  @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
  public static <T> PageNumBasedPageData<T> selectPage(
      BaseMapper<T> mapper,
      Consumer<LambdaQueryWrapper<T>> cond,
      PageNumBasedPageLink pageLink,
      Supplier<Boolean> returnsEmpty) {
    if (returnsEmpty.get()) {
      return new PageNumBasedPageData<>(pageLink);
    }
    Page<T> page = pageNumBasedPageLinkToPageLink(pageLink);
    LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
    cond.accept(w);
    mapper.selectPage(page, w);
    List<T> data = List.ofAll(page.getRecords());
    long pages = page.getPages();
    long total = page.getTotal();
    return new PageNumBasedPageData<>(data, pageLink, pages, total);
  }

  public static <T> PageNumBasedPageData<T> selectPage(
      BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> cond, PageNumBasedPageLink pageLink) {
    return selectPage(mapper, cond, pageLink, () -> false);
  }

  /**
   * @param mapper   MyBatis mapper
   * @param cond     查询条件
   * @param pageLink 分页数据
   * @param <T>      Map 的对象
   * @return 分页数据
   */
  @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
  public static <T> PageNumBasedPageData<T> selectPage(
      BaseMapper<T> mapper, LambdaQueryWrapper<T> cond, PageNumBasedPageLink pageLink) {
    Page<T> page = pageNumBasedPageLinkToPageLink(pageLink);
    mapper.selectPage(page, cond);
    List<T> data = List.ofAll(page.getRecords());
    long pages = page.getPages();
    long total = page.getTotal();
    return new PageNumBasedPageData<T>(data, pageLink, pages, total);
  }

  /**
   * @param mapper MyBatis mapper
   * @param update 更新条件及更新数据
   * @param <T>    Map 的数据
   */
  public static <T> int update(BaseMapper<T> mapper, Consumer<LambdaUpdateWrapper<T>> update) {
    LambdaUpdateWrapper<T> w = new LambdaUpdateWrapper<>();
    update.accept(w);
    return mapper.update(null, w);
  }

  /**
   * @param mapper     MyBatis mapper
   * @param data       非空字段将用于更新
   * @param updateCond 用于构造更新条件
   * @param <T>        Map 的数据
   */
  public static <T> int update(
      BaseMapper<T> mapper, T data, Consumer<LambdaQueryWrapper<T>> updateCond) {
    LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
    updateCond.accept(w);
    return mapper.update(data, w);
  }

  /**
   * @param mapper     MyBatis mapper
   * @param removeCond 用于构造删除条件
   * @param <T>        Map 的数据
   */
  public static <T> int delete(BaseMapper<T> mapper, Consumer<LambdaQueryWrapper<T>> removeCond) {
    LambdaQueryWrapper<T> w = new LambdaQueryWrapper<>();
    removeCond.accept(w);
    return mapper.delete(w);
  }

  public static <T> Page<T> pageNumBasedPageLinkToPageLink(PageNumBasedPageLink pageLink) {
    int pageNum = pageLink.getPageNum();
    int pageSize = pageLink.getPageSize();
    SortOrder sortOrder = pageLink.getSortOrder();
    Page<T> page = new Page<>(pageNum + 1, pageSize);

    if (sortOrder != null) {
      OrderItem orderItem = new OrderItem();
      orderItem.setColumn(sortOrder.getProperty());
      orderItem.setAsc(sortOrder.getDirection() == Direction.ASC);

      page.setOrders(
          API.Seq(
              orderItem
          ).toJavaList());
    }
    
    return page;
  }
}
