package com.linkoog.simpleframework.xml.stream;

import junit.framework.TestCase;

public class StyleTest extends TestCase {
   
   public void testHyphenStyle() {
      Style strategy = new HyphenStyle();
      
      System.err.println(strategy.getElement("Base64Encoder"));
      System.err.println(strategy.getElement("Base64_Encoder"));
      System.err.println(strategy.getElement("Base64___encoder"));
      System.err.println(strategy.getElement("base64--encoder"));
      System.err.println(strategy.getElement("Base64encoder"));
      System.err.println(strategy.getElement("_Base64encoder"));
      System.err.println(strategy.getElement("__Base64encoder"));
      System.err.println(strategy.getElement("URLList"));
      System.err.println(strategy.getElement("__Base64encoder"));
      System.err.println(strategy.getElement("Base_64_Encoder"));
      System.err.println(strategy.getElement("base_64_encoder"));
   }

   public void testCamelCaseStyle() {
      Style strategy = new CamelCaseStyle();
      
      System.err.println(strategy.getElement("Base64Encoder"));
      System.err.println(strategy.getElement("Base64_Encoder"));
      System.err.println(strategy.getElement("Base64___encoder"));
      System.err.println(strategy.getElement("base64--encoder"));
      System.err.println(strategy.getElement("Base64encoder"));
      System.err.println(strategy.getElement("_Base64encoder"));
      System.err.println(strategy.getElement("__Base64encoder"));
      System.err.println(strategy.getElement("URLList"));
      System.err.println(strategy.getElement("__Base64encoder"));
      System.err.println(strategy.getElement("Base_64_Encoder"));
      System.err.println(strategy.getElement("base_64_encoder"));
   }
}
