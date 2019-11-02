package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Path;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import junit.framework.TestCase;

public class PathConstructorAmbiguityTest extends TestCase {

   @Root(name="test1")
   public static class Test1 {
      
      @Attribute(name="iri")
      final String identifier;
      
      @Path(value="resource")
      @Attribute(name="iri")
      final String identifier1;
      
      public Test1(
            @Attribute(name="iri")  String identifier,
            @Path(value="resource") @Attribute(name="iri") String identifier1)
      {
         this.identifier = identifier;
         this.identifier1 = identifier1;
      }   
   }

      public void testParameters() throws Exception{
         XmlMapper s = new Persister();
         StringWriter sw = new StringWriter();
         s.write(new Test1("a", "b"), sw);      
         String serializedForm = sw.toString();
         System.out.println(serializedForm);
         Test1 o = s.read(Test1.class, serializedForm);
      }
}
