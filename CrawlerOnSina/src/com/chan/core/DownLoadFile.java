package com.chan.core;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;  
import java.net.URLEncoder;  
import java.util.ArrayList;  
import java.util.Date;  
import java.util.HashSet;
import java.util.List;  
import java.util.Set;
import com.alibaba.fastjson.JSON;
import com.chan.core.FeedList;
  
import org.apache.commons.codec.binary.Base64;  
import org.apache.http.Header;
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
  
import org.apache.http.client.entity.UrlEncodedFormEntity;  

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;  
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;  
  
import org.apache.http.message.BasicNameValuePair;  
  
import org.apache.http.params.HttpConnectionParams;  
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;  
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;



import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.chan.tool.HtmlParserTool;
import com.chan.tool.SinaSSOEncoder;

/**
 * download html file from Internet
 * @author Rando
 */
public class DownLoadFile 
{
  //remove illegal char in url
	public String getFileNameByUrl(String url,String contentType)
	{
		//remove "http://"
		url=url.substring(7);
		//for type of html||text
		if(contentType.indexOf("html")!=-1)
		{
			url=url.replaceAll("[\\?/:*|<>\"]", "_")+".html";
			return url;
		}
		//for other type
		else
		{
			return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+contentType.substring(contentType.lastIndexOf("/")+1);
		}
	}
	
    //save webpage byte group to local
	private void saveToLocal(byte[] data,String filepath)
	{   System.out.println("now saving : "+filepath+" ...");
		try
		{
			DataOutputStream out=new DataOutputStream(new FileOutputStream(new File(filepath)));
			for(int i=0;i<data.length;i++)
				out.write(data[i]);
			out.flush();
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//download html file url refers to
	public boolean downloadFile(String url)
	{
		String filepath=null;
		
		HttpClient httpClient=new HttpClient();
	
		//set overtime of request to http-5000ms
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		PostMethod postMethod=new PostMethod(url);
		//PostMethod postMethod=new PostMethod("http://weibo.com/");
		//NameValuePair[] postData=new NameValuePair[2];
		//postData[0]=new NameValuePair("loginName","379045776@qq.com");
		//postData[1]=new NameValuePair("loginPswd","iamyours");
		//postMethod.addParameters(postData);
		int statusCode;
		/*try {
			statusCode = httpClient.executeMethod(postMethod);
		} catch (HttpException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		//build get method
		//GetMethod getMethod=new GetMethod(url);
		//set overtime of request to get-5000ms
		//getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		//retry machine
		//getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		//execute get request
		String[] li=null;
		try
		{   
		    statusCode=httpClient.executeMethod(postMethod);
			//judge status code
			if(statusCode!=HttpStatus.SC_OK)
			{
				System.err.println("Method failed:"+postMethod.getStatusLine());
				filepath=null;
			}
		    //success, process the response froom http
			byte[] responseBody=postMethod.getResponseBody();
			filepath="test\\"+getFileNameByUrl(url,postMethod.getResponseHeader("Content-Type").getValue());
			
			String entity=new String(responseBody,"UTF-8");
			if(entity.indexOf("{\"pid\":\"pl_weibo_feedlist\"")==-1)
				return false;
			String feed_list=entity.substring(entity.indexOf("{\"pid\":\"pl_weibo_feedlist\""));
		    
            feed_list=feed_list.substring(0,feed_list.indexOf(")</script>"));
            FeedList fl=JSON.parseObject(feed_list,FeedList.class);
            
            String aResult=fl.getHtml();
            /*
            String plun=fl.getHtml().toString();
            String[] l=plun.split("\" action-type=\"login\">评论");
            li=new String[l.length-1];
            for(int i=0;i<l.length-1;i++)
            {   
            	li[i]=l[i].substring(l[i].lastIndexOf("<a href=\"")+9);
            	System.out.println(li[i]);
            }
            
           */
            
            
			saveToLocal(aResult.getBytes(),filepath);
		}catch(HttpException e)
		{
			//proposal incorrect || content of response has erros
			System.out.println("Please check out your provided http address!");
			e.printStackTrace();
		}catch(IOException e)
		{
			//erros in your connection
			e.printStackTrace();
		}finally
		{
		  //release connection
			//getMethod.releaseConnection();
			postMethod.releaseConnection();
		}
		return true;
	}
	
    public String downloadComment(String url)
    {
    	DefaultHttpClient client = new DefaultHttpClient();  
        client.getParams().setParameter("http.protocol.cookie-policy",  
                CookiePolicy.BROWSER_COMPATIBILITY);  
        client.getParams().setParameter(  
                HttpConnectionParams.CONNECTION_TIMEOUT, 5000);  
        try {  
            HttpPost post = new HttpPost(  
                    "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.4)");  
  
            String data = HtmlParserTool.getServerTime();  
  
            String nonce = HtmlParserTool.makeNonce(6);  
  
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
            nvps.add(new BasicNameValuePair("entry", "weibo"));
            nvps.add(new BasicNameValuePair("gateway", "1"));  
            nvps.add(new BasicNameValuePair("from", ""));  
            nvps.add(new BasicNameValuePair("savestate", "7"));  
            nvps.add(new BasicNameValuePair("useticket", "1"));  
            nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));  
            nvps.add(new BasicNameValuePair("su",HtmlParserTool.encodeAccount("379045776@qq.com")));  
            nvps.add(new BasicNameValuePair("service", "miniblog"));  
            nvps.add(new BasicNameValuePair("servertime", data));  
            nvps.add(new BasicNameValuePair("nonce", nonce));  
            nvps.add(new BasicNameValuePair("pwencode", "wsse"));  
            nvps.add(new BasicNameValuePair("sp", new SinaSSOEncoder().encode(  
                    "iamyours", data, nonce)));  
  
            nvps.add(new BasicNameValuePair(  
                            "url",  
                            "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));  
            nvps.add(new BasicNameValuePair("returntype", "META"));  
            nvps.add(new BasicNameValuePair("encoding", "UTF-8"));  
            nvps.add(new BasicNameValuePair("vsnval", ""));  
  
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
  
            HttpResponse response = client.execute(post);  
            String entity = EntityUtils.toString(response.getEntity());  
            
              
            String url_d = entity.substring(entity  
                    .indexOf("http://weibo.com/ajaxlogin.php?"), entity  
                    .indexOf("code=0") + 6);  
  
            // 获取到实际url进行连接  
            HttpGet getMethod = new HttpGet(url_d);  
  
            response = client.execute(getMethod);  
            entity = EntityUtils.toString(response.getEntity());  
            entity = entity.substring(entity.indexOf("userdomain") + 14, entity  
                    .lastIndexOf("\""));  
           // System.out.println(entity);  
            String filepath=null;
            
            getMethod = new HttpGet(url);
            response = client.execute(getMethod);  
            entity = EntityUtils.toString(response.getEntity());
            filepath="weibo\\"+getFileNameByUrl(url,"html");
            if(entity.indexOf("{\"pid\":\"pl_content_weiboDetail\"")==-1)
				return null;
			String weiboDetail=entity.substring(entity.indexOf("{\"pid\":\"pl_content_weiboDetail\""));
			
            weiboDetail=weiboDetail.substring(0,weiboDetail.indexOf(")</script>"));
           
            FeedList fl=JSON.parseObject(weiboDetail,FeedList.class);
            
            String aResult=fl.getHtml();
            //System.out.println(aResult);
            saveToLocal(aResult.getBytes(),filepath);
    	   
    	
    	    if(aResult.contains("\">下一页</span>"))
    	    	return aResult.substring(aResult.lastIndexOf("action-data=\"")+16, aResult.indexOf("\">下一页</span>"));
    	    else
    	    	return null;
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	/*
    	String filepath=null;
		
		HttpClient httpClient=new HttpClient();
	
		//set overtime of request to http-5000ms
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		PostMethod postMethod=new PostMethod(url);
		
		int statusCode;
		try
		{   
		    statusCode=httpClient.executeMethod(postMethod);
			//judge status code
			if(statusCode!=HttpStatus.SC_OK)
			{
				System.err.println("Method failed:"+postMethod.getStatusLine());
				filepath=null;
			}
		    //success, process the response froom http
			byte[] responseBody=postMethod.getResponseBody();
			filepath="weibo\\"+getFileNameByUrl(url,postMethod.getResponseHeader("Content-Type").getValue());
			
			String entity1=new String(responseBody,"UTF-8");
			
				
				if(entity1.indexOf("{\"pid\":\"pl_content_weiboDetail\"")==-1)
					return false;
				String weiboDetail=entity1.substring(entity1.indexOf("{\"pid\":\"pl_content_weiboDetail\""));
				
	            weiboDetail=weiboDetail.substring(0,weiboDetail.indexOf(")</script>"));
	            
	            FeedList fl=JSON.parseObject(weiboDetail,FeedList.class);
	           
	            String aResult=fl.getHtml();
	            
	            saveToLocal(aResult.getBytes(),filepath);
			
			
			
			
			
			
			
		}catch(HttpException e)
		{
			//proposal incorrect || content of response has erros
			System.out.println("Please check out your provided http address!");
			e.printStackTrace();
		}catch(IOException e)
		{
			//erros in your connection
			e.printStackTrace();
		}finally
		{
		  //release connection
			//getMethod.releaseConnection();
			postMethod.releaseConnection();
		}*/
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
		return null;
		
	}
    
	
	public String downloadFile_c(String url)
	{
		String filepath=null;
		
		HttpClient httpClient=new HttpClient();
		//set overtime of request to http-5000ms
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		//build get method
		GetMethod getMethod=new GetMethod(url);
		//set overtime of request to get-5000ms
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		//retry machine
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		//execute get request
		try
		{
			int statusCode=httpClient.executeMethod(getMethod);
			//judge status code
			if(statusCode!=HttpStatus.SC_OK)
			{
				System.err.println("Method failed:"+getMethod.getStatusLine());
				filepath=null;
			}
		    //success, process the response froom http
			byte[] responseBody=getMethod.getResponseBody();
			filepath="temp\\"+getFileNameByUrl(url,getMethod.getResponseHeader("Content-Type").getValue());
			saveToLocal(responseBody,filepath);
		}catch(HttpException e)
		{
			//proposal incorrect || content of response has erros
			System.out.println("Please check out your provided http address!");
			e.printStackTrace();
		}catch(IOException e)
		{
			//erros in your connection
			e.printStackTrace();
		}finally
		{
		  //release connection
			getMethod.releaseConnection();
		}
		return filepath;
	}
}
