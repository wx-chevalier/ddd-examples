package icu.ngte.udma.core.tools.random;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;
import org.jetbrains.annotations.Nullable;

public class RandomCodeUtils {

  private static final Random rand = new Random(System.currentTimeMillis());

  /** 注意此处移除1，i，I o o O等易混淆的符号 */
  private static final String VERIFY_CODES = "1234567890";

  public static String randomCode(int size) {
    int codesLen = VERIFY_CODES.length();
    StringBuilder verifyCode = new StringBuilder(size);
    for (int i = 0; i < size; i++) {
      verifyCode.append(VERIFY_CODES.charAt(rand.nextInt(codesLen - 1)));
    }
    return verifyCode.toString();
  }

  public static double randomRate() {
    // 生成随机比例
    double v = rand.nextDouble();
    // 随机正负号
    int prefixFlag = rand.nextInt() % 3 == 0 ? -1 : 1;
    BigDecimal b = BigDecimal.valueOf(prefixFlag * v);
    return b.setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  public static Long randomLong() {
    return rand.nextLong();
  }

  public static int randomInt(int bound) {
    return rand.nextInt(bound);
  }

  public static int randomInt() {
    return rand.nextInt();
  }

  public static Long randomLong(Integer start, Integer endExclusive) {
    return start + randomLong() % (endExclusive - start);
  }

  /**
   * 为指定Code添加校验位
   *
   * @apiNote 校验位和数据通过中划线分割
   * @apiNote 校验位计算公式(参考身份证号校验位算法): ∑((int)char * [index+1]) % 11
   */
  public static String appendCheckData(String randomStr) {
    if (randomStr == null || randomStr.length() == 0) {
      return "";
    }
    char[] chars = randomStr.toCharArray();
    int s = 0;
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      s += c * (i + 1);
    }

    String validateBitData = s % 11 == 10 ? "X" : String.valueOf(s % 11);
    return randomStr + "-" + validateBitData;
  }

  /**
   * @apiNote 支持校验格式为 20200409-476889-8 格式的单号
   * @apiNote 单号的校验位与数据位通过 - 分割
   */
  public static boolean validate(String code) {
    if (!hasText(code)) {
      return false;
    }
    String rawCode = code.substring(0, code.length() - 2);
    String checkCode = appendCheckData(rawCode);
    return Objects.equals(checkCode, code);
  }

  private static boolean hasText(@Nullable String str) {
    return (str != null && !str.isEmpty() && containsText(str));
  }

  private static boolean containsText(CharSequence str) {
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }
}
