package com.chan.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.chan.help.LinkQueue;
import com.chan.tool.HtmlParserTool;
import com.chan.tool.LinkFilter;

/**
 * the main crawler
 * @author Rando
 *
 */
public class MyCrawler 
{
	private void initCrawlerWithSeeds(String[] seeds)
	{
		for(int i=0;i<seeds.length;i++)
			LinkQueue.addUnvisitedUrl(seeds[i]);
	}
	/*//another crawler on weibo in a violence way
	public void crawlingOnWeibo(String seed) throws IOException
	{
		//define the Filter
		String c="http://s.weibo.com/weibo/%25E7%2594%25B5%25E4%25BF%25A1&rd=newTips&page=";
		int page=1;
		LinkFilter filter=new LinkFilter(){
			public boolean accept(String url)
			{
				return true;
			}
		};
		String temp=null;
		do
		{   
			if(page!=1)
			{
			   seed=c+page;
			}
			page++;
			
				DownLoadFile downLoader=new DownLoadFile();
				
				temp=downLoader.downloadFile(seed);
			
		}while(temp!=null);
		System.out.println("count of page:"+page);
		
	}
	*/
	
	public void crawlingOnWeibo(String seed) throws IOException
	{
		//define the Filter
		
		
		LinkFilter filter=new LinkFilter(){
			public boolean accept(String url)
			{
				return true;
			}
		};
		DownLoadFile downloader=new DownLoadFile();
		//String[] pingLun;
		//String lastPage=null;
		while(seed!=null&&!LinkQueue.getVisiterUrl().contains(seed))
		{   downloader.downloadFile(seed);
		  //  if(pingLun[0]=="failed"||pingLun==null)
		    //	break;
		   // lastPage=seed;
		    LinkQueue.addVisitedUrl(seed);
		    seed=HtmlParserTool.nextPage_w(seed);
		    
		
		}
		System.out.println("finished!");  
	
	}
	public void crawlingOnComment_Weibo(ArrayList<String> urlSet)
	{
		DownLoadFile downloader=new DownLoadFile();
		String url;
		for(int i=0;i<urlSet.size();i++)
		{   url=urlSet.get(i);
			while(url!=null)
			{   System.out.println(url);
				url=urlSet.get(i)+"?"+downloader.downloadComment(url);
			}
		}
	}
	public void crawlingOnC114(String seed)
	{
		String c1="http://www.c114.net/search/?q=%B5%E7%D0%C5&p=";
		String c2="&addtime=0&r=0&source=0";
		int page=1;
		
		do
		{   
			if(page!=1)
			{
			   seed=c1+page+c2;
			}
			page++;
			Set<String> links=HtmlParserTool.extraUrl(seed);
			
			for(String link:links)
			{   
				LinkQueue.addUnvisitedUrl(link);
			}
			while(!LinkQueue.unVisitedUrlIsEmpty())
			{
				DownLoadFile downLoader=new DownLoadFile();
				String visitedUrl=(String)LinkQueue.unVisitedUrlDeQueue();
				downLoader.downloadFile(visitedUrl);
			}
			
			
		}while(HtmlParserTool.nextPage_c(seed));
		System.out.println("count of page:"+page);
		System.out.println("finished!");  
	}
	public static void main(String[] args) throws IOException
	{
		MyCrawler crawler=new MyCrawler();
		ArrayList<String> a=new ArrayList<String>();
		a.add("http://e.weibo.com/1735209835/zbGryh3F8");
		crawler.crawlingOnComment_Weibo(a);
		//crawler.crawlingOnC114("http://www.c114.net/search/?q=%B5%E7%D0%C5&r=0");
		//crawler.crawlingOnWeibo("http://s.weibo.com/weibo/%25E7%2594%25B5%25E4%25BF%25A1?topnav=1&wvr=5&topsug=1&rd=newTips");
		  
			                                //  http://s.weibo.com/weibo/%25E7%2594%25B5%25E4%25BF%25A1?topnav=1&wvr=5&topsug=1&rd=newTips
			                                 // http://s.weibo.com/weibo/%25E7%2594%25B5%25E4%25BF%25A1&rd=newTips&page=2
	}
	

}
