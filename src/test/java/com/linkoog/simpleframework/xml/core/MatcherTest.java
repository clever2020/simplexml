package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.transform.matcher.Matcher;
import com.linkoog.simpleframework.xml.transform.Transform;

public class MatcherTest extends ValidationTestCase {
   
   @Root
   @Namespace(prefix="foo", reference="http://www.domain.com/value")
   private static class Example {
      
      @Element
      private Integer value;
      
      @Attribute
      private Integer attr;
      
      public Example() {
         super();
      }
      
      public Example(Integer value, Integer attr) {
         this.value = value;
         this.attr = attr;
      }
   }
   
   @Root
   private static class EmptyStringExample {
      
      @Element(required=false)
      private String emptyValue;
      
      @Element(required=false)
      private String nullValue;
      
      public EmptyStringExample() {
         super();
      }
      
      public EmptyStringExample(String emptyValue, String nullValue) {
         this.emptyValue = emptyValue;
         this.nullValue = nullValue;
      }
   }
   
   @Root
   private static class ExampleEnum {
      
      @Attribute
      private MyEnum value;
      
      public ExampleEnum(@Attribute(name="value") MyEnum value) {
         this.value = value;
      }
   }
   
   private static class ExampleIntegerMatcher implements Matcher, Transform<Integer> {

      public Transform match(Class type) throws Exception {
         if(type == Integer.class) {
            return this;
         }
         return null;
      }

      public Integer read(String value) throws Exception {
         return Integer.valueOf(value);
      }

      public String write(Integer value) throws Exception {
         return "12345";
      }
   }
   
   private static class ExampleStringMatcher implements Matcher, Transform<String> {

      public Transform match(Class type) throws Exception {
         if(type == String.class) {
            return this;
         }
         return null;
      }

      public String read(String value) throws Exception {         
         if(value != null) {
            if(value.equals("[[NULL]]")) {
               return null;
            }
            if(value.equals("[[EMPTY]]")) {
               return "";
            }
         }
         return value;
      }

      public String write(String value) throws Exception {
         if(value == null) {
            return "[[NULL]]";
         }
         if(value.equals("")) {
            return "[[EMPTY]]";
         }
         return value;
      }
   }
   
   private static enum MyEnum {
      A_1,
      B_2,
      C_3,
   }
   
   private static class ExampleEnumMatcher implements Matcher, Transform<MyEnum> {

      public Transform match(Class type) throws Exception {
         if(type == MyEnum.class) {
            return this;
         }
         return null;
      }

      public MyEnum read(String value) throws Exception {
         return Enum.valueOf(MyEnum.class, value);
      }

      public String write(MyEnum value) throws Exception {
         return value.name().replace('_', '-');
      }
      
   }
   
   public void testMatcher() throws Exception {
      Matcher matcher = new ExampleIntegerMatcher();
      XmlMapper xmlMapper = new Persister(matcher);
      Example example = new Example(1, 9999);
      
      xmlMapper.write(example, System.out);
    
      validate(xmlMapper, example);
   }
   
   public void testEnumMatcher() throws Exception {
      Matcher matcher = new ExampleEnumMatcher();
      XmlMapper xmlMapper = new Persister(matcher);
      ExampleEnum value = new ExampleEnum(MyEnum.A_1);
      StringWriter writer = new StringWriter();
      
      xmlMapper.write(value, writer);
      
      assertElementHasAttribute(writer.toString(), "/exampleEnum", "value", "A-1");
      
      System.out.println(writer.toString());
      
      validate(xmlMapper, value);
   }
   
   public void testStringMatcher() throws Exception {
      Matcher matcher = new ExampleStringMatcher();
      XmlMapper xmlMapper = new Persister(matcher);
      EmptyStringExample original = new EmptyStringExample("", null);
      StringWriter writer = new StringWriter();
      
      xmlMapper.write(original, writer);
      
      String text = writer.toString();
      System.out.println(text);
      
      EmptyStringExample recovered = xmlMapper.read(EmptyStringExample.class, text);
      
      assertEquals(recovered.emptyValue, original.emptyValue);
      assertEquals(recovered.nullValue, original.nullValue);
   }

}
