package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.strategy.Type;
import com.linkoog.simpleframework.xml.strategy.Visitor;
import com.linkoog.simpleframework.xml.strategy.VisitorStrategy;
import com.linkoog.simpleframework.xml.stream.CamelCaseStyle;
import com.linkoog.simpleframework.xml.stream.Format;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.NodeMap;
import com.linkoog.simpleframework.xml.stream.OutputNode;
import com.linkoog.simpleframework.xml.stream.Style;

public class VisitorSetNodeNameTest extends ValidationTestCase {
   
   @Default
   private static class RenameExample {
      private String name;
      private String value;
      public RenameExample(@Element(name="name") String name, @Element(name="value") String value) {
         this.name = name;
         this.value = value;
      }
   }

   public void testVisitor() throws Exception {
      Style style = new CamelCaseStyle();
      Format format = new Format(style);
      Visitor visitor = new RenameVisitor();
      VisitorStrategy strategy = new VisitorStrategy(visitor);
      Persister renamePersister = new Persister(strategy);
      Persister normalPersister = new Persister(format);
      RenameExample example = new RenameExample("example name", "example value");
      StringWriter renameWriter = new StringWriter();
      StringWriter normalWriter = new StringWriter();
      renamePersister.write(example, renameWriter);
      normalPersister.write(example, normalWriter);
      String renameResult = renameWriter.toString();
      String normalResult = normalWriter.toString();
      System.out.println(renameResult);
      System.out.println(normalResult);
      assertElementExists(renameResult, "/RENAMEEXAMPLE");
      assertElementExists(renameResult, "/RENAMEEXAMPLE/NAME");
      assertElementExists(renameResult, "/RENAMEEXAMPLE/VALUE");
      assertElementHasValue(renameResult, "/RENAMEEXAMPLE/NAME", "example name");
      assertElementHasValue(renameResult, "/RENAMEEXAMPLE/VALUE", "example value");
      assertElementExists(normalResult, "/RenameExample");
      assertElementExists(normalResult, "/RenameExample/Name");
      assertElementExists(normalResult, "/RenameExample/Value");
      assertElementHasValue(normalResult, "/RenameExample/Name", "example name");
      assertElementHasValue(normalResult, "/RenameExample/Value", "example value");
   }
   
   private static class RenameVisitor implements Visitor {

      public void read(Type type, NodeMap<InputNode> node) throws Exception {
         throw new IllegalStateException("Can not deserialize");
      }

      public void write(Type type, NodeMap<OutputNode> node) throws Exception {
         OutputNode parent = node.getNode();
         String name = parent.getName();
         name = name.toUpperCase();
         parent.setName(name);
      }
      
   }
   
   
}
