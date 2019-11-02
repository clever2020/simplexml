package com.linkoog.simpleframework.xml.convert;

import java.util.ArrayList;
import java.util.List;

import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.DefaultType;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.strategy.CycleStrategy;
import com.linkoog.simpleframework.xml.stream.CamelCaseStyle;
import com.linkoog.simpleframework.xml.stream.Format;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.OutputNode;
import com.linkoog.simpleframework.xml.stream.Style;
import junit.framework.TestCase;

public class RegistryConverterCycleTest extends TestCase {

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
            super(person.address, person.name, person.age);
         }
         public Address getAddress() {
            return address;
         }
         public void setAddress(Address address) {
            this.address = address;
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
   @Default
   public static class Club {
      private Address address;
      private List<Person> members;
      public Club() {
         super();
      }
      public Club(Address address) {
         this.members = new ArrayList<Person>();
         this.address = address;
      }
      public void addMember(Person person) {
         this.members.add(person);
      }
   }
   
   private static class Person {
      protected Address address;
      protected String name;
      protected int age;
      public Person() {
         super();
      }
      public Person(Address address, String name, int age) {
         this.address = address;
         this.name= name;
         this.age = age;
      }      
   }
   
   @Root
   @Default
   private static class Address {
      private String address;
      public Address() {
         super();
      }
      public Address(String address) {
         this.address = address;
      }
   }
   
   public void testCycle() throws Exception {
      Style style = new CamelCaseStyle();
      Format format = new Format(style);
      Registry registry = new Registry();
      Address address = new Address("An Address");
      Person person = new Person(address, "Niall", 30);
      CycleStrategy referencer = new CycleStrategy();
      RegistryStrategy strategy = new RegistryStrategy(registry, referencer);
      XmlMapper xmlMapper = new Persister(strategy, format);
      Converter converter = new PersonConverter(xmlMapper);
      Club club = new Club(address);
      
      club.addMember(person);
      registry.bind(Person.class, converter);
      
      xmlMapper.write(club, System.out);
   }
}
