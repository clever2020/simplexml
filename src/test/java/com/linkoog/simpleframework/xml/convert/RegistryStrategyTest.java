package com.linkoog.simpleframework.xml.convert;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.ElementList;
import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.core.Persister;
import com.linkoog.simpleframework.xml.strategy.Strategy;
import com.linkoog.simpleframework.xml.util.Dictionary;

public class RegistryStrategyTest extends ValidationTestCase {
   
   @Root
   @Namespace(prefix="a", reference="http://domain/a")
   private static class PetShop {
      @ElementList
      @Namespace(prefix="b", reference="http://domain/b")
      private Dictionary<ExampleConverters.Pet> pets;
      public PetShop(){
         this.pets = new Dictionary<ExampleConverters.Pet>();
      }
      public void addPet(ExampleConverters.Pet pet) {
         pets.add(pet);
      }
      public ExampleConverters.Pet getPet(String name){
         return pets.get(name);
      }
   }
   
   public void testConverter() throws Exception {
      Registry registry = new Registry();
      Strategy interceptor = new RegistryStrategy(registry);
      Persister persister = new Persister(interceptor);
      StringWriter writer = new StringWriter();
      PetShop shop = new PetShop();
      
      registry.bind(ExampleConverters.Dog.class, ExampleConverters.DogConverter.class)
              .bind(ExampleConverters.Cat.class, ExampleConverters.CatConverter.class);
   
      shop.addPet(new ExampleConverters.Dog("Lassie", 10));
      shop.addPet(new ExampleConverters.Cat("Kitty", 2));
      
      persister.write(shop, writer);
      persister.write(shop, System.out);
      
      String text = writer.toString();
      PetShop newShop = persister.read(PetShop.class, text);
      
      assertEquals("Lassie", newShop.getPet("Lassie").getName());
      assertEquals(10, newShop.getPet("Lassie").getAge());
      assertEquals("Kitty", newShop.getPet("Kitty").getName());
      assertEquals(2, newShop.getPet("Kitty").getAge());
      
      assertElementExists(text, "/petShop");
      assertElementExists(text, "/petShop/pets");
      assertElementExists(text, "/petShop/pets/pet[1]");
      assertElementExists(text, "/petShop/pets/pet[2]");
      assertElementDoesNotExist(text, "/petShop/pets/pet[3]");
      assertElementHasNamespace(text, "/petShop", "http://domain/a");
      assertElementHasNamespace(text, "/petShop/pets", "http://domain/b");
      assertElementHasNamespace(text, "/petShop/pets/pet[1]", null);
      assertElementHasAttribute(text, "/petShop/pets/pet[1]", "name", "Lassie");
      assertElementHasAttribute(text, "/petShop/pets/pet[1]", "age", "10");
      assertElementHasValue(text, "/petShop/pets/pet[2]/name", "Kitty");
      assertElementHasValue(text, "/petShop/pets/pet[2]/age", "2");
   }
}
