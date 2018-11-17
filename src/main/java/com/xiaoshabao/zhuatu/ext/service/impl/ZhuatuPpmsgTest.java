package com.xiaoshabao.zhuatu.ext.service.impl;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;
import org.junit.Test;

import com.xiaoshabao.zhuatu.core.TuInfo;
import com.xiaoshabao.zhuatu.core.ZhuatuCenter;

public class ZhuatuPpmsgTest {

	protected String urlRoot = "http://www.ppmsg.net/jiepaimeinv/";
	
	private String pohtoRoot=null;

	@Test
	public void test() {
		new ZhuatuCenter().createDownloadConfig().setUrl(urlRoot)
		.setCharset("gb2312").setSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\ppmsgjp")
		.createService().waitProject(true)//解析项目
		.parserResultFunction((html,pageInfo,config,result)->{
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList list = parser.parse(new HasAttributeFilter("class", "lm"));
			Node body = list.elementAt(0);
			body.accept(new NodeVisitor() {
				@Override
				public void visitTag(Tag tag) {
					if (tag instanceof LinkTag) {
						LinkTag link = (LinkTag) tag;
						String href = link.getLink();
						
						Object parent=link.getParent();
						if(parent instanceof BulletList){
							BulletList bulletList=(BulletList) parent;
							String classStr=bulletList.getAttribute("class");
							if("page".equals(classStr)){
								return;
							}
						}
						result.add(new TuInfo(urlRoot + href,link.getLinkText()));
					}
				}
			});
		}).next((html,config)->{
			String nextUrl = null;
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList nextUl = parser.parse(new HasAttributeFilter("class", "page"));
			NodeList nexts=nextUl.toNodeArray()[0].getChildren();
			for (Node node : nexts.toNodeArray()) {
				if (node instanceof LinkTag) {
					LinkTag link = (LinkTag) node;
					if("下一页".equals(link.getLinkText())){
						nextUrl = urlRoot+"/"+link.getLink();
						return nextUrl;
					}
				}

			}
			return nextUrl;
		}).createService().downloadUrl(true)//解析出要下载的图片
		.parserResultFunction((html,pageInfo,config,result)->{
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList imgagetList = parser.parse(new HasAttributeFilter("id", "imagelist"));
			NodeList imgs=imgagetList.toNodeArray()[0].getChildren();
			for (Node node : imgs.toNodeArray()) {
				if (node instanceof ImageTag) {
					ImageTag img = (ImageTag) node;
					result.add(new TuInfo(urlRoot + img.getAttribute("src"),img.getAttribute("alt")));
				}
			}
			pohtoRoot=pageInfo.getUrl().substring(0, pageInfo.getUrl().lastIndexOf("/"));
		}).next((html,config)->{
			TuInfo info = new TuInfo();
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList list = parser.parse(new HasAttributeFilter("class", "image"));
			Node body = list.elementAt(0);
			body.accept(new NodeVisitor() {
				@Override
				public void visitTag(Tag tag) {
					if (tag instanceof LinkTag) {
						LinkTag link = (LinkTag) tag;
						if("下一页".equals(link.getLinkText())){
							String nextUrl = link.getLink();
							info.setUrl(pohtoRoot+"/"+nextUrl);
						}
					}
				}
			});
			return info.getUrl();
		}).start();
	}

}
