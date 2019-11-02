package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.Serializer;
import com.linkoog.simpleframework.xml.ValidationTestCase;

/**
 * @author <a href="bogdang@amazon.com">Bogdan Ghidireac</a>
 */
public class MetadataSerializationTest extends ValidationTestCase {
   

   /**
    * @author <a href="bogdang@amazon.com">Bogdan Ghidireac</a>
    */
   public static class A {

       private final Set<B> setOfB;
       private final Set<C> setOfC;

       public A(@ElementList(name = "setOfB", required = false) Set<B> setOfB,
                @ElementList(name = "setOfC", required = false) Set<C> setOfC) {

           this.setOfB = setOfB;
           this.setOfC = setOfC;
       }

       @ElementList(name="setOfB", required = false)
       public Set<B> getSetOfB() {
           return setOfB;
       }

       @ElementList(name="setOfC", required = false)
       public Set<C> getSetOfC() {
           return setOfC;
       }
   }
   

   /**
    * @author <a href="bogdang@amazon.com">Bogdan Ghidireac</a>
    */
   public static class B {

       private final String name;

       public B(@Attribute(name="name") String name) {
           this.name = name;
       }

       @Attribute(name="name")
       public String getName() {
           return name;
       }
   }
   

   /**
    * @author <a href="bogdang@amazon.com">Bogdan Ghidireac</a>
    */
   public static class C {

       private final String name;

       public C(@Attribute(name="name") String name) {
           this.name = name;
       }

       @Attribute(name="name")
       public String getName() {
           return name;
       }
   }

    public void testWriteA() throws Exception {
        A a = new A(
                new HashSet<B>(Arrays.asList(new B("bbb"))),
                new HashSet<C>(Arrays.asList(new C("ccc"))));

        Serializer serializer = new Persister();
        StringWriter writer = new StringWriter();
        serializer.write(a, writer);

        System.out.println(writer.getBuffer().toString());
    }
}
