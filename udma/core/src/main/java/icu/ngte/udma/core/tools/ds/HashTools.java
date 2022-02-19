package icu.ngte.udma.core.tools.ds;

import com.google.common.io.BaseEncoding;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashTools {

  /** @return hex string with lower case */
  public static String sha256(File file) {
    try (InputStream ins = new BufferedInputStream(new FileInputStream(file))) {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] buf = new byte[1024];
      int nRead = 0;
      while ((nRead = ins.read(buf)) != -1) {
        md.update(buf, 0, nRead);
      }
      return BaseEncoding.base16().encode(md.digest()).toLowerCase();
    } catch (IOException | NoSuchAlgorithmException e) {
      throw new UncheckedExecutionException(e);
    }
  }

  /** @return hex string with lower case */
  public static String md5(File file) {
    try (InputStream ins = new BufferedInputStream(new FileInputStream(file))) {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] buf = new byte[1024];
      int nRead = 0;
      while ((nRead = ins.read(buf)) != -1) {
        md.update(buf, 0, nRead);
      }
      return BaseEncoding.base16().encode(md.digest()).toLowerCase();
    } catch (IOException | NoSuchAlgorithmException e) {
      throw new UncheckedExecutionException(e);
    }
  }

  /** @return hex string with lower case */
  public static String sha256(String s) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      return BaseEncoding.base16()
          .encode(md.digest(s.getBytes(StandardCharsets.UTF_8)))
          .toLowerCase();
    } catch (NoSuchAlgorithmException e) {
      throw new UncheckedExecutionException(e);
    }
  }

  /** @return hex string with lower case */
  public static String md5(String s) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return BaseEncoding.base16()
          .encode(md.digest(s.getBytes(StandardCharsets.UTF_8)))
          .toLowerCase();
    } catch (NoSuchAlgorithmException e) {
      throw new UncheckedExecutionException(e);
    }
  }
}
