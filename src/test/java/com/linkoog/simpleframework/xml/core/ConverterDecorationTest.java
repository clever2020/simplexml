package com.linkoog.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.List;

import com.linkoog.simpleframework.xml.convert.Converter;
import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.convert.AnnotationStrategy;
import com.linkoog.simpleframework.xml.annotations.Convert;
import com.linkoog.simpleframework.xml.strategy.CycleStrategy;
import com.linkoog.simpleframework.xml.strategy.Strategy;
import com.linkoog.simpleframework.xml.stream.CamelCaseStyle;
import com.linkoog.simpleframework.xml.stream.Format;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.OutputNode;
import com.linkoog.simpleframework.xml.stream.Style;

public class ConverterDecorationTest extends ValidationTestCase {
   
   @Default  
   private static class ConverterDecoration {      
      private List<ConverterDecorationExample> list;
      private List<NormalExample> normal;
      public ConverterDecoration(@ElementList(name="list") List<ConverterDecorationExample> list, @ElementList(name="normal") List<NormalExample> normal) {
         this.list = list;
         this.normal = normal;
      }
   }
   
   @Root
   @Namespace(reference="http://blah/host1")
   @Convert(ConverterDecorationConverter.class)
   private static class ConverterDecorationExample {
      private final String name;
      public ConverterDecorationExample(String name){
         this.name = name;
      }
   }
   
   @Default
   @Namespace(reference="http://blah/normal")
   private static class NormalExample {
      private final String name;
      public NormalExample(@Element(name="name") String name){
         this.name = name;
      }
   }
   
   private static class ConverterDecorationConverter implements Converter<ConverterDecorationExample> {
      public ConverterDecorationExample read(InputNode node) throws Exception {
         String name = node.getValue();
         return new ConverterDecorationExample(name);
      }      
      public void write(OutputNode node, ConverterDecorationExample value) throws Exception {
         node.setValue(value.name);
      }
      
   }
   
   public void testConverter() throws Exception {
      Style style = new CamelCaseStyle();
      Format format = new Format(style);
      Strategy cycle = new CycleStrategy();
      Strategy strategy = new AnnotationStrategy(cycle);
      Persister persister = new Persister(strategy, format);
      List<ConverterDecorationExample> list = new ArrayList<ConverterDecorationExample>();
      List<NormalExample> normal = new ArrayList<NormalExample>();
      ConverterDecoration example = new ConverterDecoration(list, normal);
      ConverterDecorationExample duplicate = new ConverterDecorationExample("duplicate");
      NormalExample normalDuplicate = new NormalExample("duplicate");
      list.add(duplicate);
      list.add(new ConverterDecorationExample("a"));
      list.add(new ConverterDecorationExample("b"));
      list.add(new ConverterDecorationExample("c"));
      list.add(duplicate);
      list.add(new ConverterDecorationExample("d"));
      list.add(duplicate);
      normal.add(normalDuplicate);
      normal.add(new NormalExample("1"));
      normal.add(new NormalExample("2"));
      normal.add(normalDuplicate);
      persister.write(example, System.err);     
   }

}
