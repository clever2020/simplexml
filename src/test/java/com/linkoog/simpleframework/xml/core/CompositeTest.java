package com.linkoog.simpleframework.xml.core;

import java.util.List;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.annotations.Text;
import com.linkoog.simpleframework.xml.ValidationTestCase;


public class CompositeTest extends ValidationTestCase {
   
   private static final String SOURCE =
   "<compositeObject>" +
   "   <interfaceEntry name='name.1' class='CompositeTest$CompositeEntry'>value.1</interfaceEntry>" +
   "   <objectEntry name='name.2' class='CompositeTest$CompositeEntry'>value.2</objectEntry>" +
   "   <interfaceList>" +
   "      <interfaceEntry name='a' class='CompositeTest$CompositeEntry'>a</interfaceEntry>" +
   "      <interfaceEntry name='b' class='CompositeTest$CompositeEntry'>b</interfaceEntry>" +
   "   </interfaceList>" +
   "   <objectList>" +
   "      <objectEntry name='a' class='CompositeTest$CompositeEntry'>a</objectEntry>" +
   "      <objectEntry name='b' class='CompositeTest$CompositeEntry'>b</objectEntry>" +
   "   </objectList>" +
   "</compositeObject>";
   
   private static interface Entry {
      
      public String getName();
      
      public String getValue();
   }
   
   @Root
   public static class CompositeEntry implements Entry {
      
      @Attribute
      private String name;
      
      @Text
      private String value;
      
      public String getName() {
         return name;
      }
      
      public String getValue() {
         return value;
      }
   }
   
   @Root
   private static class CompositeObject {
      
      @ElementList
      private List<Entry> interfaceList;
      
      @ElementList
      private List<Object> objectList;
      
      @Element
      private Entry interfaceEntry;
      
      @Element
      private Object objectEntry;
      
      public CompositeEntry getInterface() {
         return (CompositeEntry) interfaceEntry;
      }
      
      public CompositeEntry getObject() {
         return (CompositeEntry) objectEntry;
      }
      
      public List<Entry> getInterfaceList() {
         return interfaceList;
      }
      
      public List<Object> getObjectList() {
         return objectList;
      }
   }
   
   public void testComposite() throws Exception {
      Persister persister = new Persister();
      CompositeObject object = persister.read(CompositeObject.class, SOURCE);
      CompositeEntry objectEntry = object.getObject();
      CompositeEntry interfaceEntry = object.getInterface();
      
      assertEquals(interfaceEntry.getName(), "name.1");
      assertEquals(interfaceEntry.getName(), "name.1");
      assertEquals(objectEntry.getName(), "name.2");
      assertEquals(objectEntry.getValue(), "value.2");   
      
      List<Entry> interfaceList = object.getInterfaceList();
      List<Object> objectList = object.getObjectList();
      
      assertEquals(interfaceList.get(0).getName(), "a");
      assertEquals(interfaceList.get(0).getValue(), "a");
      assertEquals(interfaceList.get(1).getName(), "b");
      assertEquals(interfaceList.get(1).getValue(), "b");
      
      assertEquals(((Entry)objectList.get(0)).getName(), "a");
      assertEquals(((Entry)objectList.get(0)).getName(), "a");
      assertEquals(((Entry)objectList.get(1)).getName(), "b");
      assertEquals(((Entry)objectList.get(1)).getName(), "b");
   }
}
