package com.fish.webcrawler;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlParserTool {
	public static final String START_URL = "http://mvnrepository.com/open-source";
	// 获取一个网站上的链接,filter 用来过滤链接
	public static Set<String> extracLinks(String url,LinkFilter filter) {

		Set<String> links = new HashSet<String>();
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");
			
			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					if (node.getText().startsWith("href=")) {
						return true;
					} else {
						return false;
					}
				}
			};
			// OrFilter 来设置过滤 <a> 标签
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(
					LinkTag.class), frameFilter);
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag)// <a> 标签
				{
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					if(filter.accept(linkUrl)) {
						links.add(linkUrl);
						System.out.println(linkUrl);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return links;
	}
	//测试的 main 方法
	public static void main(String[]args)
	{
Set<String> links = HtmlParserTool.extracLinks(START_URL,new LinkFilter()
		{
			//提取以 http://www.twt.edu.cn 开头的链接
			public boolean accept(String url) {
				if(url.startsWith(START_URL))
					return true;
				else
					return false;
			}
			
		});
		for(String link : links)
			System.out.println(link);
	}
}