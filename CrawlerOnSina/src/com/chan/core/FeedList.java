package com.chan.core;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON class
 * @author Rando
 *
 */
public class FeedList {
public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public List<String> getJs() {
		return js;
	}
	public void setJs(List<String> js) {
		this.js = js;
	}
	public List<String> getCss() {
		return css;
	}
	public void setCss(List<String> css) {
		this.css = css;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
private String pid;
private List<String> js=new ArrayList<String>();
private List<String> css=new ArrayList<String>();
private String html;
}
