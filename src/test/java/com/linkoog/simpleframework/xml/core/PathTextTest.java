package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Path;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.annotations.Text;
import com.linkoog.simpleframework.xml.stream.CamelCaseStyle;
import com.linkoog.simpleframework.xml.stream.Format;
import com.linkoog.simpleframework.xml.stream.Style;
import junit.framework.TestCase;

public class PathTextTest extends TestCase {

   @Root
   public static class TextExample {
      @Path("path[1]/details")
      @Text
      private final String a;
      @Path("path[2]/details")
      @Text
      private final String b;
      public TextExample(
            @Path("path[1]/details") @Text String x, 
            @Path("path[2]/details") @Text String z) {
         this.a = x;
         this.b = z;
      }
   }
   
   public void testStyleDup() throws Exception {
      Style style = new CamelCaseStyle();
      Format format = new Format(style);
      TextExample example = new TextExample("a", "b");
      Persister persister = new Persister(format);
      StringWriter writer = new StringWriter();
      persister.write(example, writer);
      System.out.println(writer);
      TextExample restored = persister.read(TextExample.class, writer.toString());
      assertEquals(example.a, restored.a);
      assertEquals(example.b, restored.b);
   } 

}
