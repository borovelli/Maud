/*
 * @(#)MaudApp.java created 11/02/2002 Mesiano
 *
 * Copyright (c) 2002 Luca Lutterotti All Rights Reserved.
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

import com.apple.eawt.*;
// import com.apple.cocoa.application.*;
// import com.apple.cocoa.foundation.*;
import it.unitn.ing.rista.awt.*;
import it.unitn.ing.rista.util.*;

import javax.swing.*;
import java.io.IOException;

/**
 * The MaudApp is a basic class to
 * startup the Maud program as an application.
 * It performs constant and interface (swing) initialization and launch
 * the principal frame mainFrame.
 *
 * @version $Revision: 1.9 $, $Date: 2006/07/20 13:39:02 $
 * @author Luca Lutterotti
 * @since JDK1.1
 */

public class MaudApp extends Maud {

  public static void main(String args[]) {

//	  System.out.println("Starting Maud program, wait........");

    System.setProperty("apple.laf.useScreenMenuBar","true");
    System.setProperty("apple.awt.use-file-dialog-packages","true");
    System.setProperty("apple.awt.showGrowBox","true");
//    System.setProperty("apple.awt.brushMetalLook", "true");
    System.setProperty("apple.awt.graphics.UseQuartz", "true");  //todo only for Leopard initially as the Sun rendering is slow
    Constants.textonly = false;
    boolean reflectivity = false;
    boolean maudette = false;  // now we go
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        if (args[i].equalsIgnoreCase("-t") || args[i].equalsIgnoreCase("-textonly")
                || args[i].equalsIgnoreCase("t") || args[i].equalsIgnoreCase("textonly"))
          Constants.textonly = true;
        else if (args[i].equalsIgnoreCase("-film"))
          reflectivity = true;
        else if (args[i].equalsIgnoreCase("-simple"))
          maudette = true;
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

    } else if (reflectivity) {
      initInteractive();
      goReflectivityInteractive();
    } else if (maudette) {
      initInteractive();
      goDiffractionInteractive();
    } else {
      initInteractive();
      goInteractive();
    }
  }

  public static void goDiffractionInteractive() {

    appMainFrame = new DiffractionMainFrame(true);

    initMac();

    if (!appMainFrame.initDone())
      appMainFrame.initMainFrame(false, null, "Diffraction Screamer");

//    appMainFrame.setVisible(true);

  }

  public static void goInteractive() {

    appMainFrame = new DiffractionMainFrame(false);

    initMac();

    if (!appMainFrame.initDone())
      appMainFrame.initMainFrame(false, null);

/*    NSApplication app  = NSApplication.sharedApplication();
    NSArray array = app.windows();
    for (int i = 0; i < array.count(); i++) {
      NSWindow window = (NSWindow) array.objectAtIndex(i);
      System.out.println(window);
      if (window.isVisible()) {
        System.out.println("isMainWindow: " + window.isMainWindow());
//        window.makeMainWindow();
      }
    }
    NSWindow window = app.mainWindow();
    window.setTitle("Title set by cocoa");
//    System.out.println(window.toolbar());
//    Object wcntl = window.windowController();
//    System.out.println(wcntl);
//    NSWindowController wcntl = (NSWindowController) window.windowController();
//    System.out.println(window.hasShadow());
//    System.out.println(window);
    NSToolbar toolbar = new NSToolbar("Queue Toolbar");
    toolbar.setDelegate(new CDWindowController());
    toolbar.setAllowsUserCustomization(false);
    toolbar.setAutosavesConfiguration(false);
    window.setToolbar(toolbar);
    window.setShowsToolbarButton(true);
*/
//    appMainFrame.setVisible(true);

  }

  public static void initMac() {
    helpOwnHandler = false;
    Application app = Application.getApplication();
    if (!app.isAboutMenuItemPresent())
      app.addAboutMenuItem();
    if (!app.isPreferencesMenuItemPresent())
      app.addPreferencesMenuItem();
    app.setEnabledAboutMenu(true);
    app.setEnabledPreferencesMenu(true);
    // Install the application listener
    app.addApplicationListener(new ApplicationListener() {

      public void handleAbout(ApplicationEvent applicationEvent) {
        appMainFrame.aboutHelp_Action();
        applicationEvent.setHandled(true);
      }

      public void handleOpenApplication(ApplicationEvent applicationEvent) {
        //To change body of implemented methods use Options | File Templates.
      }

      public void handleOpenFile(ApplicationEvent applicationEvent) {
        if (!appMainFrame.initDone())
          appMainFrame.initMainFrame(false, applicationEvent.getFilename());
        else
          appMainFrame.openParameterFile(Misc.getFolderandName(applicationEvent.getFilename()), null);
        applicationEvent.setHandled(true);
      }

      public void handlePreferences(ApplicationEvent applicationEvent) {
        Utility.showPrefs(appMainFrame);
        applicationEvent.setHandled(true);
      }

      public void handlePrintFile(ApplicationEvent applicationEvent) {
        //To change body of implemented methods use Options | File Templates.
      }

      public void handleQuit(ApplicationEvent applicationEvent) {
        appMainFrame.myFrame_WindowClosing();
      }

      public void handleReOpenApplication(ApplicationEvent applicationEvent) {
        //To change body of implemented methods use Options | File Templates.
      }
    });

  }

}
/*
class CDWindowController extends NSWindowController
        implements NSToolbarItem.ItemValidation {


    private static final String TOOLBAR_RESUME = "Resume";
    private static final String TOOLBAR_RELOAD = "Reload";
    private static final String TOOLBAR_STOP = "Stop";
    private static final String TOOLBAR_REMOVE = "Remove";
    private static final String TOOLBAR_CLEAN_UP = "Clean Up";
    private static final String TOOLBAR_OPEN = "Open";
    private static final String TOOLBAR_SHOW = "Show";

    public NSToolbarItem toolbarItemForItemIdentifier(NSToolbar toolbar, String itemIdentifier, boolean flag) {
        NSToolbarItem item = new NSToolbarItem(itemIdentifier);
        if(itemIdentifier.equals(TOOLBAR_STOP)) {
            item.setLabel(NSBundle.localizedString(TOOLBAR_STOP, ""));
            item.setPaletteLabel(NSBundle.localizedString(TOOLBAR_STOP, ""));
            item.setToolTip(NSBundle.localizedString(TOOLBAR_STOP, ""));
            item.setImage(NSImage.imageNamed("stop.tiff"));
            item.setTarget(this);
            item.setAction(new NSSelector("stopButtonClicked", new Class[]{Object.class}));
            return item;
        }
        if(itemIdentifier.equals(TOOLBAR_RESUME)) {
            item.setLabel(NSBundle.localizedString(TOOLBAR_RESUME, ""));
            item.setPaletteLabel(NSBundle.localizedString(TOOLBAR_RESUME, ""));
            item.setToolTip(NSBundle.localizedString(TOOLBAR_RESUME, ""));
            item.setImage(NSImage.imageNamed("resume.tiff"));
            item.setTarget(this);
            item.setAction(new NSSelector("resumeButtonClicked", new Class[]{Object.class}));
            return item;
        }
        if(itemIdentifier.equals(TOOLBAR_RELOAD)) {
            item.setLabel(NSBundle.localizedString(TOOLBAR_RELOAD, ""));
            item.setPaletteLabel(NSBundle.localizedString(TOOLBAR_RELOAD, ""));
            item.setToolTip(NSBundle.localizedString(TOOLBAR_RELOAD, ""));
            item.setImage(NSImage.imageNamed("reload.tiff"));
            item.setTarget(this);
            item.setAction(new NSSelector("reloadButtonClicked", new Class[]{Object.class}));
            return item;
        }
        if(itemIdentifier.equals(TOOLBAR_SHOW)) {
            item.setLabel(NSBundle.localizedString(TOOLBAR_SHOW, ""));
            item.setPaletteLabel(NSBundle.localizedString("Show in Finder", ""));
            item.setToolTip(NSBundle.localizedString("Show in Finder", ""));
            item.setImage(NSImage.imageNamed("reveal.tiff"));
            item.setTarget(this);
            item.setAction(new NSSelector("revealButtonClicked", new Class[]{Object.class}));
            return item;
        }
        if(itemIdentifier.equals(TOOLBAR_OPEN)) {
            item.setLabel(NSBundle.localizedString(TOOLBAR_OPEN, ""));
            item.setPaletteLabel(NSBundle.localizedString(TOOLBAR_OPEN, ""));
            item.setToolTip(NSBundle.localizedString(TOOLBAR_OPEN, ""));
            item.setImage(NSImage.imageNamed("open.tiff"));
            item.setTarget(this);
            item.setAction(new NSSelector("openButtonClicked", new Class[]{Object.class}));
            return item;
        }
        if(itemIdentifier.equals(TOOLBAR_REMOVE)) {
            item.setLabel(NSBundle.localizedString(TOOLBAR_REMOVE, ""));
            item.setPaletteLabel(NSBundle.localizedString(TOOLBAR_REMOVE, ""));
            item.setToolTip(NSBundle.localizedString(TOOLBAR_REMOVE, ""));
            item.setImage(NSImage.imageNamed("clean.tiff"));
            item.setTarget(this);
            item.setAction(new NSSelector("deleteButtonClicked", new Class[]{Object.class}));
            return item;
        }
        if(itemIdentifier.equals(TOOLBAR_CLEAN_UP)) {
            item.setLabel(NSBundle.localizedString(TOOLBAR_CLEAN_UP, ""));
            item.setPaletteLabel(NSBundle.localizedString(TOOLBAR_CLEAN_UP, ""));
            item.setToolTip(NSBundle.localizedString(TOOLBAR_CLEAN_UP, ""));
            item.setImage(NSImage.imageNamed("cleanAll.tiff"));
            item.setTarget(this);
            item.setAction(new NSSelector("clearButtonClicked", new Class[]{Object.class}));
            return item;
        }
        // itemIdent refered to a toolbar item that is not provide or supported by us or cocoa.
        // Returning null will inform the toolbar this kind of item is not supported.
        return null;
    }

    public void paste(final Object sender) {
    }

    public void stopButtonClicked(final Object sender) {
    }

    public void stopAllButtonClicked(final Object sender) {
    }

    public void resumeButtonClicked(final Object sender) {
    }

    public void reloadButtonClicked(final Object sender) {
    }

    public void openButtonClicked(final Object sender) {
    }

    public void revealButtonClicked(final Object sender) {
    }

    public void deleteButtonClicked(final Object sender) {
    }

    public void clearButtonClicked(final Object sender) {
    }

    public NSArray toolbarDefaultItemIdentifiers(NSToolbar toolbar) {
        return new NSArray(new Object[]{
                TOOLBAR_RESUME,
                TOOLBAR_RELOAD,
                TOOLBAR_STOP,
                TOOLBAR_REMOVE,
                TOOLBAR_CLEAN_UP,
                NSToolbarItem.FlexibleSpaceItemIdentifier,
                TOOLBAR_OPEN,
                TOOLBAR_SHOW
        });
    }

    public NSArray toolbarAllowedItemIdentifiers(NSToolbar toolbar) {
        return new NSArray(new Object[]{
                TOOLBAR_RESUME,
                TOOLBAR_RELOAD,
                TOOLBAR_STOP,
                TOOLBAR_REMOVE,
                TOOLBAR_CLEAN_UP,
                TOOLBAR_SHOW,
                TOOLBAR_OPEN,
                NSToolbarItem.CustomizeToolbarItemIdentifier,
                NSToolbarItem.SpaceItemIdentifier,
                NSToolbarItem.SeparatorItemIdentifier,
                NSToolbarItem.FlexibleSpaceItemIdentifier
        });
    }

    public boolean validateMenuItem(NSMenuItem item) {
        String identifier = item.action().name();
        if(item.action().name().equals("paste:")) {
            boolean valid = false;
            if(!valid) {
                item.setTitle(NSBundle.localizedString("Paste", "Menu item"));
            }
        }
        return this.validateItem(identifier);
    }

    public boolean validateToolbarItem(NSToolbarItem item) {
        return this.validateItem(item.action().name());
    }

    private boolean validateItem(String identifier) {
        if(identifier.equals("paste:")) {
            return false;
        }
        if(identifier.equals("stopButtonClicked:")) {
            return true;
        }
        if(identifier.equals("resumeButtonClicked:")) {
            return false;
        }
        if(identifier.equals("reloadButtonClicked:")) {
            return false;
        }
        if(identifier.equals("openButtonClicked:")
                || identifier.equals(TOOLBAR_SHOW) || identifier.equals("revealButtonClicked:")) {
            return false;
        }
        if(identifier.equals("clearButtonClicked:")) {
            return false;
        }
        if(identifier.equals("deleteButtonClicked:")) {
            return true;
        }
        return true;
    }
}

*/
