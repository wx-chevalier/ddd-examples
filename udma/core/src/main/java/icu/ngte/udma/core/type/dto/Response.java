package icu.ngte.udma.core.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import icu.ngte.udma.core.tools.lang.ExceptionUtils;
import icu.ngte.udma.core.type.pageable.PageNumBasedPageData;
import icu.ngte.udma.core.type.pageable.PageNumBasedPageLink;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Data
@JsonInclude(Include.NON_NULL)
public class Response<T> {

  public static <T> Response<T> ok() {
    return new Response<T>().setStatus(ResponseStatus.ok);
  }

  public static <T> Response<List<T>> paged(PageNumBasedPageData<T> pageData) {
    PageNumBasedPageLink pageLink = pageData.getPageLink();
    return ok(
        pageData.getData(),
        pageLink.getPageNum(),
        (long) pageData.getTotalElements(),
        pageData.getTotalPage());
  }

  public static <T> Response<T> ok(T data) {
    return new Response<T>().setStatus(ResponseStatus.ok).setData(data);
  }

  public static <T> Response<T> ok(T data, Pagination pagination) {
    return ok(data).setMeta(new ResponseMeta().setPagination(pagination));
  }

  public static <T> Response<T> ok(
      T data, Integer pageNum, Long totalElements, Integer totalPages) {
    return ok(data)
        .setMeta(
            new ResponseMeta().setPagination(new Pagination(pageNum, totalElements, totalPages)));
  }

  public static <T> Response<T> err(ErrorMessage err) {
    return new Response<T>().setStatus(ResponseStatus.error).setErr(err);
  }

  public static <T> Response<T> err(String code, String reason) {
    return new Response<T>().setStatus(ResponseStatus.error).setErr(new ErrorMessage(code, reason));
  }

  private ResponseStatus status;

  private ResponseMeta meta;

  private ErrorMessage err;

  private T data;

  public enum ResponseStatus {
    ok,
    error
  }

  @Data
  @JsonInclude(Include.NON_NULL)
  public static class ResponseMeta {

    private Pagination pagination;
  }

  @Data
  @NoArgsConstructor
  @JsonInclude(Include.NON_NULL)
  public static class Pagination {

    public static String PAGE_NUMBER = "pageNum";
    public static String PAGE_SIZE = "pageSize";
    public static String ID_OFFSET = "idOffset";
    public static String ASC = "asc";
    public static String ORDER_BY = "orderBy";

    private static final Pagination EMPTY = new Pagination(0, 0L, 0);

    public static Pagination empty() {
      return EMPTY;
    }

    public static Pagination singlePage(long totalElements) {
      return new Pagination(0, totalElements, 0);
    }

    public static Pagination singlePage(int totalElements) {
      return new Pagination(0, (long) totalElements, 0);
    }

    public Pagination(Integer pageNum, Long totalElements, Integer totalPages) {
      this.pageNum = pageNum;
      this.totalElements = totalElements;
      this.totalPages = totalPages;
    }

    public Pagination(String pageStart, String nextPageStart) {
      this.pageStart = pageStart;
      this.nextPageStart = nextPageStart;
    }

    private Integer pageNum;

    private Long totalElements;

    private Integer totalPages;

    private String pageStart;

    private String nextPageStart;
  }

  @Data
  @AllArgsConstructor
  public static class ErrorMessage {

    private String code;

    private String reason;

    @Nullable private String exception;

    public ErrorMessage(String code, String reason) {
      this.code = code;
      this.reason = reason;
      this.exception = null;
    }

    public ErrorMessage(String code, String reason, @Nullable Throwable t) {
      this.code = code;
      this.reason = reason;
      if (t != null) {
        this.exception = ExceptionUtils.getStackTrace(t);
      }
    }
  }
}
