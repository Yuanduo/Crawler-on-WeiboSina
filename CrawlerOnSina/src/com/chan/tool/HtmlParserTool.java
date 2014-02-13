package com.chan.tool;

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

/**
 * tools to process html files
 * @author Rando
 *
 */
public class HtmlParserTool 
{
	public static String nextPage_w(String url)
	{  
        DefaultHttpClient client = new DefaultHttpClient();  
        client.getParams().setParameter("http.protocol.cookie-policy",  
                CookiePolicy.BROWSER_COMPATIBILITY);  
        client.getParams().setParameter(  
                HttpConnectionParams.CONNECTION_TIMEOUT, 5000);  
        try {  
            HttpPost post = new HttpPost(  
                    "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.4)");  
  
            String data = getServerTime();  
  
            String nonce = makeNonce(6);  
  
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
            nvps.add(new BasicNameValuePair("entry", "weibo"));  
            nvps.add(new BasicNameValuePair("gateway", "1"));  
            nvps.add(new BasicNameValuePair("from", ""));  
            nvps.add(new BasicNameValuePair("savestate", "7"));  
            nvps.add(new BasicNameValuePair("useticket", "1"));  
            nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));  
            nvps.add(new BasicNameValuePair("su",  
                    encodeAccount("379045776@qq.com")));  
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
  
            getMethod = new HttpGet(url);//"http://weibo.com/u/2709434204");  
            response = client.execute(getMethod);  
            entity = EntityUtils.toString(response.getEntity());
            
            
           /* 
            String feed_list=entity.substring(entity.indexOf("{\"pid\":\"pl_weibo_feedlist\""));
            feed_list=feed_list.substring(0,feed_list.indexOf(")</script>"));
            
            FeedList fl=JSON.parseObject(feed_list,FeedList.class);
            
            System.out.println(fl.getHtml());
            
            
            
            
            */
            
            
            
            
            
            
            String tt=entity.substring(entity.indexOf("<ul class=\\\"search_page_M\\\">"),entity.indexOf("<p class=\\\"search_rese clearfix\\\">"));
            tt=tt.substring(tt.lastIndexOf("<li><a href")+15, tt.lastIndexOf("\\\""));
          //  System.out.println(tt);
            StringBuffer sf=new StringBuffer(tt);
            while(sf.indexOf("\\")!=-1)
            {
            sf.deleteCharAt(sf.indexOf("\\"));
            
            }
            
            
            sf.insert(0, "http://s.weibo.com");
            //System.out.println(sf.toString());
            
            return sf.toString();
            
      //     "http://s.weibo.com"+
            ///weibo\/%25E7%2594%25B5%25E4%25BF%25A1&rd=newTips&page=2
            
            
            
            
            
            
            
            
            // Document doc =  
            // Jsoup.parse(EntityUtils.toString(response.getEntity()));  
        /*    FileWriter fw = new FileWriter("./cookie.txt");   
            //读取cookie并保存文件  
            List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();  
            
            if (cookies.isEmpty()) {    
                //System.out.println("None");    
            } else {    
                for (int i = 0; i < cookies.size(); i++) {  
                    //System.out.println("- " + cookies.get(i).toString());  
                    fw.write(cookies.get(i).toString()+"\r\n");   
                }    
            }  
            fw.close();  */
            /*
            System.out.println(entity);  
            FileWriter fw1 = new FileWriter("./htm.txt");   
            fw1.write(entity);
            fw1.close();
            */
            
           /*
            try
    		{   org.htmlparser.http.Cookie cookie=new org.htmlparser.http.Cookie(cookies.get(6).getName(),cookies.get(6).getValue());
    		    ConnectionManager cm=new ConnectionManager();
    		    cm.setCookie(cookie, cookies.get(6).getDomain());
    			Parser parser=new Parser(url);
    			parser.setConnectionManager(cm);
    			parser.setEncoding("utf-8");
    		
    			
    			TagNameFilter filter=new TagNameFilter("script");
    			//OrFilter for <a> and <frame>
    			OrFilter linkFilter=new OrFilter(new NodeClassFilter(LinkTag.class),filter);
    			
    			//to gain tags pass all filter
    			NodeList list=parser.extractAllNodesThatMatch(filter);
    			System.out.println(list.size());
    			for(int i=0;i<list.size();i++)
    			{   //System.out.println((list.elementAt(i)).toString());
    			  if((list.elementAt(i)).toString().contains("STK && STK.pageletM && STK.pageletM.view({\"pid\":\"pl_weibo_feedlist\""))
    				 System.out.println((list.elementAt(i)).toString());
    					
    			}
    			
    		}catch(ParserException e){
    			e.printStackTrace();
    		}*/
            
            
         
            
  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return null;
    }  
  
    public static String encodeAccount(String account) {  
        String userName = "";  
        try {  
            userName = new String(Base64.encodeBase64(URLEncoder.encode(account,  
                    "UTF-8").getBytes()));  
        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return userName;  
    }  
  
    public static String makeNonce(int len) {  
        String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
        String str = "";  
        for (int i = 0; i < len; i++) {  
            str += x.charAt((int) (Math.ceil(Math.random() * 1000000) % x  
                    .length()));  
        }  
        return str;  
    }  
  
    public static String getServerTime() {  
        long servertime = new Date().getTime() / 1000;  
        return String.valueOf(servertime);  
    }  

    
	public static Set<String> extraUrl(String url)
	{   //System.out.println("extraUrl");
		Set<String> links=new HashSet<String>();
		try
		{   
			Parser parser=new Parser(url);
			parser.setEncoding("gb2312");
			//NodeFilter for <frame> to gain its attr--"src"
			NodeFilter frameFilter=new NodeFilter(){
				public boolean accept(Node node)
				{
					if(node.getText().startsWith("H3")){
						return true;
					    }
					else{
						return false;
						}
				 }
				};
			
			//OrFilter for <a> and <frame>
			OrFilter linkFilter=new OrFilter(new NodeClassFilter(LinkTag.class),frameFilter);
			
			//to gain tags pass all filter
			
			
			NodeList list=parser.extractAllNodesThatMatch(linkFilter);
		    
			
			for(int i=0;i<list.size();i++)
			{  if(list.elementAt(i).getParent().getText().equals("h3")) 
			   {  Node temp=list.elementAt(i);
			      
			      LinkTag link=(LinkTag)temp;
			      String linkUrl=link.getLink();
			      //System.out.println(linkUrl);
			      links.add(linkUrl);
			      
			   }
			}
		  }catch(ParserException e){
				e.printStackTrace();
			}
		return links;
	}
	
	public static boolean nextPage_c(String url)
	{   System.out.println("nextPage");
		Set<String> links=new HashSet<String>();
		try
		{  
			Parser parser=new Parser(url);
			parser.setEncoding("gb2312");
			//NodeFilter for <frame> to gain its attr--"src"
			NodeFilter frameFilter=new NodeFilter(){
				public boolean accept(Node node)
				{
					if(node.getText().startsWith("A onclick=\"formSub('p',")){
						return true;
					    }
					else{
						return false;
						}
					}
				};
			
			//OrFilter for <a> and <frame>
			OrFilter linkFilter=new OrFilter(new NodeClassFilter(LinkTag.class),frameFilter);
			
			//to gain tags pass all filter
			NodeList list=parser.extractAllNodesThatMatch(linkFilter);
			
			for(int i=0;i<list.size();i++)
			{   //System.out.println(((TagNode)list.elementAt(i)).getText());
				if(((TagNode)list.elementAt(i)).getText().startsWith("a onclick=\"formSub('p',")&&list.elementAt(i).getFirstChild().getText().equals("下一页"))
					return true;
					
			}
			
		}catch(ParserException e){
			e.printStackTrace();
		}
		return false;
	}
	

}
