package com.linkoog.simpleframework.xml.convert;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Convert;
import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.strategy.Strategy;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.OutputNode;
import junit.framework.TestCase;

public class WrapperTest extends TestCase {
   
   private static class Wrapper {
      private final Object value;
      public Wrapper(Object value) {
         this.value = value;
      }
      public Object get(){
         return value;
      }
   }
   
   private static class WrapperConverter implements Converter<Wrapper> {
      private final XmlMapper xmlMapper;
      public WrapperConverter(XmlMapper xmlMapper) {
         this.xmlMapper = xmlMapper;
      }
      public Wrapper read(InputNode node) throws Exception {
         InputNode type = node.getAttribute("type");
         InputNode child = node.getNext();
         String className = type.getValue();
         Object value = null;
         if(child != null) {
            value = xmlMapper.read(Class.forName(className), child);
         }
         return new Wrapper(value);
      }
      public void write(OutputNode node, Wrapper wrapper) throws Exception {
         Object value = wrapper.get();
         Class type = value.getClass();
         String className = type.getName();
         node.setAttribute("type", className);
         xmlMapper.write(value, node);
      }
   } 
   
   @Root
   @Default(required=false)
   private static class Entry {
      private String name;
      private String value;
      public Entry(@Element(name="name", required=false) String name, @Element(name="value", required=false) String value){
         this.name = name;
         this.value = value;
      }
   }
   
   @Root
   @Default(required=false)
   private static class WrapperExample {
      @Convert(WrapperConverter.class)
      private Wrapper wrapper;
      public WrapperExample(@Element(name="wrapper", required=false) Wrapper wrapper) {
         this.wrapper = wrapper;
      }
   }

   public void testWrapper() throws Exception{
      Registry registry = new Registry();
      Strategy strategy = new RegistryStrategy(registry);
      XmlMapper xmlMapper = new Persister(strategy);
      Entry entry = new Entry("name", "value");
      Wrapper wrapper = new Wrapper(entry);
      WrapperExample example = new WrapperExample(wrapper);
      WrapperConverter converter = new WrapperConverter(xmlMapper);
      StringWriter writer = new StringWriter();
      registry.bind(Wrapper.class, converter);
      xmlMapper.write(example, writer);
      xmlMapper.read(WrapperExample.class, writer.toString());
      System.err.println(writer.toString());
   }
}
