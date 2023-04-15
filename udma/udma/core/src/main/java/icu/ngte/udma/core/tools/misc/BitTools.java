package icu.ngte.udma.core.tools.misc;

import java.util.HashSet;
import java.util.Set;

public class BitTools {

  public static String convertToBitString(Set<String> strings, String[] strArray) {
    StringBuilder sb = new StringBuilder();
    for (String s : strArray) {
      if (strings.contains(s)) {
        sb.append('1');
      } else {
        sb.append('0');
      }
    }
    return sb.toString();
  }

  public static Set<String> convertFromBitString(String bitString, String[] strArray) {
    Set<String> res = new HashSet<>();
    int i = 0;
    for (char ch : bitString.toCharArray()) {
      if (ch == '1') {
        res.add(strArray[i]);
      }
      i++;
    }
    return res;
  }
}
