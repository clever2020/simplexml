package com.linkoog.simpleframework.xml.core;

import java.util.List;
import java.util.Map;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementArray;
import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.ElementMap;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class DefaultEmptyTest extends ValidationTestCase {
   
   private static final String SOURCE = 
   "<defaultExample name='test'>\n" +
   "  <text>some text</text>\n"+
   "</defaultExample>";
   
   @Root
   private static class DefaultExample  {
      
      @ElementList(empty=false, required=false)
      private List<String> stringList;
      
      @ElementMap(empty=false, required=false)
      private Map<String, String> stringMap;
      
      @ElementArray(empty=false, required=false)
      private String[] stringArray;
      
      @Attribute
      private String name;
      
      @Element
      private String text;
      
      public DefaultExample() {
         super();
      }
      
      public DefaultExample(String name, String text) {
         this.name = name;
         this.text = text;
      }
   }
   
   public void testDefaults() throws Exception {
      Persister persister = new Persister();
      DefaultExample example = persister.read(DefaultExample.class, SOURCE);
    
      assertEquals(example.name, "test");
      assertEquals(example.text, "some text");
      assertNotNull(example.stringList);
      assertNotNull(example.stringMap);
      assertNotNull(example.stringArray);
      
      persister.write(example, System.out);
      
      validate(persister, example);
      
      persister.write(new DefaultExample("name", "example text"), System.out);
   }

}
