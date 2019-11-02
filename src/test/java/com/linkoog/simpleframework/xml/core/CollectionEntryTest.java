package com.linkoog.simpleframework.xml.core;

import java.util.List;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class CollectionEntryTest extends ValidationTestCase {
        
   private static final String LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<exampleCollection>\n"+
   "   <list>\n"+   
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
   "</exampleCollection>";
   
   private static final String INLINE_LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<exampleInlineCollection>\n"+
   "   <substitute id='1'>\n"+
   "      <text>one</text>  \n\r"+
   "   </substitute>\n\r"+
   "   <substitute id='2'>\n"+
   "      <text>two</text>  \n\r"+
   "   </substitute>\n"+
   "   <substitute id='3'>\n"+
   "      <text>three</text>  \n\r"+
   "   </substitute>\n"+
   "</exampleInlineCollection>";  
   
   private static final String INLINE_PRIMITIVE_LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<examplePrimitiveCollection>\n"+
   "   <substitute>a</substitute>\n"+
   "   <substitute>b</substitute>\n"+
   "   <substitute>c</substitute>\n"+
   "   <substitute>d</substitute>\n"+
   "</examplePrimitiveCollection>"; 
   
   private static final String PRIMITIVE_LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<examplePrimitiveCollection>\n"+
   "  <list>\r\n" +
   "     <substitute>a</substitute>\n"+
   "     <substitute>b</substitute>\n"+
   "     <substitute>c</substitute>\n"+
   "     <substitute>d</substitute>\n"+
   "  </list>\r\n" +
   "</examplePrimitiveCollection>"; 
   
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
   private static class ExampleCollection {
      
      @ElementList(name="list", entry="substitute")
      private List<Entry> list;
      
      public List<Entry> getList() {
         return list;
      }
   }
   
   @Root
   private static class ExampleInlineCollection {
      
      @ElementList(name="list", entry="substitute", inline=true)
      private List<Entry> list;
      
      public List<Entry> getList() {
         return list;
      }
   }
   
   @Root
   private static class ExamplePrimitiveCollection {
      
      @ElementList(name="list", entry="substitute")
      private List<Character> list;
      
      public List<Character> getList() {
         return list;
      }
   }
   
   @Root
   private static class ExamplePrimitiveInlineCollection {
      
      @ElementList(name="list", entry="substitute", inline=true)
      private List<String> list;
      
      public List<String> getList() {
         return list;
      }
   }
   
   public void testExampleCollection() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ExampleCollection list = xmlMapper.read(ExampleCollection.class, LIST);
      
      validate(list, xmlMapper);
   }  
   
   public void testExampleInlineCollection() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ExampleInlineCollection list = xmlMapper.read(ExampleInlineCollection.class, INLINE_LIST);
      
      validate(list, xmlMapper);
   }  
   
   public void testExamplePrimitiveInlineCollection() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ExamplePrimitiveInlineCollection list = xmlMapper.read(ExamplePrimitiveInlineCollection.class, INLINE_PRIMITIVE_LIST);
      
      validate(list, xmlMapper);
   }
   
   public void testExamplePrimitiveCollection() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ExamplePrimitiveCollection list = xmlMapper.read(ExamplePrimitiveCollection.class, PRIMITIVE_LIST);
      
      validate(list, xmlMapper);
   }
}
