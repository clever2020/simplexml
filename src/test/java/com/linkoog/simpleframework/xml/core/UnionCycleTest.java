package com.linkoog.simpleframework.xml.core;

import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementUnion;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.strategy.CycleStrategy;
import com.linkoog.simpleframework.xml.strategy.Strategy;

public class UnionCycleTest extends ValidationTestCase {
   
   private static final String SOURCE =
   "<shapeExample>" +
   "  <circle>" +
   "    <type>CIRCLE</type>" +
   "  </circle>" +
   "</shapeExample>";
   
   @Root
   public static class Square implements Shape {
      @Element
      private String type;
      
      public String type() {
         return type;
      }
   }
   @Root
   public static class Circle implements Shape {
      @Element
      private String type;
      
      public String type() {
         return type;
      }
   }
   public static interface Shape<T> {
      public String type();
   }
   @Root
   public static class ShapeExample {
      
      @ElementUnion({
         @Element(name="circle", type=Circle.class),
         @Element(name="square", type=Square.class)
      })
      private Shape shape;
   }
      
   public void testUnionCycle() throws Exception {
      Strategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      ShapeExample example = persister.read(ShapeExample.class, SOURCE);
      validate(persister, example);
   }
}
