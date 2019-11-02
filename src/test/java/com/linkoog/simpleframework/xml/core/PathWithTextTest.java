package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Path;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.annotations.Text;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class PathWithTextTest extends ValidationTestCase {
   
   @Root
   private static class PathWithTextExample {
      @Attribute
      @Path("some/path")
      private String value;
      @Text
      private String text;
      public void setValue(String value) {
         this.value = value;
      }
      public void setText(String text) {
         this.text = text;
      }
   }
   
   public void testTextWithPath() throws Exception {
      Persister persister = new Persister();
      PathWithTextExample example = new PathWithTextExample();
      example.setText("Some Text");
      example.setValue("Some Value");
      boolean exception = false;
      try {
         persister.write(example, System.out);
      }catch(Exception e) {
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Text should not appear with paths", exception);
   }

}
