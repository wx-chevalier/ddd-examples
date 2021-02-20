package com.udma.core.tools.lang;

import static com.google.common.base.Preconditions.checkArgument;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class FileUtils {

  private static final List<String> PICTURE_FORMAT =
      Arrays.asList("PNG", "JPG", "BMP", "GIF", "JPEG", "SVG", "WEBP", "TIF");

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
  public static void deleteFile(File file) {
    file.delete();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
  public static void mkdirs(File dir) {
    dir.mkdirs();
  }

  public static File ensureDir(File file) {
    if (file.exists()) {
      checkArgument(file.isDirectory(), "file %s is not dir", file);
    } else {
      mkdirs(file);
    }
    return file;
  }

  @SuppressWarnings({"UnstableApiUsage", "deprecation", "StatementWithEmptyBody"})
  public static String md5(File file) {
    try {
      return DigestUtils.md5Hex(new FileInputStream(file.getAbsolutePath()));
    } catch (IOException e) {
      log.error("计算文件MD5出现异常", e);
      throw new RuntimeException(e);
    }
  }

  /** 获取文件的拓展名(小写) */
  public static String getExtension(String fileName) {
    String extension = null;
    int i = fileName.lastIndexOf('.');
    if (i > 0) {
      extension = fileName.substring(i + 1).toLowerCase();
    }
    return extension;
  }

  /** 判断文件是否是图片类型 */
  public static boolean isPicture(String fileName) {
    if (!StringUtils.hasText(fileName)) {
      return false;
    }
    String extension = getExtension(fileName);
    if (!StringUtils.hasText(extension)) {
      return false;
    }
    return PICTURE_FORMAT.contains(extension.toUpperCase());
  }
}
