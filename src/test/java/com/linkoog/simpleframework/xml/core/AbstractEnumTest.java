package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import junit.framework.TestCase;

import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;

public class AbstractEnumTest extends TestCase {

   public void testFoo() throws Exception {
   final XmlMapper xmlMapper = new Persister();
   final FooObject fooObject = new FooObject();
   fooObject.setFoo(Foo.NAY);
   xmlMapper.write(fooObject, new StringWriter());
   }

   @Root
   public static class FooObject {

   @Element
   private Foo foo;

   public FooObject() {
   }

   public Foo getFoo() {
   return foo;
   }

   public void setFoo(Foo foo) {
   this.foo = foo;
   }
   }

   public static enum Foo {
   YEA {
   public boolean foo() {
   return true;
   }
   },
   NAY {
   public boolean foo() {
   return false;
   }
   };

   public abstract boolean foo();
   }
}
