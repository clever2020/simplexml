package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementUnion;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.Serializer;
import junit.framework.TestCase;

public class Test4_2Test extends TestCase {

   @Root(name="test4")
   public static class Test4_2 {
      
      @ElementUnion({
            @Element(name="single-elementA", type=MyElementA.class),
            @Element(name="single-elementB", type=MyElementB.class),
            @Element(name="single-element", type=MyElement.class)
      })
      MyElement element;
      
      Test4_2(){
         
      }
      
      public Test4_2(final MyElement element) {
         super();
         this.element = element;
      }
      
   }

   
   @Root
   public static class MyElement{
      
   }
   
   public static class MyElementA extends MyElement{
      
   }
   
   public static class MyElementB extends MyElement{
      
   }
   
   public static class MyElementC extends MyElement{
      
   }

   public void testSerialization() throws Exception{
      Serializer s = new Persister();
      StringWriter sw = new StringWriter();     
            
      String serializedForm =    "<test4>\n" + 
                           "   <single-element class=\"Test4_2Test$MyElementC\"/>\n" +
                           "</test4>";
      System.out.println(serializedForm);
      System.out.println();
      
      Test4_2 o = s.read(Test4_2.class, serializedForm);
      
      //FIXME read ignores the class statement

      MyElementC ec = (MyElementC) o.element;
      
      sw.getBuffer().setLength(0);
      s.write(new Test4_2(new MyElementC()), sw);
      //FIXME it would be great, if this worked. Actually it works for ElementUnionLists.
      System.out.println(sw.toString());
      System.out.println();

   } 
}
