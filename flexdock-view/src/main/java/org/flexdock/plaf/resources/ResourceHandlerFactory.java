/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.flexdock.plaf.resources;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.flexdock.plaf.Configurator;
import org.flexdock.plaf.PropertySet;
import org.flexdock.plaf.XMLConstants;
import org.w3c.dom.Element;

/**
 * @author Christopher Butler
 */
public class ResourceHandlerFactory implements XMLConstants {

    private static final HashMap RESOURCE_HANDLERS = loadResourceHandlers();
    private static final HashMap PROPERTY_HANDLERS = loadPropertyHandlers();

    public static ResourceHandler getResourceHandler(String handlerName) {
        return (ResourceHandler)RESOURCE_HANDLERS.get(handlerName);
    }

    public static String getPropertyHandler(String propertyType) {
        return (String)PROPERTY_HANDLERS.get(propertyType);
    }

    private static HashMap loadResourceHandlers() {
        HashMap elements = Configurator.getNamedElementsByTagName(HANDLER_KEY);
        HashMap handlers = new HashMap(elements.size());

        for(Iterator it=elements.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            Element elem = (Element)elements.get(key);

            String name = elem.getAttribute(NAME_KEY);
            String className = elem.getAttribute(VALUE_KEY);
            ResourceHandler handler = createResourceHandler(className);
            if(handler!=null) {
                handlers.put(name, handler);
            }
        }
        // add constructor handlers to the set
        HashMap constructors = loadConstructors();
        handlers.putAll(constructors);

        return handlers;
    }

    private static ResourceHandler createResourceHandler(String className) {
        if(Configurator.isNull(className)) {
            return null;
        }

        try {
            Class clazz = Class.forName(className);
            return (ResourceHandler)clazz.newInstance();
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static HashMap loadPropertyHandlers() {
        HashMap elements = Configurator.getNamedElementsByTagName(PROP_HANDLER_KEY);
        HashMap propHandlers = new HashMap(elements.size());

        for(Iterator it=elements.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            Element elem = (Element)elements.get(key);

            String tagName = elem.getAttribute(NAME_KEY);
            String handlerName = elem.getAttribute(VALUE_KEY);
            if(!Configurator.isNull(tagName) && !Configurator.isNull(handlerName)) {
                propHandlers.put(tagName, handlerName);
            }
        }
        return propHandlers;
    }

    private static HashMap loadConstructors() {
        PropertySet[] constructors = Configurator.getProperties(CONSTRUCTOR_KEY);
        HashMap map = new HashMap(constructors.length);

        for(int i=0; i<constructors.length; i++) {
            ConstructorHandler handler = createConstructorHandler(constructors[i]);
            if(handler!=null) {
                map.put(constructors[i].getName(), handler);
            }
        }
        return map;
    }

    private static ConstructorHandler createConstructorHandler(PropertySet props) {
        String className = props.getString(CLASSNAME_KEY);
        if(Configurator.isNull(className)) {
            return null;
        }

        try {
            List argKeys = props.getNumericKeys(true);
            ArrayList params = new ArrayList(argKeys.size());
            for(Iterator it=argKeys.iterator(); it.hasNext();) {
                String key = (String)it.next();
                Class paramType = props.toClass(key);
                if(!paramType.isPrimitive() && paramType!=String.class) {
                    throw new IllegalArgumentException("ConstructorHandler can only parse primitive and String arguments: " + paramType);
                }
                params.add(paramType);
            }

            Class type = Class.forName(className);
            Class[] paramTypes = (Class[])params.toArray(new Class[0]);
            Constructor constructor = type.getConstructor(paramTypes);

            return new ConstructorHandler(constructor);

        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
