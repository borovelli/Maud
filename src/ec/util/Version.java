package ec.util;

import java.util.*;

/*
 * Version.java
 *
 * Created: Wed Aug 11 19:44:46 1999
 * By: Sean Luke
 */

/**
 * Version is a static class which stores version information for this
 * evolutionary computation system.
 *
 * @author Sean Luke
 * @version 1.0
 */

public class Version {
  public static final String name = "ECJ";
  public static final String version = "9";
  public static final String copyright = "2002";
  public static final String author = "Sean Luke";
  public static final String authorEmail = "sean@cs.gmu.edu";
  public static final String authorURL = "http://www.cs.umd.edu/projects/plus/ec/ecj/";
  public static final String date = "October 10, 2002";
  public static final String suggestedJavaVersion = "1.2.2";
  public static final String minimumJavaVersion = "1.1";

  public static final String message() {
    Properties p = System.getProperties();
    String javaVersion = p.getProperty("java.version");
    String javaVM = p.getProperty("java.vm.name");
    String javaVMVersion = p.getProperty("java.vm.version");
    if (javaVM != null) javaVersion = javaVersion + " / " + javaVM;
    if (javaVM != null && javaVMVersion != null) javaVersion = javaVersion + "-" + javaVMVersion;


    return
            "\n| " + name +
            "\n| An evolutionary computation system (version " + version + ")" +
            "\n| Copyright " + copyright + " by " + author +
            "\n| URL: " + authorURL +
            "\n| Mail: " + authorEmail +
            "\n| Date: " + date +
            "\n| Current Java: " + javaVersion +
            "\n| Suggested Minimum Java: " + suggestedJavaVersion +
            "\n| Required Minimum Java: " + minimumJavaVersion +
            "\n\n";
  }
}
