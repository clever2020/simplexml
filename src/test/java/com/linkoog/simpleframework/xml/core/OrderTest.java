package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Order;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class OrderTest extends ValidationTestCase {

   @Root
   @Order(elements={"first", "second", "third", "fourth"}, attributes={"one", "two", "three"})
   private static class OrderExample {
      
      @Attribute
      private int one;
      
      @Attribute
      private double two;
      
      @Attribute
      private long three;
      
      @Element
      private String first;
      
      @Element
      private String fourth;
      
      @Element
      private String second;
      
      @Element
      private String third;
      
      public OrderExample() {
         super();
      }
      
      public OrderExample(String first, String second, String third, String fourth, long one, int two, double three) {
         this.first = first;
         this.second = second;
         this.third = third;
         this.fourth = fourth;
         this.three = one;
         this.one = two;
         this.two = three;
      }
   }
   
   public void testLinkedHashMapOrder() {
      Map map = new LinkedHashMap();
      
      map.put("first", null);
      map.put("second", null);
      map.put("third", null);
      map.put("fourth", null);
      
      map.put("third", "third");
      map.put("fourth", "fourth");
      map.put("first", "first");
      map.put("second", "second");
      
      Iterator values = map.values().iterator();
      
      assertEquals("first", values.next());
      assertEquals("second", values.next());
      assertEquals("third", values.next());
      assertEquals("fourth", values.next());
   }
   
   public void testSerializationOrder() throws Exception {
      XmlMapper xmlMapper = new Persister();
      OrderExample example = new OrderExample("first", "second", "third", "fourth", 1, 2, 3.0);
      StringWriter writer = new StringWriter();
      xmlMapper.write(example, writer);
      validate(example, xmlMapper);
      String text = writer.toString();
      
      assertTrue(text.indexOf("first") < text.indexOf("second"));
      assertTrue(text.indexOf("second") < text.indexOf("third"));
      assertTrue(text.indexOf("third") < text.indexOf("fourth"));
      assertTrue(text.indexOf("one") < text.indexOf("two"));
      assertTrue(text.indexOf("two") < text.indexOf("three"));
      
      example = new OrderExample("1st", "2nd", "3rd", "4th", 10, 20, 30.0);           
      validate(example, xmlMapper);
   }
}
