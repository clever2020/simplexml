package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.ElementListUnion;
import com.linkoog.simpleframework.xml.annotations.ElementMap;
import com.linkoog.simpleframework.xml.annotations.ElementMapUnion;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class UnionEmptyListBugTest extends ValidationTestCase {

   @Root
   public static class ElementListUnionBug {
      @ElementListUnion( {
            @ElementList(entry="string", inline=true, type=String.class, required=false),
            @ElementList(entry="integer", inline=true, type=Integer.class, required=false) 
      })
      List<Object> values = new ArrayList<Object>();
      
      @ElementList(entry="X", inline=true, required=false)
      List<String> list = new ArrayList<String>();
   }
   
   @Root
   public static class ElementMapUnionBug {
      @ElementMapUnion( {
            @ElementMap(entry="string", inline=true, keyType=String.class, valueType=String.class, required=false),
            @ElementMap(entry="integer", inline=true, keyType=String.class, valueType=Integer.class, required=false)
      })
      Map<String, Object> values = new HashMap<String, Object>();
      
      @ElementList(entry="X", inline=true, required=false)
      List<String> list = new ArrayList<String>();
   }
   
   public void testListBug() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ElementListUnionBug element = new ElementListUnionBug();
      StringWriter writer = new StringWriter();
      xmlMapper.write(element, writer);
      String text = writer.toString();
      assertElementExists(text, "/elementListUnionBug");
      assertElementDoesNotExist(text, "/elementListUnionBug/string");
      assertElementDoesNotExist(text, "/elementListUnionBug/integer");
      writer = new StringWriter();
      element.values.add("A");
      element.values.add(111);    
      xmlMapper.write(element, writer);
      text = writer.toString();
      System.out.println(text);
      assertElementExists(text, "/elementListUnionBug/string");
      assertElementHasValue(text, "/elementListUnionBug/string", "A");
      assertElementExists(text, "/elementListUnionBug/integer");
      assertElementHasValue(text, "/elementListUnionBug/integer", "111");
   }
   
   public void testMapBug() throws Exception {
      XmlMapper xmlMapper = new Persister();
      ElementMapUnionBug element = new ElementMapUnionBug();
      StringWriter writer = new StringWriter();
      xmlMapper.write(element, writer);
      String text = writer.toString();
      assertElementExists(text, "/elementMapUnionBug");
      assertElementDoesNotExist(text, "/elementMapUnionBug/string");
      assertElementDoesNotExist(text, "/elementMapUnionBug/integer");
      writer = new StringWriter();
      writer = new StringWriter();
      element.values.put("A", "string");
      element.values.put("B", 1);    
      xmlMapper.write(element, writer);
      text = writer.toString();
      System.out.println(text);
      assertElementExists(text, "/elementMapUnionBug/string");
      assertElementHasValue(text, "/elementMapUnionBug/string/string[1]", "A");
      assertElementHasValue(text, "/elementMapUnionBug/string/string[2]", "string");
      assertElementExists(text, "/elementMapUnionBug/integer");
      assertElementHasValue(text, "/elementMapUnionBug/integer/string", "B");
      assertElementHasValue(text, "/elementMapUnionBug/integer/integer", "1");
   }

}
