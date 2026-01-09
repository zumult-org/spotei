/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.prefs.Preferences;

/**
 *
 * @author bernd
 */
public class XSLTHelperFactory {
    
    public static XSLTHelper newXSLTHelper() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        String preferencesClassName = Preferences.userRoot().get("de.linguisticbits.spotei.XSLTHelper", "de.linguisticbits.spotei.utils.Saxon12XSLTHelper");
                                                                                    
        return (XSLTHelper) Class.forName(preferencesClassName).getConstructor().newInstance();
    }
    
}
