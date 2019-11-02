package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Root;

public class AnonymousClassTest extends ValidationTestCase {
   
   @Root(name="anonymous")
   private static class Anonymous {
      
      @Element
      @Namespace(prefix="prefix", reference="http://www.domain.com/reference")
      private static Object anonymous = new Object() {
         @Attribute(name="attribute")
         private static final String attribute = "example attribute"; 
         @Element(name="element")
         private static final String element = "example element";      
      };
   }
   /*
    TODO fix this test
   public void testAnonymousClass() throws Exception {
      Persister persister = new Persister();
      Anonymous anonymous = new Anonymous();
      
      validate(persister, anonymous);
   }
   */
   
   public void testA() {}

}
