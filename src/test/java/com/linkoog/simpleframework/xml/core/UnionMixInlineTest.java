package com.linkoog.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.List;

import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.ElementListUnion;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class UnionMixInlineTest extends ValidationTestCase {
   
   @Root
   private static class Example {
      @ElementListUnion({
         @ElementList(name="x", inline=true, type=Integer.class),
         @ElementList(name="y", inline=false, type=Double.class),
         @ElementList(name="z", inline=false, type=String.class)
      })
      private List<Object> list = new ArrayList<Object>();
      public void add(Object o) {
         list.add(o);
      }
   }
   
   public void testInlineMix() throws Exception {
      Persister persister = new Persister();
      Example example = new Example();
      boolean exception = false;
      example.add(1);
      example.add(1.0);
      example.add("1");
      try {
         persister.write(example, System.out);
      }catch(UnionException e){
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Inline must be consistent across union declarations", exception);
         
   }

}
