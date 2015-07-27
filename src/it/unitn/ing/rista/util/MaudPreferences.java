/*
 * @(#)MaudPreferences.java created 25/04/2000 Casalino
 *
 * Copyright (c) 2000 Luca Lutterotti All Rights Reserved.
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

import javax.swing.*;
import java.io.*;

/**
 *  The MaudPreferences is a class to manage the Maud preferences.
 *
 * @version $Revision: 1.6 $, $Date: 2006/01/19 14:45:59 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */


public class MaudPreferences extends PreferencesInterface {

  public static String analysisFile = "analysis.last";
  public static String analysisPath = "analysis.path";
  public static String datafilePath = "datafile.path";
  public static String databasePath = "database.path";
  public static String iterations = "analysis.iterations";
  public static String removeConfirm = "remove.confirm";
  public static String swingLF = "swing.defaultL&F";
  public static String showTooltip = "tooltip.show";
  public static String plotScale = "plot.scale";
  public static String showProgressFrame = "shows.floatingProgressWindow";
  public static SortedProperties prefs = null;
//	public static String speedupComp = "reflection.update";

  public MaudPreferences() {
  }

  public static void loadPreferences() {

    InputStream preferencesFile = Misc.getInputStream(Constants.filesfolder, "preferences.Maud");
    prefs = new SortedProperties();
    // Default values

    if (preferencesFile != null) {
      try {
        prefs.load(preferencesFile);
//        if (Constants.testing)
//          prefs.list(System.out);
      } catch (Exception e) {
//        System.out.println("Preferences file not found (first run?), a new one will be created on exit");
//				e.printStackTrace();
      }
    }
    // default values
    getPref(analysisFile, "default.par");
    getPref(analysisPath, Constants.filesfolder);
    getPref(datafilePath, Constants.filesfolder);
    getPref(databasePath, Constants.filesfolder);
    getPref(iterations, "3");
    getPref(removeConfirm, "true");
    getPref(showProgressFrame, "true");
  }

  public Object getValue(String key) {
    return MaudPreferences.getPref(key);
  }

  public void setValue(String key, Object value) {
    MaudPreferences.setPref(key, value);
  }

  public static Object getPref(String key) {
    return prefs.getProperty(key);
  }

  public static boolean isPresent(String key) {
    return (prefs.getProperty(key) != null);
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

  public static void setPref(String key, int intvalue) {
    prefs.setProperty(key, Integer.toString(intvalue));
  }

	public static void setPref(String key, long longvalue) {
		prefs.setProperty(key, Long.toString(longvalue));
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

	public static long getLong(String key) {
		return Long.valueOf((String) getPref(key)).longValue();
	}

	public static long getLong(String key, String defaultValue) {
		return Long.valueOf(getPref(key, defaultValue)).longValue();
	}

	public static long getLong(String key, long defaultValue) {
		return getLong(key, Long.toString(defaultValue));
	}

	public static boolean getBoolean(String key) {
    String tmp = (String) getPref(key);
    if (tmp != null && tmp.equalsIgnoreCase("true"))
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
    FileOutputStream preferencesFile = Misc.getFileOutputStream(Constants.filesfolder, "preferences.Maud");
    if (preferencesFile != null)
      try {
        prefs.store(preferencesFile, " Maud preferences, version " + Constants.getVersion());
      } catch (IOException io) {

      }
  }
}
