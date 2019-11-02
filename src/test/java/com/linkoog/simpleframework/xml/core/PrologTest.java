package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.stream.Format;

public class PrologTest extends ValidationTestCase {

   private static final String SOURCE =
   "<prologExample id='12' flag='true'>\n"+
   "   <text>entry text</text>  \n\r"+
   "   <name>some name</name> \n"+
   "</prologExample>";


   @Root
   private static class PrologExample {

      @Attribute
      public int id;           

      @Element
      public String name;
           
      @Element
      public String text;  
      
      @Attribute
      public boolean flag;              
   }
        
	private Persister serializer;

	public void setUp() {
	   serializer = new Persister(new Format(4, "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"));
	}
	
   public void testProlog() throws Exception {    
      PrologExample example = serializer.read(PrologExample.class, SOURCE);
      
      assertEquals(example.id, 12);
      assertEquals(example.text, "entry text");
      assertEquals(example.name, "some name");
      assertTrue(example.flag);

      StringWriter buffer = new StringWriter();
      serializer.write(example, buffer);
      String text = buffer.toString();

      assertTrue(text.startsWith("<?xml"));
      validate(example, serializer);
   }
}
