package com.linkoog.simpleframework.xml.convert;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Convert;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.annotations.Transient;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.strategy.Strategy;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.NodeMap;
import com.linkoog.simpleframework.xml.stream.OutputNode;

public class DynamicMapOfAttributesTest extends ValidationTestCase {

   private static final String SOURCE =
   "<Car color='green' length='3.3' nrOfDoors='2' topSpeed='190' brand='audi' /> ";
   
   private static class CarConverter implements Converter<Car> {

      private final Persister persister;
      
      public CarConverter() {
         this.persister = new Persister();
      }
      
      public Car read(InputNode node) throws Exception {
         Car car = persister.read(Car.class, node, false);
         NodeMap<InputNode> attributes = node.getAttributes();
         
         for(String name : attributes) {
            InputNode attribute = attributes.get(name);
            String value = attribute.getValue();
            
            car.furtherAttributes.put(name, value);
         }
         return car;
      }

      public void write(OutputNode node, Car car) throws Exception {
         Set<String> keys = car.furtherAttributes.keySet();
         
         for(String name : keys) {
            String value = car.furtherAttributes.get(name);
            
            node.setAttribute(name, value);
         }
      }
   }

   @Root(name="Car")
   @Convert(CarConverter.class)
   private static class Car
   {
       @Attribute
       private double length;

       @Attribute
       private String color;

       @Attribute
       private int nrOfDoors;
       
       @Transient
       private Map<String, String> furtherAttributes;
       
       public Car() {
          this.furtherAttributes = new HashMap<String, String>();
       }
   }
   
   public void testConverter() throws Exception {
      Strategy strategy = new AnnotationStrategy();
      XmlMapper xmlMapper = new Persister(strategy);
      StringWriter buffer = new StringWriter();
      Car car = xmlMapper.read(Car.class, SOURCE, false);
      
      assertNotNull(car);
      assertEquals(car.length, 3.3);
      assertEquals(car.color, "green");
      assertEquals(car.nrOfDoors, 2);
      assertEquals(car.furtherAttributes.get("topSpeed"), "190");
      assertEquals(car.furtherAttributes.get("brand"), "audi");
      
      xmlMapper.write(car, System.out);
      xmlMapper.write(car, buffer);
      
      String text = buffer.toString();
      assertElementExists(text, "/Car");
      assertElementHasAttribute(text, "/Car", "length", "3.3");
      assertElementHasAttribute(text, "/Car", "color", "green");
      assertElementHasAttribute(text, "/Car", "nrOfDoors", "2");
      assertElementHasAttribute(text, "/Car", "topSpeed", "190");
      assertElementHasAttribute(text, "/Car", "brand", "audi");
   }
}
