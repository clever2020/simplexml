package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementUnion;
import com.linkoog.simpleframework.xml.annotations.Root;
import junit.framework.TestCase;

public class UnionParameterTest extends TestCase {

   private static final String WITH_NAME = 
   "<parameterExample>"+
   "   <int>12</int>"+
   "   <name>x</name>"+
   "</parameterExample>";
   
   private static final String WITHOUT_NAME = 
   "<parameterExample>"+
   "   <double>4.0</double>"+
   "</parameterExample>";
   
   @Root
   public static class ParameterExample {
      
      @ElementUnion({
         @Element(name="int", type=Integer.class),
         @Element(name="double", type=Double.class),
         @Element(name="string", type=String.class)
      })
      private Object value;
      
      @Element(name="name", required=false)
      private String name;
      
      public ParameterExample(
            @ElementUnion({
               @Element(name="int", type=Integer.class),
               @Element(name="double", type=Double.class),
               @Element(name="string", type=String.class)
            })
            Object value)
      {
         this.value = value;
         this.name = "[NOT SET]";
      }
      
      public ParameterExample(
            @Element(name="name")
            String name,
            
            @ElementUnion({
               @Element(name="int", type=Integer.class),
               @Element(name="double", type=Double.class),
               @Element(name="string", type=String.class)
            })
            Object value)
      {
         this.value = value;
         this.name = name;
      }
      
      public String getName() {
         return name;
      }
      
      public Object getValue() {
         return value;
      }
   }
   
   
   public void testParameter() throws Exception {
      Persister persister = new Persister();
      ParameterExample example = persister.read(ParameterExample.class, WITH_NAME);
      assertEquals(example.getValue(), 12);
      assertEquals(example.getName(), "x");
      example = persister.read(ParameterExample.class, WITHOUT_NAME);
      assertEquals(example.getValue(), 4.0);
      assertEquals(example.getName(), "[NOT SET]");
   }
}
