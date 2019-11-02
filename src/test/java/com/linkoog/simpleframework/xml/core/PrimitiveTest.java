package com.linkoog.simpleframework.xml.core;

import java.io.StringReader;

import com.linkoog.simpleframework.xml.strategy.CycleStrategy;
import com.linkoog.simpleframework.xml.strategy.TreeStrategy;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.NodeBuilder;
import junit.framework.TestCase;

public class PrimitiveTest extends TestCase {
   
   public static final String SOURCE =
   "<value class='java.lang.String'>some text</value>";
   
   public static final String CYCLE_1 =
   "<value id='1' class='java.lang.String'>some text</value>";

   public static final String CYCLE_2 =
   "<value id='2' class='java.lang.String'>some text</value>";
   
   public void testPrimitive() throws Exception {
      Context context = new Source(new TreeStrategy(), new Support(), new Session());
      Primitive primitive = new Primitive(context, new ClassType(String.class));
      InputNode node = NodeBuilder.read(new StringReader(SOURCE));
      Object value = primitive.read(node);
      
      assertEquals("some text", value);
      
      InputNode newNode = NodeBuilder.read(new StringReader(SOURCE));
      
      assertTrue(primitive.validate(newNode)); 
   }
   
   public void testPrimitiveCycle() throws Exception {
      Context context = new Source(new CycleStrategy(), new Support(), new Session());
      Primitive primitive = new Primitive(context, new ClassType(String.class));
      InputNode node = NodeBuilder.read(new StringReader(CYCLE_1));
      Object value = primitive.read(node);
      
      assertEquals("some text", value);
      
      // Need to use a different id for validate as reading has created the object
      // and an exception is thrown that the value already exists if id=1 is used 
      InputNode newNode = NodeBuilder.read(new StringReader(CYCLE_2));
      
      assertTrue(primitive.validate(newNode)); 
   }
}
