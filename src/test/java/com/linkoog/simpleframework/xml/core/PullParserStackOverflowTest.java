package com.linkoog.simpleframework.xml.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class PullParserStackOverflowTest extends ValidationTestCase {

   private static final String BROKEN_ROOT_INCOMPLETE = 
   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
   "level/>"; 
   
   private static final String BROKEN_ROOT_COMPLETE = 
   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
   "level number='10'><text>example text</text></level>"; 
   
   private static final String BROKEN_ELEMENT_COMPLETE = 
   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
   "<level number='10'>text>example text</text></level>"; 
   
   @Root(name = "level")
   public static class Level {
      @Attribute(name="number")
      private int levelNumber;
      @Element(name="text")
      private String name;
   }
   
   public void testBrokenStart() throws Exception {
      XmlMapper xmlMapper = new Persister();
      byte[] data = BROKEN_ROOT_INCOMPLETE.getBytes("ISO-8859-1");
      InputStream stream = new ByteArrayInputStream(data);
      boolean exception = false;
      try {
         xmlMapper.read(Level.class, stream);
      }catch(Exception e) {
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Exception should have been thrown for broken root", exception);
   }
   
   public void testBrokenStartButComplete() throws Exception {
      XmlMapper xmlMapper = new Persister();
      byte[] data = BROKEN_ROOT_COMPLETE.getBytes("ISO-8859-1");
      InputStream stream = new ByteArrayInputStream(data);
      boolean exception = false;
      try {
         xmlMapper.read(Level.class, stream);
      }catch(Exception e) {
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Exception should have been thrown for broken root", exception);
   }
   
   public void testBrokenElementButComplete() throws Exception {
      XmlMapper xmlMapper = new Persister();
      byte[] data = BROKEN_ELEMENT_COMPLETE.getBytes("ISO-8859-1");
      InputStream stream = new ByteArrayInputStream(data);
      boolean exception = false;
      try {
         xmlMapper.read(Level.class, stream);
      }catch(Exception e) {
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Exception should have been thrown for broken element", exception);
   }
}
