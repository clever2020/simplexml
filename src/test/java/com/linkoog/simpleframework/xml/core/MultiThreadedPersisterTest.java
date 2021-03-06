package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import com.linkoog.simpleframework.xml.annotations.Default;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import junit.framework.Assert;
import junit.framework.TestCase;

public class MultiThreadedPersisterTest extends TestCase {
   
   @Root
   @Default
   private static class Example {
      private String name;
      private String value;
      private int number;
      private Date date;
      private Locale locale;
   }
   
   private static enum Status {
      ERROR,
      SUCCESS
   }
   
   private static class Worker extends Thread {
      private final CountDownLatch latch;
      private final XmlMapper xmlMapper;
      private final Queue<Status> queue;
      private final Example example;
      public Worker(CountDownLatch latch, XmlMapper xmlMapper, Queue<Status> queue, Example example) {
         this.xmlMapper = xmlMapper;
         this.example = example;
         this.latch = latch;
         this.queue = queue;
      }
      public void run() {
         try {
            latch.countDown();
            latch.await();
            for(int i = 0; i < 100; i++) {
               StringWriter writer = new StringWriter();
               xmlMapper.write(example, writer);
               String text = writer.toString();
               Example copy = xmlMapper.read(Example.class, text);
               Assert.assertEquals(example.name, copy.name);
               Assert.assertEquals(example.value, copy.value);
               Assert.assertEquals(example.number, copy.number);
               Assert.assertEquals(example.date, copy.date);
               Assert.assertEquals(example.locale, copy.locale);
               System.out.println(text);
            }
            queue.offer(Status.SUCCESS);
         }catch(Exception e) {
            e.printStackTrace();
            queue.offer(Status.ERROR);
         }   
      }
   }
   
   public void testConcurrency() throws Exception {
      Persister persister = new Persister();
      CountDownLatch latch = new CountDownLatch(20);
      BlockingQueue<Status> status = new LinkedBlockingQueue<Status>();
      Example example = new Example();
      
      example.name = "Eample Name";
      example.value = "Some Value";
      example.number = 10;
      example.date = new Date();
      example.locale = Locale.UK;
      
      for(int i = 0; i < 20; i++) {
         Worker worker = new Worker(latch, persister, status, example);
         worker.start();
      }
      for(int i = 0; i < 20; i++) {
         assertEquals("Serialization fails when used concurrently", status.take(), Status.SUCCESS);
      }
   }

}
