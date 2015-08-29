package com.webservice.newsapp.test;

import com.webservice.newsapp.jaxb.model.Feed;
import com.webservice.newsapp.jaxb.model.FeedMessage;
import com.webservice.newsapp.jaxb.model.RSSFeedParser;

public class ReadTest {
	  public static void main(String[] args) {
	    RSSFeedParser parser = new RSSFeedParser("http://www.vogella.com/article.rss");
	    Feed feed = parser.readFeed();
	    System.out.println(feed);
	    for (FeedMessage message : feed.getMessages()) {
	      System.out.println(message);

	    }

	  }
	} 

