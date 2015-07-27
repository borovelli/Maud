/*
 * @(#)RSSFeedLoader.java created Jul 25, 2005 Casalino
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

package it.unitn.ing.rista.io;

import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.feed.synd.SyndFeed;

import java.net.URL;

import it.unitn.ing.rista.awt.BlogContentPane;
import it.unitn.ing.rista.util.Misc;


/**
 * The RSSFeedLoader is a class
 * <p/>
 * Description
 *
 * @author Luca Lutterotti
 * @version $Revision: 1.2 $, $Date: 2006/01/19 14:45:58 $
 * @since JDK1.1
 */

public class RSSFeedLoader {

  String feedURL = null;

  public RSSFeedLoader(String aURL) {
    feedURL = aURL;
  }

  public void loadTheFeed() {
    try {
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new XmlReader(new URL(feedURL)));
//      System.out.println(feed);
      BlogContentPane.FeedFrame(feed);

    } catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("ERROR loaading RSS feed: "+ex.getMessage());
    }
  }
}
