package com.linkoog.simpleframework.xml.convert;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Convert;
import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.strategy.Strategy;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.OutputNode;

public class HideEnclosingConverterTest extends ValidationTestCase {

   public static class EntryConverter implements Converter<Entry> {
      private final XmlMapper xmlMapper;
      public EntryConverter() {
         this.xmlMapper = new Persister();
      }
      public Entry read(InputNode node) throws Exception {
         return xmlMapper.read(Entry.class, node);
      }
      public void write(OutputNode node, Entry entry) throws Exception {
         if(!node.isCommitted()) {
            node.remove();
         }
         xmlMapper.write(entry, node.getParent());
      }
   }
   
   @Default(required=false)
   public static class Entry {
      private final String name;
      private final String value;
      public Entry(@Element(name="name", required=false) String name, @Element(name="value", required=false) String value){
         this.name = name;
         this.value = value;
      }
      public String getName(){
         return name;
      }
      public String getValue(){
         return value;
      }
   }
   
   @Default
   public static class EntryHolder {
      @Convert(EntryConverter.class)
      private final Entry entry;
      private final String name;
      @Attribute
      private final int code;
      public EntryHolder(@Element(name="entry") Entry entry, @Element(name="name") String name, @Attribute(name="code") int code) {
         this.entry = entry;
         this.name = name;
         this.code = code;
      }
      public Entry getEntry(){
         return entry;
      }
      public String getName() {
         return name;
      }
      public int getCode() {
         return code;
      }
   }

   public void testWrapper() throws Exception{
      Strategy strategy = new AnnotationStrategy();
      XmlMapper xmlMapper = new Persister(strategy);
      Entry entry = new Entry("name", "value");
      EntryHolder holder = new EntryHolder(entry, "test", 10);
      StringWriter writer = new StringWriter();
      xmlMapper.write(holder, writer);
      System.out.println(writer.toString());
      xmlMapper.read(EntryHolder.class, writer.toString());
      System.err.println(writer.toString());
      String sourceXml = writer.toString();
      assertElementExists(sourceXml, "/entryHolder");
      assertElementHasAttribute(sourceXml, "/entryHolder", "code", "10");
      assertElementExists(sourceXml, "/entryHolder/entry");
      assertElementExists(sourceXml, "/entryHolder/entry/name");
      assertElementHasValue(sourceXml, "/entryHolder/entry/name", "name");
      assertElementExists(sourceXml, "/entryHolder/entry/value");
      assertElementHasValue(sourceXml, "/entryHolder/entry/value", "value");
      assertElementExists(sourceXml, "/entryHolder/name");
      assertElementHasValue(sourceXml, "/entryHolder/name", "test");
   }
}
