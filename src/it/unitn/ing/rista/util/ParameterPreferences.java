/*
 * @(#)ParameterPreferences.java created Mar 28, 2003 Berkeley
 *
 * Copyright (c) 1996-2003 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is 
 * provided as it is as confidential and proprietary information.  
 * You shall not disclose such Confidential Information and shall use 
 * it only in accordance with the terms of the license agreement you 
 * entered into with the author.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package it.unitn.ing.rista.util;

import it.unitn.ing.rista.interfaces.PreferencesInterface;

import java.io.*;


/**
 * The ParameterPreferences is a class
 *  
 * @version $Revision: 1.5 $, $Date: 2006/01/19 14:45:59 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */

public class ParameterPreferences extends PreferencesInterface {

  public static SortedProperties prefs = null;

  public ParameterPreferences() {
  }

  public static void loadPreferences() {

    InputStream preferencesFile = Misc.getInputStream(Constants.filesfolder, "preferences.Parameters");
    prefs = new SortedProperties();
    // Default values

    if (preferencesFile != null) {
      try {
        prefs.load(preferencesFile);
//        if (Constants.testing)
//          prefs.list(System.out);
      } catch (Exception e) {
        System.out.println("Parameter preferences file not found (first run?), a new one will be created on exit");
//				e.printStackTrace();
      }
    }

  }

  public Object getValue(String key) {
    return ParameterPreferences.getPref(key);
  }

  public void setValue(String key, Object value) {
    ParameterPreferences.setPref(key, value);
  }

  public static Object getPref(String key) {
    return prefs.getProperty(key);
  }

  public static String getPref(String key, String defaultValue) {
    if (prefs.getProperty(key) == null) {
      addPref(key, defaultValue);
      if (Constants.testing)
        System.out.println("Adding preference: " + key + ", value: " + defaultValue);
    }
    return (String) getPref(key);
  }

  public static void setPref(String key, Object value) {
    setPref(key, (String) value);
  }

  public static void setPref(String key, String value) {
    prefs.setProperty(key, value);
  }

  public static void setPref(String key, double value) {
    prefs.setProperty(key, Fmt.format(value));
  }

  public static void addPref(String key, String value) {
    prefs.setProperty(key, value);
  }

  public static double getDouble(String key) {
    return Double.valueOf((String) getPref(key)).doubleValue();
  }

  public static double getDouble(String key, String defaultValue) {
    return Double.valueOf(getPref(key, defaultValue)).doubleValue();
  }

  public static double getDouble(String key, double defaultValue) {
    return getDouble(key, Double.toString(defaultValue));
  }

  public static void savePreferences() {
    FileOutputStream preferencesFile = Misc.getFileOutputStream(Constants.filesfolder, "preferences.Parameters");
    if (preferencesFile != null)
      try {
        prefs.store(preferencesFile, " Parameter preferences, version " + Constants.getVersion());
      } catch (IOException e) {
      }
  }

}
