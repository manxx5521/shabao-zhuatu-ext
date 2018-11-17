package com.xiaoshabao.zhuatu.ext.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoshabao.zhuatu.core.TuInfo;
import com.xiaoshabao.zhuatu.core.ZhuatuCenter;

public class ZhuTuGmtt8 {

	private final static Logger logger = LoggerFactory.getLogger(ZhuTuGmtt8.class);

	protected String urlRoot = "http://www.gmtt8.com";
	
	private String url="http://www.gmtt8.com/archives/category/%E5%9B%BD%E6%A8%A1%E5%A5%97%E5%9B%BE/";
	
	public void test1(){

		new ZhuatuCenter().createDownloadConfig().setUrl(url)
		.setSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\gmtt8")
		.createService().waitProject(true)//解析出项目
		.parserResultFunction((html,pageInfo,config,result)->{
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList list = parser.parse(new HasAttributeFilter("rel", "bookmark"));

			for (Node node : list.toNodeArray()) {
				if (node instanceof LinkTag && node.getParent() instanceof HeadingTag) {
					HeadingTag h2 = (HeadingTag) node.getParent();
					if ("entry-title".equals(h2.getAttribute("class"))) {
						LinkTag link = (LinkTag) node;
						String href = link.getLink();
						String title = link.childAt(0).getText();
						result.add(new TuInfo(href,title));
					}

				}

			}
		}).next((html,config)->{
			String nextUrl = null;
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList nexts = parser.parse(new HasAttributeFilter("class", "page-numbers"));
			for (Node node : nexts.toNodeArray()) {
				if (node instanceof LinkTag) {
					LinkTag link = (LinkTag) node;
					nextUrl = link.getLink();
				}
			}
			return nextUrl;
		}).createService().downloadUrl(true)//解析出要下载的链接
		.parserResultFunction((html,pageInfo,config,result)->{
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList imgs = parser.parse(new HasAttributeFilter("class", "size-full"));
			// 换解析思路
			if (imgs == null || imgs.size() < 1) {
				imgs = Parser.createParser(html, config.getCharsetName()).parse(new TagNameFilter("img"));
			}

			for (Node node : imgs.toNodeArray()) {
				if (node instanceof ImageTag) {
					ImageTag img = (ImageTag) node;
					String src = img.getAttribute("src");
					String alt = img.getAttribute("alt");
					if (StringUtils.isNotEmpty(img.getAttribute("data-echo"))) {
						continue;
					}
					if (alt == null || src == null) {
						continue;
					}

					// 去除部分无用链接
					if (src.startsWith("http://33img.com")) {
						continue;
					}
					logger.info("取到下载链接：" + src);
					if (src.endsWith("/")) {
						throw new RuntimeException("获得的图片下载链接错误");
					}
					TuInfo info = new TuInfo();
					info.setUrl(src);
					info.setTitle(alt);
					result.add(info);
				}
			}
		}).start();
	
	}


}
