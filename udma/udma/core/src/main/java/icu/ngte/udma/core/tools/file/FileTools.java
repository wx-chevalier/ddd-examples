package icu.ngte.udma.core.tools.file;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.io.BaseEncoding;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class FileTools {

  private static final int BUFFER_SIZE = 3 * 1024;

  public static File createTempFile(String suffix) {
    try {
      return Files.createTempFile("", suffix).toFile();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static File createTempFile() {
    try {
      return Files.createTempFile("", "").toFile();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static File createTempDir(String prefix) {
    try {
      return Files.createTempDirectory(prefix).toFile();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String base64EncodeFile(File inputFile) {
    try (BufferedInputStream in =
        new BufferedInputStream(new FileInputStream(inputFile), BUFFER_SIZE)) {
      BaseEncoding encoding = BaseEncoding.base64();
      StringBuilder result = new StringBuilder();
      byte[] chunk = new byte[BUFFER_SIZE];
      int len;
      while ((len = in.read(chunk)) == BUFFER_SIZE) {
        result.append(encoding.encode(chunk));
      }
      if (len > 0) {
        chunk = Arrays.copyOf(chunk, len);
        result.append(encoding.encode(chunk));
      }
      return result.toString();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String readToString(File file) {
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
      StringBuilder content = new StringBuilder();
      String line;
      while (true) {
        line = br.readLine();
        if (line == null) {
          break;
        } else {
          content.append(line).append('\n');
        }
      }
      return content.toString();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void ensureDir(File file) {
    if (file.exists()) {
      checkState(file.isDirectory(), "%s is not directory", file);
    } else {
      if (file.mkdirs()) {
        log.debug("mkdir {}", file);
      }
    }
  }

  @SuppressWarnings("UnstableApiUsage")
  public static void copyFile(File src, File dst) {
    try {
      com.google.common.io.Files.copy(src, dst);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void deleteFileIfExists(@Nullable File file) {
    if (file == null || !file.exists()) {
      return;
    }

    checkArgument(file.isFile(), "not a file: %s", file);
    if (file.exists() && file.delete()) {
      log.debug("Removed file {}", file);
    }
  }
}
