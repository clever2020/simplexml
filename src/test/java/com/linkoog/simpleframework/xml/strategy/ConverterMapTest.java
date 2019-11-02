package com.linkoog.simpleframework.xml.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementMap;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.convert.AnnotationStrategy;
import com.linkoog.simpleframework.xml.annotations.Convert;
import com.linkoog.simpleframework.xml.convert.Converter;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.OutputNode;

public class ConverterMapTest extends ValidationTestCase {
   
   private static class MapConverter implements Converter<Map> {
      
      public Map read(InputNode node) throws Exception{
         java.util.Map map = new HashMap();
         
         while(true) {
            InputNode next = node.getNext("entry");
            
            if(next == null) {
               break;
            }
            Entry entry = readEntry(next);
      
            map.put(entry.name, entry.value);
         }
         return map;
      }
      
      public void write(OutputNode node, Map map) throws Exception {
         Set keys = map.keySet();
         
         for(Object key : keys) {
            OutputNode next = node.getChild("entry");
            next.setAttribute("key", key.toString());
            OutputNode value = next.getChild("value");
            value.setValue(map.get(key).toString());
         }
      }
      
      private Entry readEntry(InputNode node) throws Exception {
         InputNode key = node.getAttribute("key");
         InputNode value = node.getNext("value");
         
         return new Entry(key.getValue(), value.getValue());
      }
      
      private static class Entry {
         private String name;
         private String value;
         public Entry(String name, String value){
            this.name = name;
            this.value = value;
         }
      }
   }
   
   @Root
   @Default
   private static class MapHolder {
      @Element
      @ElementMap
      @Convert(MapConverter.class)
      private Map<String, String> map = new HashMap<String, String>();
      public void put(String name, String value){
         map.put(name, value);
      }
   }
   
   public void testMap() throws Exception {
      Strategy strategy = new AnnotationStrategy();
      XmlMapper xmlMapper = new Persister(strategy);
      MapHolder holder = new MapHolder();
      
      holder.put("a", "A");
      holder.put("b", "B");
      holder.put("c", "C");
      holder.put("d", "D");
      holder.put("e", "E");
      holder.put("f", "F");
      
      xmlMapper.write(holder, System.out);
      
      validate(holder, xmlMapper);
   }

}
