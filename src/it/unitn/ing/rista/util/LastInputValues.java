/*
 * @(#)LastInputValues.java created 5/07/2006 Casalino
 *
 * Copyright (c) 2006 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is
 * provided as it is as confidential and proprietary information.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Luca Lutterotti.
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
 *  The LastInputValues is a class to manage the Maud last inputs.
 *
 * @version $Revision: 1.1 $, $Date: 2006/07/20 14:06:04 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */


public class LastInputValues extends PreferencesInterface {

  public static SortedProperties prefs = null;

  public LastInputValues() {
  }

  public static void loadPreferences() {

    InputStream preferencesFile = Misc.getInputStream(Constants.filesfolder, "lastInput.Maud");
    prefs = new SortedProperties();
    // Default values

    if (preferencesFile != null) {
      try {
        prefs.load(preferencesFile);
//        if (Constants.testing)
//          prefs.list(System.out);
      } catch (Exception e) {
        System.out.println("Last input file not found (first run?), a new one will be created on exit");
//				e.printStackTrace();
      }
    }
  }

  public Object getValue(String key) {
    return getPref(key);
  }

  public void setValue(String key, Object value) {
    setPref(key, value);
  }

  public static Object getPref(String key) {
    return prefs.getProperty(key);
  }

  public static String getPref(String key, String defaultValue) {
    if (prefs.getProperty(key) == null) {
      addPref(key, defaultValue);
      if (Constants.testing)
        System.out.println("Adding last input value: " + key + ", value: " + defaultValue);
    }
    return (String) getPref(key);
  }

  public static void setPref(String key, Object value) {
    setPref(key, (String) value);
  }

  public static void setPref(String key, String value) {
    prefs.setProperty(key, value);
  }

  public static void setPref(String key, int intvalue) {
    prefs.setProperty(key, Integer.toString(intvalue));
  }

  public static void setPref(String key, double value) {
    prefs.setProperty(key, Double.toString(value));
  }

  public static void setPref(String key, boolean intvalue) {
    String value = "false";
    if (intvalue)
      value = "true";
    prefs.setProperty(key, value);
  }

  public static void addPref(String key, String value) {
    prefs.setProperty(key, value);
  }

  public static int getInteger(String key) {
    return Integer.valueOf((String) getPref(key)).intValue();
  }

  public static int getInteger(String key, String defaultValue) {
    return Integer.valueOf(getPref(key, defaultValue)).intValue();
  }

  public static int getInteger(String key, int defaultValue) {
    return getInteger(key, Integer.toString(defaultValue));
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

  public static boolean getBoolean(String key) {
    String tmp = (String) getPref(key);
    if (tmp.equalsIgnoreCase("true"))
      return true;
    else
      return false;
  }

  public static boolean getBoolean(String key, String defaultValue) {
    String tmp = getPref(key, defaultValue);
    if (tmp.equalsIgnoreCase("true"))
      return true;
    else
      return false;
  }

  public static boolean getBoolean(String key, boolean defaultValue) {
    String value = "true";
    if (!defaultValue)
      value = "false";
    return getBoolean(key, value);
  }

  public static void savePreferences() {
//    if (Constants.webStart)
//      return;
    FileOutputStream preferencesFile = Misc.getFileOutputStream(Constants.filesfolder, "lastInput.Maud");
    if (preferencesFile != null)
      try {
        prefs.store(preferencesFile, " Last input values for Maud, version " + Constants.getVersion());
      } catch (IOException io) {

      }
  }
}
