package com.linkoog.simpleframework.xml.convert;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.linkoog.simpleframework.xml.annotations.Convert;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.Serializer;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.strategy.Strategy;

public class ContactEntryTest extends ValidationTestCase {
   
   @Root
   public static class EntryList {
      
      @ElementList(inline=true)
      private List<ExampleConverters.Entry> list;
      
      @Element
      @Convert(ExampleConverters.OtherEntryConverter.class)
      private ExampleConverters.Entry other;
      
      @Element
      private ExampleConverters.Entry inheritConverter;
      
      @Element
      private ExampleConverters.Entry polymorhic;
      
      @Element
      @Convert(ExampleConverters.EntryListConverter.class)
      private List<ExampleConverters.Entry> otherList;
      
      public EntryList() {
         this("Default", "Value");
      }
      public EntryList(String name, String value){
         this.list = new ArrayList<ExampleConverters.Entry>();
         this.otherList = new ArrayList<ExampleConverters.Entry>();
         this.other = new ExampleConverters.Entry(name, value);
         this.inheritConverter = new ExampleConverters.Entry("INHERIT", "inherit");
         this.polymorhic = new ExampleConverters.ExtendedEntry("POLY", "poly", 12);
      }
      public ExampleConverters.Entry getInherit() {
         return inheritConverter;
      }
      public ExampleConverters.Entry getOther() {
         return other;
      }
      public List<ExampleConverters.Entry> getList() {
         return list;
      }
      public List<ExampleConverters.Entry> getOtherList() {
         return otherList;
      }
   }
  
   public void testContact() throws Exception {
      Strategy strategy = new AnnotationStrategy();
      Serializer serializer = new Persister(strategy);
      EntryList list = new EntryList("Other", "Value");
      StringWriter writer = new StringWriter();
      
      list.getList().add(new ExampleConverters.Entry("a", "A"));
      list.getList().add(new ExampleConverters.Entry("b", "B"));
      list.getList().add(new ExampleConverters.Entry("c", "C"));
      
      list.getOtherList().add(new ExampleConverters.Entry("1", "ONE"));
      list.getOtherList().add(new ExampleConverters.Entry("2", "TWO"));
      list.getOtherList().add(new ExampleConverters.Entry("3", "THREE"));
      
      serializer.write(list, writer);
      
      String text = writer.toString();
      EntryList copy = serializer.read(EntryList.class, text);
      
      assertEquals(copy.getList().get(0).getName(), list.getList().get(0).getName());
      assertEquals(copy.getList().get(0).getValue(), list.getList().get(0).getValue());
      assertEquals(copy.getList().get(1).getName(), list.getList().get(1).getName());
      assertEquals(copy.getList().get(1).getValue(), list.getList().get(1).getValue());
      assertEquals(copy.getList().get(2).getName(), list.getList().get(2).getName());
      assertEquals(copy.getList().get(2).getValue(), list.getList().get(2).getValue());
      
      assertEquals(copy.getOtherList().get(0).getName(), list.getOtherList().get(0).getName());
      assertEquals(copy.getOtherList().get(0).getValue(), list.getOtherList().get(0).getValue());
      assertEquals(copy.getOtherList().get(1).getName(), list.getOtherList().get(1).getName());
      assertEquals(copy.getOtherList().get(1).getValue(), list.getOtherList().get(1).getValue());
      assertEquals(copy.getOtherList().get(2).getName(), list.getOtherList().get(2).getName());
      assertEquals(copy.getOtherList().get(2).getValue(), list.getOtherList().get(2).getValue());
      
      System.out.println(text);
   }

}
