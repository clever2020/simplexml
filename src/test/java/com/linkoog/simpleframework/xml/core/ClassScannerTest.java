package com.linkoog.simpleframework.xml.core;

import junit.framework.TestCase;

import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Order;
import com.linkoog.simpleframework.xml.annotations.Root;

public class ClassScannerTest extends TestCase {
   
   @Root
   @Order(elements={"a", "b"}, attributes={"A", "B"})
   @Namespace(prefix="prefix", reference="http://domain/reference")
   private static class Example {
      
      @Commit
      public void commit() {
         return;
      }
      
      @Validate
      public void validate() {
         return;
      }
   }

   public void testClassScanner() throws Exception {
      Support support = new Support();
      Detail detail = new DetailScanner(Example.class);
      ClassScanner scanner = new ClassScanner(detail, support);
      
      assertNotNull(scanner.getRoot());
      assertNotNull(scanner.getOrder());
   }
}
