/*
 * $Id: JDesktopDemo.java,v 1.1 2004/12/27 16:15:21 luca Exp $
 *
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */
package gov.noaa.pmel.sgt.demo;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import gov.noaa.pmel.sgt.*;
import gov.noaa.pmel.util.SoTPoint;
import gov.noaa.pmel.util.SoTRange;
import gov.noaa.pmel.util.Point2D;
import gov.noaa.pmel.util.Dimension2D;

/**
 * Example demonstrating the use of <code>PropertyChangeEvents</code>
 * in the datamodel.  <code>JDesktopDemo</code> constructs the plot
 * from basic <code>sgt</code> objects. 
 *
 * @author Donald Denbo
 * @version $Revision: 1.1 $, $Date: 2004/12/27 16:15:21 $
 * @since 2.1
 */

public class JDesktopDemo extends JApplet implements PropertyChangeListener {
  PseudoRealTimeData rtData_;
  JPane pane_;
  Layer layer_;
  TimeAxis xbot_;
  PlainAxis yleft_;
  LinearTransform xt_, yt_;
  boolean isStandalone = false;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonPanel = new JPanel();
  JButton startButton = new JButton();
  JButton stopButton = new JButton();
  JButton resetButton = new JButton();

  /**Construct the applet*/
  public JDesktopDemo() {
  }
  /**Initialize the applet*/
  public void init() {
    /*
     * Create the data source
     */
    rtData_ = new PseudoRealTimeData("rtDataSource", "Sea Level");
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    /*
     * add listener for data source.  JDesktopDemo is listening
     * for rangeModified events
     */
    rtData_.addPropertyChangeListener(this);
  }
  /**Component initialization*/
  private void jbInit() throws Exception {
    this.setSize(new Dimension(800, 440));
    this.getContentPane().setLayout(borderLayout1);
    startButton.setText("start");
    startButton.addActionListener(new JDesktopDemo_startButton_actionAdapter(this));
    stopButton.setText("stop");
    stopButton.addActionListener(new JDesktopDemo_stopButton_actionAdapter(this));
    resetButton.setText("reset");
    resetButton.addActionListener(new JDesktopDemo_resetButton_actionAdapter(this));
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(startButton, null);
    buttonPanel.add(stopButton, null);
    buttonPanel.add(resetButton, null);
    //
    // construct JPane
    //
    pane_ = new JPane("Real Time Data Demo", new Dimension(800, 400));
    pane_.setBatch(true);
    pane_.setLayout(new StackedLayout());
    pane_.setBackground(Color.white);
    /*
     * xsize, ysize are the width and height in physical units
     * of the Layer graphics region.
     *
     * xstart, xend are the start and end points for the X axis
     * ystart, yend are the start and end points for the Y axis
     */
    double xsize = 6.0;
    double xstart = 0.6;
    double xend = 5.5;
    double ysize = 3.0;
    double ystart = 0.6;
    double yend = 2.75;
    /*
     * Create the layer and add it to the Pane.
     */
    CartesianGraph graph;
    /*
     * Get x and y ranges from data source.
     */
    SoTRange.GeoDate xrange = (SoTRange.GeoDate)rtData_.getXRange();
    SoTRange.Double yrange = (SoTRange.Double)rtData_.getYRange();

    xt_ = new LinearTransform(xstart, xend, xrange.start, xrange.end);
    yt_ = new LinearTransform(ystart, yend, yrange.start, yrange.end);

    layer_ = new Layer("Layer 1", new Dimension2D(xsize, ysize));
    pane_.add(layer_);

    SGLabel title = new SGLabel("title",
				"Real Time Demo",
				new Point2D.Double((xstart+xend)/2.0,
						   ysize-0.05));
    title.setAlign(SGLabel.TOP, SGLabel.CENTER);
    title.setFont(new Font("Serif", Font.PLAIN, 14));
    title.setHeightP(0.25);
    title.setColor(Color.blue.darker());
    layer_.addChild(title);
    /*
     * Create a CartesianGraph and set transforms.
     */
    graph = new CartesianGraph("Time Graph");
    layer_.setGraph(graph);
    graph.setXTransform(xt_);
    graph.setYTransform(yt_);
    /*
     * Create the bottom axis, set its range in user units
     * and its origin. Add the axis to the graph.
     */
    SoTPoint origin = new SoTPoint(xrange.start, yrange.start);
    xbot_ = new TimeAxis("Botton Axis", TimeAxis.AUTO);
    xbot_.setRangeU(xrange);
    xbot_.setLocationU(origin);
    Font xbfont = new Font("Helvetica", Font.PLAIN, 14);
    xbot_.setLabelFont(xbfont);
    graph.addXAxis(xbot_);
    /*
     * Create the left axis, set its range in user units
     * and its origin. Add the axis to the graph.
     */
    String yLabel = "Latitude";

    yleft_ = new PlainAxis("Left Axis");
    yleft_.setRangeU(yrange);
    yleft_.setLocationU(origin);
    yleft_.setLabelFont(xbfont);
    SGLabel ytitle = new SGLabel("yaxis title", yLabel,
                                 new Point2D.Double(0.0, 0.0));
    Font ytfont = new Font("Helvetica", Font.PLAIN, 14);
    ytitle.setFont(ytfont);
    ytitle.setHeightP(0.2);
    yleft_.setTitle(ytitle);
    graph.addYAxis(yleft_);

    LineAttribute attr = new LineAttribute();
    graph.setData(rtData_, attr);

    this.getContentPane().add(pane_, BorderLayout.CENTER);
    if(!isStandalone) pane_.setBatch(false);
  }
  /**Start the applet*/
  public void start() {
  }
  /**Stop the applet*/
  public void stop() {
    rtData_.stopData();
  }
  /**Destroy the applet*/
  public void destroy() {
    rtData_.stopData();
  }
  /**Get Applet information*/
  public String getAppletInfo() {
    return "Applet Information";
  }
  /**Main method*/
  public static void main(String[] args) {
    JFrame desk = new JFrame("Desktop Demo");
    //EXIT_ON_CLOSE == 3
    desk.setDefaultCloseOperation(3);
    desk.getContentPane().setLayout(new BorderLayout());
    desk.setSize(850, 500);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    desk.setLocation((d.width - desk.getSize().width) / 2, 
		     (d.height - desk.getSize().height) / 2);
    JDesktopPane desktop = new JDesktopPane();
    desk.getContentPane().add(desktop, BorderLayout.CENTER);
    JDesktopDemo applet = new JDesktopDemo();
    applet.isStandalone = true;
    JInternalFrame frame = new JInternalFrame("Real Time Data Demo",
					      false, false, false, true);
    frame.getContentPane().add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.setSize(800,440);
    desktop.add(frame);
    applet.pane_.setBatch(false);
    desk.setVisible(true);
    frame.setVisible(true);
  }

  //static initializer for setting look & feel
  static {
    try {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch(Exception e) {
    }
  }

  void startButton_actionPerformed(ActionEvent e) {
    rtData_.startData();
  }

  void stopButton_actionPerformed(ActionEvent e) {
    rtData_.stopData();
  }

  void resetButton_actionPerformed(ActionEvent e) {
    rtData_.stopData();
    rtData_.resetData();
    resetRange();
  }
  private void resetRange() {
    /*
     * A change in the range has occured. Get new range
     * and set transforms, axes, and origin appropriately.
     */
    pane_.setBatch(true);
    SoTRange.GeoDate xrange = (SoTRange.GeoDate)rtData_.getXRange();
    SoTRange.Double yrange = (SoTRange.Double)rtData_.getYRange();
    SoTPoint origin = new SoTPoint(xrange.start, yrange.start);
    xt_.setRangeU(xrange);
    yt_.setRangeU(yrange);
    xbot_.setRangeU(xrange);
    xbot_.setLocationU(origin);
    yleft_.setRangeU(yrange);
    yleft_.setLocationU(origin);
    pane_.setBatch(false);
  }
  public void propertyChange(PropertyChangeEvent evt) {
    /**
     * dataModified property is handled by CartesianGraph
     * only need to look for rangeModified here to make sure
     * range is properly updated
     */
    if("rangeModified".equals(evt.getPropertyName())) {
      resetRange();
    }
  }
}
/*
 * wrappers for button events created by JBuilder
 */
class JDesktopDemo_startButton_actionAdapter implements ActionListener {
  JDesktopDemo adaptee;

  JDesktopDemo_startButton_actionAdapter(JDesktopDemo adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.startButton_actionPerformed(e);
  }
}

class JDesktopDemo_stopButton_actionAdapter implements ActionListener {
  JDesktopDemo adaptee;

  JDesktopDemo_stopButton_actionAdapter(JDesktopDemo adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.stopButton_actionPerformed(e);
  }
}

class JDesktopDemo_resetButton_actionAdapter implements ActionListener {
  JDesktopDemo adaptee;

  JDesktopDemo_resetButton_actionAdapter(JDesktopDemo adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.resetButton_actionPerformed(e);
  }
}
