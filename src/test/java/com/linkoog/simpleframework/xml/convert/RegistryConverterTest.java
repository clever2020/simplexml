package com.linkoog.simpleframework.xml.convert;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.DefaultType;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.stream.CamelCaseStyle;
import com.linkoog.simpleframework.xml.stream.Format;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.OutputNode;
import com.linkoog.simpleframework.xml.stream.Style;

public class RegistryConverterTest extends ValidationTestCase {

   public static class EnvelopeConverter implements Converter<Envelope> {
      private final XmlMapper xmlMapper;
      public EnvelopeConverter(XmlMapper xmlMapper) {
         this.xmlMapper = xmlMapper;
      }
      public Envelope read(InputNode node) throws Exception {
         return xmlMapper.read(Envelope.class, node);
      }
      public void write(OutputNode node, Envelope value) throws Exception {
         xmlMapper.write(value.getValue(), node);
      }
   }
   
   public static class PersonConverter implements Converter<Person> {
      private final XmlMapper xmlMapper;
      public PersonConverter(XmlMapper xmlMapper) {
         this.xmlMapper = xmlMapper;
      }
      public Person read(InputNode node) throws Exception {
         return xmlMapper.read(PersonDelegate.class, node.getNext());
      }
      public void write(OutputNode node, Person value) throws Exception {
         Person person = new PersonDelegate(value);
         xmlMapper.write(person, node);
      }
      @Root
      @Default(DefaultType.PROPERTY)
      private static class PersonDelegate extends Person {
         public PersonDelegate() {
            super();
         }
         public PersonDelegate(Person person) {
            super(person.name, person.age);
         }
         public String getName() {
            return name;
         }
         public void setName(String name) {
            this.name = name;
         }
         public int getAge() {
            return age;
         }
         public void setAge(int age) {
            this.age = age;
         }
      }
   }
   
   
   @Root
   private static class OrderItem {
      @Element
      private Envelope envelope;
      public OrderItem() {
         super();
      }
      public OrderItem(Envelope envelope) {
         this.envelope = envelope;
      }
   }
   
   @Root
   @Default
   private static class PersonProfile {
      private Person person;
      public PersonProfile() {
         super();
      }
      public PersonProfile(Person person) {
         this.person = person;
      }
   }
   
   private static class Person {
      protected String name;
      protected int age;
      public Person() {
         super();
      }
      public Person(String name, int age) {
         this.name = name;
         this.age = age;
      }
   }
   
   @Root
   @Default
   private static class Customer {
      private String name;
      private String address;
      public Customer() {
         super();
      }
      public Customer(String name, String address) {
         this.name = name;
         this.address = address;
      }
   }
  
   private static class Envelope {
      private final Object value;
      public Envelope(Object value) {
         this.value = value;  
      }
      public Object getValue() {
         return value;
      }
   }
   
   public void testConverter() throws Exception {
      Style style = new CamelCaseStyle();
      Format format = new Format(style);
      Registry registry = new Registry();
      Customer customer = new Customer("Niall", "Some Place");
      Envelope envelope = new Envelope(customer);
      RegistryStrategy strategy = new RegistryStrategy(registry);
      XmlMapper xmlMapper = new Persister(strategy, format);
      Converter converter = new EnvelopeConverter(xmlMapper);
      
      registry.bind(Envelope.class, converter);
      
      OrderItem order = new OrderItem(envelope);
      xmlMapper.write(order, System.out);
   }
   
   public void testPersonConverter() throws Exception {
      Style style = new CamelCaseStyle();
      Format format = new Format(style);
      Registry registry = new Registry();
      Person person = new Person("Niall", 30);
      RegistryStrategy strategy = new RegistryStrategy(registry);
      XmlMapper xmlMapper = new Persister(strategy, format);
      Converter converter = new PersonConverter(xmlMapper);
      StringWriter writer = new StringWriter();
      
      registry.bind(Person.class, converter);
      
      PersonProfile profile = new PersonProfile(person);
      xmlMapper.write(profile, writer);
      
      System.out.println(writer.toString());
      
      PersonProfile read = xmlMapper.read(PersonProfile.class, writer.toString());
      
      assertEquals(read.person.name, "Niall");
      assertEquals(read.person.age, 30);
   }
}
