package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class NonFinalConstructorInjectionTest extends ValidationTestCase {
   
   @Root
   private static class NonFinalExample {
      @Element
      private String name;
      @Element
      private String value;
      public NonFinalExample(@Element(name="name") String name, @Element(name="value") String value) {
         this.name = name;
         this.value = value;
      }
      public String getName(){
         return name;
      }
      public String getValue(){
         return value;
      }
   }
   
   public void testNonFinal() throws Exception {
      Persister persister = new Persister();
      NonFinalExample example = new NonFinalExample("A", "a");
      
      validate(example, persister);
   }
}
