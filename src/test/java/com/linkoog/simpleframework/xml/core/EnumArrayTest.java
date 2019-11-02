package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;

public class EnumArrayTest extends ValidationTestCase {
   
   private static final String SOURCE =
   "<example size='3'>"+
   "  <array>ONE,TWO,FOUR</array>"+
   "</example>";
   
   private static enum Number {
      ONE,
      TWO,
      THREE,
      FOUR
   }
   
   @Root(name="example")
   private static class NumberArray {
      
      @Element(name="array")
      private final Number[] array;
      
      private final int size;
      
      public NumberArray(@Element(name="array") Number[] array, @Attribute(name="size") int size) {
         this.array = array;
         this.size = size;
      }
      
      @Attribute(name="size")
      public int getLength() {
         return size;
      }
   }
   
   public void testArrayElement() throws Exception {
      Persister persister = new Persister();
      NumberArray array = persister.read(NumberArray.class, SOURCE);
      
      assertEquals(array.array.length, 3);
      assertEquals(array.array[0], Number.ONE);
      assertEquals(array.array[1], Number.TWO);
      assertEquals(array.array[2], Number.FOUR);
      assertEquals(array.getLength(), array.size);
      assertEquals(array.array.length, array.size);
      
      validate(persister, array);
   }

}
