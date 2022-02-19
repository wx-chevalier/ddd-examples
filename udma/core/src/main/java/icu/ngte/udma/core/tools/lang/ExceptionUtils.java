package icu.ngte.udma.core.tools.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

  public static String getStackTrace(Throwable throwable) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    throwable.printStackTrace(pw);
    return sw.getBuffer().toString();
  }
}
