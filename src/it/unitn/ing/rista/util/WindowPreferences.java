/*
 * @(#)WindowPreferences.java created 02/09/2001 Riva Del Garda
 *
 * Copyright (c) 2001 Luca Lutterotti All Rights Reserved.
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

import java.io.*;

/**
 *  The WindowPreferences is a class to manage the Windows preferences.
 *
 * @version $Revision: 1.4 $, $Date: 2003/04/07 06:46:06 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */


public class WindowPreferences {

  static SortedProperties prefs = null;

  private WindowPreferences() {
  }

  public static void loadPreferences() {

    InputStream preferencesFile = Misc.getInputStream(Constants.filesfolder, "preferences.Window");
    prefs = new SortedProperties();

    if (preferencesFile != null) {
      try {
        prefs.load(preferencesFile);
      } catch (Exception e) {
      }
    }

  }

  public static void savePreferences() {
    FileOutputStream preferencesFile = Misc.getFileOutputStream(Constants.filesfolder, "preferences.Window");
    if (preferencesFile != null)
    try {
      prefs.store(preferencesFile, " Window preferences, version " + Constants.getVersion());
    } catch (IOException io) {

    }
  }

  public static Object getPref(String key) {
    return prefs.getProperty(key);
  }

  public static String getPref(String key, String defaultValue) {
    if (prefs.getProperty(key) == null) {
      addPref(key, defaultValue);
    }
    return (String) getPref(key);
  }

  public static void setPref(String key, String value) {
    prefs.setProperty(key, value);
  }

  public static void setPref(String key, int intvalue) {
    prefs.setProperty(key, Integer.toString(intvalue));
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

}
