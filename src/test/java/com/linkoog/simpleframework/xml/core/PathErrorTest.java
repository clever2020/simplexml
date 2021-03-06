package com.linkoog.simpleframework.xml.core;

import java.io.StringWriter;

import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Path;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.XmlMapper;
import junit.framework.TestCase;

public class PathErrorTest extends TestCase{

   @Root(name="header")
   public static class TradeHeader {

      @Element(name = "sourceSystem")
      @Path("tradeIdentifier/tradeKey")
      @Namespace
      private String tradeKeySourceSystem = "VALUE";

      @Attribute(name = "id")
      @Path("book")
      private String bookId = "BOOK";

      @Element(name = "code")
      @Path("book/identifier")
      @Namespace
      private String bookCode = "code";

      @Element(name = "sourceSystem")
      @Path("book/identifier")
      @Namespace
      private String bookSourceSystem = "VALUE";

      @Element(name = "type")
      @Path("book/identifier")
      @Namespace
      private String bookType = "SHORT_NAME";

      @Element(name = "role")
      @Path("book")
      @Namespace
      private String bookRole = "VALUE";

      @Attribute(name = "id")
      @Path("trader")
      private String traderId = "TID";

      @Element(name = "code")
      @Path("trader/identifier")
      @Namespace
      private String traderCode = "tCode";

      @Element(name = "sourceSystem")
      @Path("trader/identifier")
      @Namespace
      private String traderSourceSystem = "VALUE";

      @Element(name = "type")
      @Path("trader/identifier")
      @Namespace
      private String traderType = "SHORT_NAME";

      @Element(name = "role")
      @Path("trader")
      @Namespace
      private String traderRole = "VALUE";
   }
   
   public void testRepeat() throws Exception {
      XmlMapper xmlMapper = new Persister();
      StringWriter writer = new StringWriter();
      TradeHeader header = new TradeHeader();
      xmlMapper.write(header, writer);

      String data = writer.getBuffer().toString();
      
      System.out.println(data);
   }
}
