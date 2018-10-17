package com.xiaoshabao.zhuatu.ext.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;
import org.junit.Test;

import com.xiaoshabao.zhuatu.TuInfo;
import com.xiaoshabao.zhuatu.ZhuatuConfig;
import com.xiaoshabao.zhuatu.ZhuatuUtil;
import com.xiaoshabao.zhuatu.core.ZhuatuFactory;
import com.xiaoshabao.zhuatu.service.ZhuatuDownloadService;
import com.xiaoshabao.zhuatu.service.ZhuatuService;
import com.xiaoshabao.zhuatu.service.ZhuatuWaitService;

public class ZhuatuItokooTest {

	protected String[] url =new String[]{ "http://www.itokoo.com/read.php?tid=25048","MyGirl美媛馆"};
//	protected String[] url =new String[]{ "http://www.itokoo.com/read.php?tid=32549","CANDY网红馆"};
	
	PrintWriter out;
	
	@Test
	public void test() {
		List<ZhuatuService> zhuatuServices = new ArrayList<ZhuatuService>();
		// 第一层
		zhuatuServices.add(new ZhuatuWaitService()  {
			@Override
			public List<TuInfo> parser(String html, TuInfo pageInfo, ZhuatuConfig config) throws Exception {
				List<TuInfo> result = new ArrayList<TuInfo>();
				
				Parser parser = Parser.createParser(html, config.getCharsetString());
				NodeList list = parser.parse(new HasAttributeFilter("class", "tpc_content"));
				Node body = list.elementAt(0);
				body.accept(new NodeVisitor() {
					@Override
					public void visitTag(Tag tag) {
						if (tag instanceof LinkTag) {
							LinkTag a = (LinkTag) tag;
							TuInfo info = new TuInfo();
							info.setUrl(ZhuatuUtil.formatUrl(a.getLink(),config.getWebRoot()));
							info.setTitle(a.getLinkText());
							result.add(info);
						}
					}
				});
				return result;
			}

		});

		// 第二层
		zhuatuServices.add(new ZhuatuDownloadService() {
			@Override
			public List<TuInfo> parser(String html, TuInfo pageInfo, ZhuatuConfig config) throws Exception {
				List<TuInfo> result = new LinkedList<TuInfo>();
				Parser parser = Parser.createParser(html, config.getCharsetString());
				NodeList list = parser.parse(new HasAttributeFilter("class", "tpc_content"));
				Node body = list.elementAt(0);
					body.accept(new NodeVisitor() {
						@Override
						public void visitTag(Tag tag) {
							if (tag instanceof LinkTag) {
								LinkTag a = (LinkTag) tag;
								if (a.getLinkText() != null && ("百度网盘".equals(a.getLinkText())||"百度云网盘".equals(a.getLinkText()))) {
									TextNode text = (TextNode) a.getNextSibling();;
									String tqm=text.getText();
									if(tqm!=null/*&&tqm.trim().startsWith("提取码")*/){
										tqm=tqm.trim().replaceAll("[^0-9a-zA-Z]+", "");
										tqm=tqm.substring(0,tqm.length()>3?4:tqm.length());
										try {
											out.write("<li>");
											out.write("<a style=\"margin-right:15px;\" href=\"");
											out.write(a.getLink());
											
											out.write("\" target=\"_blank\">");
											out.write(pageInfo.getTitle());
											out.write("</a>");
											out.write(tqm);
											out.write("</li>");
											out.flush();
										} catch (Exception e) {
											System.out.println(pageInfo.getTitle()+"：写入文件失败");
											e.printStackTrace();
										}
									}else{
										System.out.println(pageInfo.getTitle()+":无法解析到验证码");
									}
								}
							}
						}
					});
				return result;
			}

		});

		// 装载抓图任务
		ZhuatuConfig config=new ZhuatuConfig();
		config.setCharset("gbk");
		config.setSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\爱图客");
		
		try (PrintWriter out = new PrintWriter(new File(config.getSavePath() + File.separator + url[1]+".html"),"GBK");) {
			this.out=out;
			out.write("<html><head>");
			out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">");
			out.write("<title>"+url[1]+"</title>");	
			out.write("</head>");
			out.write("<body><ul>");
			out.flush();
			ZhuatuFactory.start( url[0], zhuatuServices,config);
			out.write("<ur></body></html>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
