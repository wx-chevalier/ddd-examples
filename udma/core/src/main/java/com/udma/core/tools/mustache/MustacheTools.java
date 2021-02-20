package com.udma.core.tools.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.vavr.API;
import io.vavr.collection.Map;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

// https://mustache.github.io/
// https://github.com/spullara/mustache.java
public class MustacheTools {
  private static final MustacheFactory mf = new DefaultMustacheFactory();

  public static String mustacheTemplateFile(String templateFile, Object scope) {
    Mustache mustache = mf.compile(templateFile);
    StringWriter stringWriter = new StringWriter();
    mustache.execute(stringWriter, scope);
    return stringWriter.toString();
  }

  public static String mustacheTemplateFile(String templateFile, Map<String, String> scopes) {
    return mustacheTemplateFile(templateFile, scopes.toJavaMap());
  }

  public static String mustache(String template, Object scope) {
    Mustache mustache = mf.compile(new StringReader(template), UUID.randomUUID().toString());
    StringWriter stringWriter = new StringWriter();
    mustache.execute(stringWriter, scope);
    return stringWriter.toString();
  }

  public static String mustache(String template, Map<String, String> scopes) {
    return mustache(template, scopes.toJavaMap());
  }

  public static void main(String[] args) throws IOException {
    System.out.println(
        mustache(
            "{{name}}, {{description}}",
            API.Map(
                "name", "lotuc",
                "description", "programmer")));
    System.out.println(
        mustacheTemplateFile(
            "abc.mustache",
            API.Map(
                "hello", "damn",
                "world", "room",
                "he", "shitter",
                "it", "nobody")));
  }
}
