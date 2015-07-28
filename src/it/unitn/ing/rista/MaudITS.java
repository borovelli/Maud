/*
 * @(#)MaudITS.java created Nov 29, 2005 Casalino
 *
 * Copyright (c) 1996-2004 Luca Lutterotti All Rights Reserved.
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

package it.unitn.ing.rista;

import it.unitn.ing.rista.awt.*;
import it.unitn.ing.rista.util.*;

import javax.swing.*;
import java.io.IOException;


/**
 * The MaudITS is a class
 * <p/>
 * Description
 *
 * @author Luca Lutterotti
 * @version $Revision: 1.2 $, $Date: 2006/07/20 13:39:02 $
 * @since JDK1.1
 */

public class MaudITS {


  /**
   * Initialize program constants.
   */

  public static JFrame appMainFrame = null;

  static boolean programInitialized = false;
  static boolean swingInitialized = false;
  public static String instrument = Constants.NONE;

  public static void programInitialization() {
    if (programInitialized)
      return;
    programInitialized = true;
    Constants.initConstants();
  }

  /**
    * Initialize interface.
    * Force SwingSet to come up in the System Platform L&F.
    * If you want the Cross platform L&F instead, use instead
    * <cose>UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())</code>
    */

   public static void preSwingInitialization() {
     if (swingInitialized)
       return;
     try {

//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

     } catch (Exception exc1) {
     }

     JPopupMenu.setDefaultLightWeightPopupEnabled(false);

   }

   public static void postSwingInitialization() {
     if (swingInitialized)
       return;
     swingInitialized = true;
     try {

       UIManager.setLookAndFeel((String) MaudPreferences.getPref("swing.defaultL&F"));

     } catch (Exception exc) {

//	    System.out.println("Error loading L&F: " + exc);

       try {

//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

       } catch (Exception exc1) {
         System.out.println("Error loading L&F: " + exc1);
       }

     }
     JPopupMenu.setDefaultLightWeightPopupEnabled(false);

   }

  /**
   * Call the <code>swingInitialization()</code> and start the mainFrame.
   * By default Maud will run interactively starting the interface; if
   * the -t argument is used Maud will run in text only and will process
   * a default "textonly.ins" file where there should be the instructions
   * on what and how to run the analysis. The textonly.ins file is in CIF
   * format and must contain the analysis/es to run and the iterations number.
   *
   @see it.unitn.ing.rista.awt.mainFrame
   */

  public static void main(String args[]) {

//	  System.out.println("Starting Maud program, wait........");

    System.setProperty("apple.laf.useScreenMenuBar","true");
    System.setProperty("apple.awt.use-file-dialog-packages","true");
    System.setProperty("apple.awt.showGrowBox","true");
    Constants.textonly = false;
    boolean reflectivity = false;
    boolean maudette = true;  // now we go
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        if (args[i].equalsIgnoreCase("-t") || args[i].equalsIgnoreCase("-textonly")
                || args[i].equalsIgnoreCase("t") || args[i].equalsIgnoreCase("textonly"))
          Constants.textonly = true;
        else if (args[i].equalsIgnoreCase("-film"))
          reflectivity = true;
        else if (args[i].equalsIgnoreCase("-simple"))
          maudette = true;
        else if (args[i].equalsIgnoreCase("-instrument"))
          instrument = args[++i];
      }
    }
    if (Constants.textonly) {
      String insFileName = new String("textonly.ins");
      for (int i = 0; i < args.length; i++)
        if (args[i].equalsIgnoreCase("-f") || args[i].equalsIgnoreCase("-file")
                || args[i].equalsIgnoreCase("f") || args[i].equalsIgnoreCase("file"))
          if (i + 1 < args.length)
            insFileName = args[i + 1];

      System.out.println("Starting batch mode");
      programInitialization();
      batchProcess batch = new batchProcess(insFileName);
      batch.process();
      System.exit(0);

    }
    initInteractive();
      goInteractive();

  }

  public static void initInteractive() {
//    Constants.ITS = true;
//	    System.out.println("Starting interactive mode");
    preSwingInitialization();
    programInitialization();
//	  System.out.println(instrument);
//    if (instrument.equalsIgnoreCase(Constants.NONE))
//      instrument = MaudPreferences.getPref("Italstructures.instrument", Constants.IS_INSTRUMENTS[0]);
    postSwingInitialization();

  }

  public static void goInteractive() {
//	    System.out.println("Creating mainframe");
    appMainFrame = new JFrame();
    appMainFrame.setTitle("Important notice");
    appMainFrame.setSize(800, 600);
    JPanel apanel;
    appMainFrame.getContentPane().add(apanel = new JPanel());
    JLabel alabel = new JLabel("Ital Structures does not have any right to distribute, sell or provide Maud");
    apanel.add(alabel);
    alabel = new JLabel("in any form or content as any derivative or modification of it!");
    apanel.add(alabel);
    alabel = new JLabel("There is no agreement between the University of Trento and Ital Structures or GNR,");
    apanel.add(alabel);
    alabel = new JLabel("and never there was regarding the program Maud!");
    apanel.add(alabel);
    appMainFrame.setVisible(true);

  }

}
