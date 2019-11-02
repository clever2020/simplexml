/*
 * InvalidFormatException.java May 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.linkoog.simpleframework.xml.transform;

/**
 * The <code>InvalidFormatException</code> is thrown when there is 
 * a format exception. This exception this will be thrown from the
 * <code>Transformer</code> should serialization or deserialization
 * of an object fail. Error messages provided to this exception are
 * formatted similar to the <code>PrintStream.printf</code> method.
 * 
 * @author Niall Gallagher
 */
public class InvalidFormatException extends TransformException {
   
   /**
    * Constructor for the <code>InvalidFormatException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */   
   public InvalidFormatException(String text, Object... list) {
      super(String.format(text, list));               
   }       

   /**
    * Constructor for the <code>InvalidFormatException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the stri 
    */
   public InvalidFormatException(Throwable cause, String text, Object... list) {
      super(String.format(text, list), cause);           
   }  
}