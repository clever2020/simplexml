package com.linkoog.simpleframework.xml.convert;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.linkoog.simpleframework.xml.annotations.Convert;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.strategy.CycleStrategy;

public class AnnotationCycleStrategyTest extends ValidationTestCase {

   @Root
   public static class EntryListExample {
      @ElementList(inline=true)
      private List<ExampleConverters.Entry> list = new ArrayList<ExampleConverters.Entry>();
      @Element
      @Convert(ExampleConverters.OtherEntryConverter.class)
      private ExampleConverters.Entry primary;
      public ExampleConverters.Entry getPrimary() {
         return primary;
      }
      public void setPrimary(ExampleConverters.Entry primary) {
         this.primary = primary;
      }
      public void addEntry(ExampleConverters.Entry entry){
         list.add(entry);
      }
      public List<ExampleConverters.Entry> getEntries(){
         return list;
      }
   }
   
   public void testCycle() throws Exception {
      CycleStrategy inner = new CycleStrategy();
      AnnotationStrategy strategy = new AnnotationStrategy(inner);
      Persister persister = new Persister(strategy);
      EntryListExample list = new EntryListExample();
      StringWriter writer = new StringWriter();
   
      ExampleConverters.Entry a = new ExampleConverters.Entry("A", "a");
      ExampleConverters.Entry b = new ExampleConverters.Entry("B", "b");
      ExampleConverters.Entry c = new ExampleConverters.Entry("C", "c");
      ExampleConverters.Entry primary = new ExampleConverters.Entry("PRIMARY", "primary");
      
      list.setPrimary(primary);
      list.addEntry(a);
      list.addEntry(b);
      list.addEntry(c);
      list.addEntry(b);
      list.addEntry(c);
      
      persister.write(list, writer);
      persister.write(list, System.out);
   
      String text = writer.toString();
      EntryListExample copy = persister.read(EntryListExample.class, text);
      
      assertEquals(copy.getEntries().get(0), list.getEntries().get(0));
      assertEquals(copy.getEntries().get(1), list.getEntries().get(1));
      assertEquals(copy.getEntries().get(2), list.getEntries().get(2));
      assertEquals(copy.getEntries().get(3), list.getEntries().get(3));
      assertEquals(copy.getEntries().get(4), list.getEntries().get(4));
      
      assertTrue(copy.getEntries().get(2) == copy.getEntries().get(4)); // cycle
      assertTrue(copy.getEntries().get(1) == copy.getEntries().get(3)); // cycle
      
      assertElementExists(text, "/entryListExample");
      assertElementExists(text, "/entryListExample/entry[1]");
      assertElementExists(text, "/entryListExample/entry[1]/name");
      assertElementExists(text, "/entryListExample/entry[1]/value");
      assertElementHasValue(text, "/entryListExample/entry[1]/name", "A");
      assertElementHasValue(text, "/entryListExample/entry[1]/value", "a");
      assertElementExists(text, "/entryListExample/entry[2]/name");
      assertElementExists(text, "/entryListExample/entry[2]/value");
      assertElementHasValue(text, "/entryListExample/entry[2]/name", "B");
      assertElementHasValue(text, "/entryListExample/entry[2]/value", "b");
      assertElementExists(text, "/entryListExample/entry[3]/name");
      assertElementExists(text, "/entryListExample/entry[3]/value");
      assertElementHasValue(text, "/entryListExample/entry[3]/name", "C");
      assertElementHasValue(text, "/entryListExample/entry[3]/value", "c");
      assertElementExists(text, "/entryListExample/entry[4]");
      assertElementExists(text, "/entryListExample/entry[5]");
      assertElementHasAttribute(text, "/entryListExample/entry[4]", "reference", "2"); // cycle
      assertElementHasAttribute(text, "/entryListExample/entry[5]", "reference", "3"); // cycle
      assertElementExists(text, "/entryListExample/primary");
      assertElementHasAttribute(text, "/entryListExample/primary", "name", "PRIMARY"); // other converter
      assertElementHasAttribute(text, "/entryListExample/primary", "value", "primary"); // other converter
   }
   
}
