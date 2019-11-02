package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.annotations.Order;
import com.linkoog.simpleframework.xml.annotations.Path;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class PathAttributeWithNoExistingSectionTest extends ValidationTestCase {
   
   @Order(attributes={"some/path/to/build/@value"})
   @Default
   private static class AttributeWithNoExistingSectionExample {
      @Attribute
      @Path("some/path/to/build")
      private String value;
      public void setValue(String value){
         this.value = value;
      }
   }
   
   public void testOrder() throws Exception {
      Persister persister = new Persister();
      AttributeWithNoExistingSectionExample example = new AttributeWithNoExistingSectionExample();
      example.setValue("Attribute value");
      persister.write(example, System.err);
      validate(example, persister);
      
   }

}
