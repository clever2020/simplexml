package com.linkoog.simpleframework.xml.core;
import java.io.StringWriter;
import java.util.Arrays;

import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.ElementListUnion;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.Serializer;
import junit.framework.TestCase;

import com.linkoog.simpleframework.xml.core.Test2Test.Test2.MyElementA;
import com.linkoog.simpleframework.xml.core.Test2Test.Test2.MyElementB;

public class Test2Test extends TestCase {

   @Root(name="test2")
   public static class Test2 {
      
      @ElementListUnion({
         @ElementList(name="elements", entry="element-a", type=MyElementA.class),
         @ElementList(name="elements", entry="element-b", type=MyElementB.class)
      })
      final java.util.ArrayList<MyElement> elements;
      
      public Test2(final MyElement... elements){
         this(new java.util.ArrayList<MyElement>(Arrays.asList(elements)));
      }
      
      //FIXME the exception is at least confusing. Where is the duplicate annotation of name elements?
      public Test2(  @ElementListUnion({
                     @ElementList(name="elements", entry="element-a", type=MyElementA.class),
                     @ElementList(name="elements", entry="element-b", type=MyElementB.class)
                  })
                  final java.util.ArrayList<MyElement> elements
            ) {
         super();
         this.elements = elements;
      }

      
      @Root
      public static class MyElement{
         
      }
      
      public static class MyElementA extends MyElement{
         
      }
      
      public static class MyElementB extends MyElement{
         
      }
      
   }

   public void testException() throws Exception{
      boolean failure = false;
      
      try {
         Serializer s = new Persister();
         StringWriter sw = new StringWriter();
         s.write(new Test2(new MyElementA(), new MyElementB()), sw);    
         String serializedForm = sw.toString();
         System.out.println(serializedForm);
         System.out.println();
         Test2 o = s.read(Test2.class, serializedForm);
         sw.getBuffer().setLength(0);
         s.write(o, sw);
         System.out.println(sw.toString());
         System.out.println();
      }catch(Exception e) {
         e.printStackTrace();
         failure = true;
      }
      assertTrue("Annotations are invalid as they use the same name", failure);
   }
}
