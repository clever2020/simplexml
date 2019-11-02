package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementArray;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;

public class ArrayEntryTest extends ValidationTestCase {
        
   private static final String LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<exampleArray>\n"+
   "   <list length='3'>\n"+   
   "      <substitute id='1'>\n"+
   "         <text>one</text>  \n\r"+
   "      </substitute>\n\r"+
   "      <substitute id='2'>\n"+
   "         <text>two</text>  \n\r"+
   "      </substitute>\n"+
   "      <substitute id='3'>\n"+
   "         <text>three</text>  \n\r"+
   "      </substitute>\n"+
   "   </list>\n"+
   "</exampleArray>";
   
   private static final String PRIMITIVE_LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<examplePrimitiveArray>\n"+
   "  <list length='4'>\r\n" +
   "     <substitute>a</substitute>\n"+
   "     <substitute>b</substitute>\n"+
   "     <substitute>c</substitute>\n"+
   "     <substitute>d</substitute>\n"+
   "  </list>\r\n" +
   "</examplePrimitiveArray>"; 
   
   @Root
   private static class Entry {

      @Attribute           
      private int id;           
           
      @Element
      private String text;        

      public String getText() {
         return text;
      }
      
      public int getId() {
         return id;
      }
   }
   
   @Root
   private static class ExampleArray {
      
      @ElementArray(name="list", entry="substitute") // XXX bad error if the length= attribute is missing
      private Entry[] list;
      
      public Entry[] getArray() {
         return list;
      }
   }
   
   @Root
   private static class ExamplePrimitiveArray {
      
      @ElementArray(name="list", entry="substitute") // XXX bad error if this was an array
      private Character[] list;
      
      public Character[] getArray() {
         return list;
      }
   }

   
   public void testExampleArray() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ExampleArray list = xmlMapper.read(ExampleArray.class, LIST);
      
      validate(list, xmlMapper);
   }  
   
   public void testExamplePrimitiveArray() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ExamplePrimitiveArray list = xmlMapper.read(ExamplePrimitiveArray.class, PRIMITIVE_LIST);
      
      validate(list, xmlMapper);
   }
}
