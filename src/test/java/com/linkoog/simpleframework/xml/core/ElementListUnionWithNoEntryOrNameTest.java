package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.ElementListUnion;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class ElementListUnionWithNoEntryOrNameTest extends ValidationTestCase {
   
   @Root
   private static class ExampleWithNoNameOrEntry {
      @ElementListUnion({
         @ElementList(inline=true, entry="a", type=Integer.class),
         @ElementList(inline=true, entry="b", type=Double.class),
         @ElementList(inline=true, type=String.class)
      })
      private List<Object> value = new LinkedList<Object>();
   }
   
   public void testExample() throws Exception {
      ExampleWithNoNameOrEntry e = new ExampleWithNoNameOrEntry();
      e.value.add(11);
      e.value.add(2.0);
      e.value.add("xxx");
      Persister persister = new Persister();
      StringWriter writer = new StringWriter();
      persister.write(e,  writer);
      persister.write(e, System.out);
      ExampleWithNoNameOrEntry o = persister.read(ExampleWithNoNameOrEntry.class, writer.toString());
      assertEquals(o.value.get(0), e.value.get(0));
      assertEquals(o.value.get(1), e.value.get(1));
      assertEquals(o.value.get(2), e.value.get(2));
      validate(persister,e);
   }

}
