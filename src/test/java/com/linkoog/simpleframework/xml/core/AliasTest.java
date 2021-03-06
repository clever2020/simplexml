package com.linkoog.simpleframework.xml.core;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.linkoog.simpleframework.xml.ValidationTestCase;
import com.linkoog.simpleframework.xml.annotations.Attribute;
import com.linkoog.simpleframework.xml.annotations.Element;
import com.linkoog.simpleframework.xml.annotations.ElementMap;
import com.linkoog.simpleframework.xml.annotations.Namespace;
import com.linkoog.simpleframework.xml.annotations.Root;
import com.linkoog.simpleframework.xml.strategy.Type;
import com.linkoog.simpleframework.xml.strategy.Strategy;
import com.linkoog.simpleframework.xml.strategy.TreeStrategy;
import com.linkoog.simpleframework.xml.strategy.Value;
import com.linkoog.simpleframework.xml.stream.InputNode;
import com.linkoog.simpleframework.xml.stream.Node;
import com.linkoog.simpleframework.xml.stream.NodeMap;
import com.linkoog.simpleframework.xml.stream.OutputNode;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class AliasTest extends ValidationTestCase {
    
    public class AliasStrategy implements Strategy {
        
        private final Strategy strategy;
        private final Map<Class, String> forward;
        private final Map<String, Class> backward;
        
        private AliasStrategy(Strategy strategy) {
            this.forward = new ConcurrentHashMap<Class, String>();
            this.backward = new  ConcurrentHashMap<String, Class>(); 
            this.strategy = strategy;
        }
        
        public void addAlias(Class type, String name) {
            forward.put(type, name);
            backward.put(name, type);
        }
        
        public Value read(Type field, NodeMap<InputNode> node, Map map) throws Exception {
            Node entry = node.remove("type");
            
            if(entry != null) {
                String value = entry.getValue();
                Class type = backward.get(value);
                
                if(type == null) {
                    throw new PersistenceException("Could not find class for alias %s", value);
                }
                node.put("class", type.getName());
            }
            return strategy.read(field, node, map);
        }
        
        public boolean write(Type field, Object value, NodeMap<OutputNode> node, Map map) throws Exception {
            boolean done = strategy.write(field, value, node, map);
            Node entry = node.remove("class");
            
            if(entry != null) {
                String className = entry.getValue();
                Class type = Class.forName(className);
                String name = forward.get(type);

                if(name == null) {
                    throw new PersistenceException("Could not find alias for class %s", className);
                }
                node.put("type", name);
            }
            return done;
        }      
    }
    
    @Root
    @Namespace(prefix="table", reference="http://simpleframework.org/map")
    private static class MultiValueMap {
        
        @ElementMap        
        private Map<String, Object> map;
        
        public MultiValueMap() {
            this.map = new HashMap<String, Object>();
        }
        
        public void add(String name, Object value) {
            map.put(name, value);
        }
        
        public Object get(String name) {
            return map.get(name);
        }
    }
    
    @Root
    @Namespace(prefix="item", reference="http://simpleframework.org/entry")
    private static class MultiValueEntry {
        
        @Attribute(name="name")
        private String name;
        
        @Element(name="value")
        private String value;
        
        public MultiValueEntry(@Attribute(name="name") String name,
                                @Element(name="value") String value) {
            this.name = name;
            this.value = value;            
        }
    }
    
    public void testMap() throws Exception {
        Strategy strategy = new TreeStrategy();
        AliasStrategy alias = new AliasStrategy(strategy);
        Persister persister = new Persister(alias);
        MultiValueMap map = new MultiValueMap();
        
        alias.addAlias(HashMap.class, "map");
        alias.addAlias(Integer.class, "int");
        alias.addAlias(Double.class, "float");
        alias.addAlias(String.class, "text");
        alias.addAlias(MultiValueEntry.class, "item");
        
        map.add("integer", 1);
        map.add("double", 0.0d);
        map.add("string", "test");
        map.add("item", new MultiValueEntry("example", "item"));
        
        StringWriter out = new StringWriter();
        persister.write(map, out);
        String text = out.toString();//.replaceAll("entry", "table:entry");
        System.err.println(text);
        
        MultiValueMap read = persister.read(MultiValueMap.class, text);
        
        assertEquals(read.get("integer"), 1);
        assertEquals(read.get("double"), 0.0d);
        assertEquals(read.get("string"), "test");
        
        validate(persister, map);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // Ensure we know about namespaces
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader reader = new StringReader(text);
        InputSource source = new InputSource(reader);        
        Document doc = builder.parse(source);        
        org.w3c.dom.Element element = doc.getDocumentElement();
        
        assertEquals("multiValueMap", element.getLocalName());
        assertEquals("http://simpleframework.org/map", element.getNamespaceURI());
    }
}
