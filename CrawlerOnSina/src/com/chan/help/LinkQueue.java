package com.chan.help;

import java.util.HashSet;
import java.util.Set;

/**
 * encapsulate the Queue
 * recording url
 * @author Rando
 *
 */
public class LinkQueue 
{ 
  //url visited
  private static Set visitedUrl = new HashSet();
 
  //url to be visited
  private static Queue unVisitedUrl = new Queue();
  
  //get unvisited url
  public static Queue getUnVisitedUrl()
  {
	  return unVisitedUrl;
  }
  
  //add to set of url visited
  public static void addVisitedUrl(String url)
  {
	  visitedUrl.add(url);
  }
  
  //remove visited url in queue of url unvisited
  public static void removeVisitedUrl(String url)
  {
	  visitedUrl.remove(url);
  }
  
  //get unvisited url out of queue
  public static Object unVisitedUrlDeQueue()
  {
	  return unVisitedUrl.deQueue();
  }
  
  //fun: add unvisited url
  //att: guarantee each url be visited once
  public static void addUnvisitedUrl(String url)
  {
	  if(url != null &&!url.trim().equals("") && !visitedUrl.contains(url) && !unVisitedUrl.contains(url))
		  unVisitedUrl.enQueue(url);
  }
  
  //get count of url visited
  public static int getVisitedUrlNum()
  {
	  return visitedUrl.size();
  }
  public static Set getVisiterUrl()
  {
	  return visitedUrl;
  }
  
  //is queue of unvisited url empty
  public static boolean unVisitedUrlIsEmpty()
  {
	  return unVisitedUrl.isQueueEmpty();
  }
}
