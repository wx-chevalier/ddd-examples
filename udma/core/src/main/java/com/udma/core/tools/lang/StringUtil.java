package com.udma.core.tools.lang;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.CollectionUtils;

public class StringUtil {
  private static MessageDigest digest = null;

  private StringUtil() throws InstantiationException {
    throw new InstantiationException("StringUtil不支持实例化");
  }

  /**
   * 替换字符串
   *
   * @apiNote 占位符请使用 ${xxx}
   * @param sourceText 原文本文件
   * @param param 替换的参数
   * @return 替换后的结果
   */
  public static String replaceAll(String sourceText, Map<String, String> param) {
    if (CollectionUtils.isEmpty(param)) {
      return sourceText;
    }
    Set<Map.Entry<String, String>> sets = param.entrySet();
    for (Map.Entry<String, String> entry : sets) {
      String regex = "\\$\\{" + entry.getKey() + "}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(sourceText);
      if (entry.getValue() != null) {
        sourceText = matcher.replaceAll(entry.getValue());
      }
    }
    return sourceText;
  }

  /** 将字节数组转换为十六进制字符串 */
  public static String byteToStr(byte[] byteArray) {
    StringBuilder strDigest = new StringBuilder();
    for (byte b : byteArray) {
      strDigest.append(byteToHexStr(b));
    }
    return strDigest.toString();
  }

  /** 将字节转换为十六进制字符串 */
  private static String byteToHexStr(byte mByte) {
    char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    char[] tempArr = new char[2];
    tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
    tempArr[1] = Digit[mByte & 0X0F];
    return new String(tempArr);
  }

  public static boolean containerLetter(CharSequence character) {
    if (character == null || character.length() == 0) {
      return false;
    }

    for (int i = 0; i < character.length(); i++) {
      char c = character.charAt(i);
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
        return true;
      }
    }
    return false;
  }
}
