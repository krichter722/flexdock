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
package org.flexdock.plaf.resources.paint;

import java.awt.Color;

import org.flexdock.plaf.PropertySet;

/**
 * @author Claudio Romano
 */
public class PainterResource extends PropertySet {


    public static final String CLASSNAME = "classname";
    public static final String BACKGROUND_COLOR = "bgcolor";
    public static final String BACKGROUND_COLOR_ACTIVE = "bgcolor.active";


    /**
     * @return Returns the bgColor.
     */
    public Color getBgColor() {
        return getColor( BACKGROUND_COLOR);
    }

    /**
     * @param bgColor The bgColor to set.
     */
    public void setBgColor(Color bgColor) {
        setProperty( BACKGROUND_COLOR, bgColor);
    }

    /**
     * @return Returns the bgColorActiv.
     */
    public Color getBgColorActive() {
        return getColor( BACKGROUND_COLOR_ACTIVE);
    }

    /**
     * @param bgColorActive The bgColorActive to set.
     */
    public void setBgColorActive(Color bgColorActive) {
        setProperty( BACKGROUND_COLOR_ACTIVE, bgColorActive);
    }

    /**
     * @return Returns the painter.
     */
    public String getClassname() {
        return getString( CLASSNAME);
    }
    /**
     * @param painter The painter to set.
     */
    public void setClassname(String painter) {
        setProperty(CLASSNAME, painter);
    }


    public Class getImplClass() {
        String className = getString(CLASSNAME);
        try {
            return resolveClass(getString( CLASSNAME));
        } catch( Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}