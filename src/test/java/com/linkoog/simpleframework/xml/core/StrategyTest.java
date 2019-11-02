package com.linkoog.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.Serializer;
import com.linkoog.simpleframework.xml.strategy.Strategy;
import com.linkoog.simpleframework.xml.strategy.Type;
import com.linkoog.simpleframework.xml.strategy.Value;
import com.linkoog.simpleframework.xml.stream.Node;
import com.linkoog.simpleframework.xml.stream.NodeMap;
import junit.framework.TestCase;

public class StrategyTest extends TestCase {

   private static final String ELEMENT_NAME = "example-attribute";        

   private static final String ELEMENT =
   "<?xml version=\"1.0\"?>\n"+
   "<root key='attribute-example-key' example-attribute='StrategyTest$ExampleExample'>\n"+
   "   <text>attribute-example-text</text>  \n\r"+
   "</root>";

   @Root(name="root")
   private static abstract class Example {

      public abstract String getValue();   
      
      public abstract String getKey();
   }
   
   private static class ExampleExample extends Example {

      @Attribute(name="key")
      public String key;           
           
      @Element(name="text")
      public String text;           

      public String getValue() {
         return text;              
      }
      
      public String getKey() {
         return key;
      }
   }

   public class ExampleStrategy implements Strategy {
      
      private StrategyTest test;

      public ExampleStrategy(StrategyTest test){
         this.test = test;              
      }

      public Value read(Type field, NodeMap node, Map map) throws Exception {
         Node value = node.remove(ELEMENT_NAME);
         
         if(value == null) {
        	 return null;
         }
         String name = value.getValue();
         Class type = Class.forName(name);
         
         return new SimpleType(type);
      }                      

      public boolean write(Type field, Object value, NodeMap node, Map map) throws Exception {            
         if(field.getType() != value.getClass()) {                       
            node.put(ELEMENT_NAME, value.getClass().getName());
         }  
         return false;
      }
   }
   
   public static class SimpleType implements Value{
	   
	   private Class type;
	   
	   public SimpleType(Class type) {
		   this.type = type;
	   }
	   
	   public int getLength() {
	      return 0;
	   }
	   
      public Object getValue()  {
         try {    
   		   Constructor method = type.getDeclaredConstructor();
   
   		   if(!method.isAccessible()) {
   		      method.setAccessible(true);              
   		   }
   		   return method.newInstance();
         }catch(Exception e) {
            throw new RuntimeException(e);
         }
	   }

      public void setValue(Object value) {
      }
      
       public boolean isReference() {
          return false;
       }
       

	   public Class getType() {
		  return type;
	   } 
   }

   public void testExampleStrategy() throws Exception {    
      ExampleStrategy strategy = new ExampleStrategy(this);           
      Serializer persister = new Persister(strategy);
      Example example = persister.read(Example.class, ELEMENT);
      
      assertTrue(example instanceof ExampleExample);
      assertEquals(example.getValue(), "attribute-example-text");
      assertEquals(example.getKey(), "attribute-example-key");
      
      persister.write(example, System.err);
   }
}
