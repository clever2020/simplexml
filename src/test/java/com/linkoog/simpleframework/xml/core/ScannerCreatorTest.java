package com.linkoog.simpleframework.xml.core;

import java.util.List;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Path;
import junit.framework.TestCase;

@SuppressWarnings("all")
public class ScannerCreatorTest extends TestCase {

   private static class Example1{
      @Path("path")
      @Element(name="a")
      private String el;
      @Attribute(name="a")
      private String attr;
      public Example1(
            @Element(name="a") String el, 
            @Attribute(name="a") String attr) {}
   }
   
   public void testScanner() throws Exception {
      Scanner scanner = new ObjectScanner(new DetailScanner(Example1.class), new Support());
      Instantiator creator  = scanner.getInstantiator();
      List<Creator> list = creator.getCreators();
      System.err.println(list.get(0));
   }
}
