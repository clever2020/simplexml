package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementUnion;
import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Path;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class UnionNamespaceTest extends ValidationTestCase {

   @Root
   private static class Example {
      @Path("path")
      @Namespace(prefix="x", reference="http://www.xml.com/ns")
      @ElementUnion({
         @Element(name="a"),
         @Element(name="b"),
         @Element(name="c")
      })
      private String a;
      
      @Path("path")
      @Namespace(prefix="x", reference="http://www.xml.com/ns")
      @ElementUnion({
         @Element(name="x"),
         @Element(name="y"),
         @Element(name="z")
      })
      private String x;
      
      public Example(
            @Element(name="b") String a, // TODO SCORING THE ADJUSTMENT FACTOR HERE NEEDS TOBE FIXED
            @Element(name="y") String x)
      {
         this.a = a;
         this.x = x;
      }
   }
   
   public void testNamespaceWithUnion() throws Exception{
      Persister persister = new Persister();
      Example example = new Example("A", "X");
      StringWriter writer = new StringWriter();
      persister.write(example, writer);
      String text = writer.toString();
      Example deserialized = persister.read(Example.class, text);
      assertEquals(deserialized.a, "A");
      assertEquals(deserialized.x, "X");
      validate(persister, example);
      assertElementExists(text, "/example/path/a");
      assertElementHasValue(text, "/example/path/a", "A");
      assertElementHasNamespace(text, "/example/path/a", "http://www.xml.com/ns");
   }
}
