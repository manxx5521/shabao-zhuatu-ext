package com.xiaoshabao.zhuatu.ext.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;
import org.junit.Test;

import com.xiaoshabao.zhuatu.core.TuInfo;
import com.xiaoshabao.zhuatu.core.ZhuatuCenter;
import com.xiaoshabao.zhuatu.core.ZhuatuUtil;

public class ZhuTu99renti {

	public final static String url = "http://www.9grenti.org/html/guomosipai/";
	
	
	/**封面图片 不下载*/
	private final static String FM_JPG="slt.jpg";

	@Test
	public void test() {
		new ZhuatuCenter().createDownloadConfig().setUrl(url).setCharset("GBK")
		.setSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\99renti")
		.setProxyConfig("127.0.0.1", 1080)
		/*
		.addCheckPoject("国模桂芝2017.07.07超大尺度掰穴人体","美女国模希希2016.10.29超大尺度掰穴人体","国模张巧丽超大尺度器具自慰人体"
				,"国模小雅私拍情趣内衣人体","国模李心艾2017.04.04超大尺度私拍人体","国模辰辰2017.05.20超大尺度掰穴人体","美女国模柳絮2016.08.28超大尺度粉红嫩穴人体")
		*/
		.createService().waitProject(true)
		.parserResultFunction((html,pageInfo,config,result)->{
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList list = parser.parse(new HasAttributeFilter("class", "ulPic"));
			Node body = list.elementAt(0);
			body.accept(new NodeVisitor() {
				@Override
				public void visitTag(Tag tag) {
					if (tag instanceof LinkTag) {
						LinkTag link = (LinkTag) tag;
						String href = link.getLink();
						result.add(new TuInfo(href,link.getAttribute("title")));
					}
				}
			});
		}).next((html,config)->{
			String nextUrl = null;
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList nexts = parser.parse(new HasAttributeFilter("class", "a1"));
			for (Node node : nexts.toNodeArray()) {
				LinkTag link = (LinkTag) node;
				nextUrl = link.getLink();
				if (StringUtils.isNotEmpty(nextUrl)) {
					nextUrl = config.getWebRoot() + nextUrl;
				}
			}
			return nextUrl;
		}).createService().downloadUrl(true)
		.parserResultFunction((html,pageInfo,config,result)->{
			Parser parser = Parser.createParser(html, config.getCharsetName());

			NodeList imgs = parser.parse(new TagNameFilter("img"));
			for (Node node : imgs.toNodeArray()) {
				ImageTag img = (ImageTag) node;
				String src = img.getAttribute("src");
				String alt = ZhuatuUtil.formatTitleName(img.getAttribute("alt"));
				if (alt == null || src == null||src.endsWith(ZhuTu99renti.FM_JPG)||!alt.equals(pageInfo.getTitle())) {
					continue;
				}
//				logger.info("取到下载链接：" + src);
				if (src.endsWith("/")) {
					throw new RuntimeException("获得的图片下载链接错误");
				}
				result.add(new TuInfo(src,alt));
			}
		}).next((html,config)->{
			String nextUrl = null;
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList nexts = parser.parse(new HasAttributeFilter("class", "a1"));
			for (Node node : nexts.toNodeArray()) {
				LinkTag link = (LinkTag) node;
				if ("下一页".equals(link.getLinkText())) {
					nextUrl = config.getWebRoot() + link.getLink();
				}
			}
			return nextUrl;
		}).start();
	}

}
