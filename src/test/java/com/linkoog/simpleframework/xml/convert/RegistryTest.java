package com.linkoog.simpleframework.xml.convert;

import junit.framework.TestCase;

public class RegistryTest extends TestCase {
   
   public void testRegistry() throws Exception {
      Registry registry = new Registry();
      
      registry.bind(ExampleConverters.Cat.class, ExampleConverters.CatConverter.class);
      registry.bind(ExampleConverters.Dog.class, ExampleConverters.DogConverter.class);
      
      assertEquals(registry.lookup(ExampleConverters.Cat.class).getClass(), ExampleConverters.CatConverter.class);
      assertEquals(registry.lookup(ExampleConverters.Dog.class).getClass(), ExampleConverters.DogConverter.class);
      
      Converter cat = registry.lookup(ExampleConverters.Cat.class);
      Converter dog = registry.lookup(ExampleConverters.Dog.class);
      
      assertTrue(cat == registry.lookup(ExampleConverters.Cat.class));
      assertTrue(dog == registry.lookup(ExampleConverters.Dog.class));
   }

}
