package com.xiaoshabao.zhuatu.ext.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;
import org.junit.Test;

import com.xiaoshabao.zhuatu.TuInfo;
import com.xiaoshabao.zhuatu.ZhuatuConfig;
import com.xiaoshabao.zhuatu.ZhuatuUtil;
import com.xiaoshabao.zhuatu.core.ZhuatuFactory;
import com.xiaoshabao.zhuatu.service.ZhuatuDownloadService;
import com.xiaoshabao.zhuatu.service.ZhuatuService;
import com.xiaoshabao.zhuatu.service.ZhuatuWaitService;

public class ZhuTu99renti {

//	private final static Logger logger = LoggerFactory.getLogger(ZhuTu99renti.class);

	protected String url = "http://www.9grenti.org/html/guomosipai/";
	
	
	/**封面图片 不下载*/
	private final static String FM_JPG="slt.jpg";

	@Test
	public void test() {
		List<ZhuatuService> zhuatuServices = new ArrayList<ZhuatuService>();
		// 第一层解析分项的信息，找打具体的项目
		zhuatuServices.add(new ZhuatuWaitService() {
			@Override
			public List<TuInfo> parser(String html, TuInfo pageInfo, ZhuatuConfig config) throws ParserException {
				final List<TuInfo> result = new LinkedList<TuInfo>();
				Parser parser = Parser.createParser(html, config.getCharsetString());
				NodeList list = parser.parse(new HasAttributeFilter("class", "ulPic"));
				Node body = list.elementAt(0);
				body.accept(new NodeVisitor() {
					@Override
					public void visitTag(Tag tag) {
						if (tag instanceof LinkTag) {
							LinkTag link = (LinkTag) tag;
							String href = link.getLink();
							String title = ZhuatuUtil.formatTitleName(link.getAttribute("title"));

							TuInfo info = new TuInfo();
							info.setUrl(config.getWebRoot() + href);
							info.setTitle(title);
							result.add(info);
						}
					}
				});
				return result;
			}

			@Override
			public String nextPage(String html, ZhuatuConfig config) throws ParserException {
				String nextUrl = null;
				Parser parser = Parser.createParser(html, config.getCharsetString());
				NodeList nexts = parser.parse(new HasAttributeFilter("class", "a1"));
				for (Node node : nexts.toNodeArray()) {
					LinkTag link = (LinkTag) node;
					nextUrl = link.getLink();
					if (StringUtils.isNotEmpty(nextUrl)) {
						nextUrl = config.getWebRoot() + nextUrl;
					}
				}
				return nextUrl;
			}

		});

		// 第二层解析具体照片
		zhuatuServices.add(new ZhuatuDownloadService() {
			@Override
			public List<TuInfo> parser(String html, TuInfo pageInfo, ZhuatuConfig config) throws ParserException {
				List<TuInfo> result = new LinkedList<TuInfo>();
				Parser parser = Parser.createParser(html, config.getCharsetString());

				NodeList imgs = parser.parse(new TagNameFilter("img"));
				for (Node node : imgs.toNodeArray()) {
					ImageTag img = (ImageTag) node;
					String src = img.getAttribute("src");
					String alt = ZhuatuUtil.formatTitleName(img.getAttribute("alt"));
					if (alt == null || src == null||src.endsWith(ZhuTu99renti.FM_JPG)||!alt.equals(pageInfo.getTitle())) {
						continue;
					}
//					logger.info("取到下载链接：" + src);
					if (src.endsWith("/")) {
						throw new RuntimeException("获得的图片下载链接错误");
					}
					TuInfo info = new TuInfo();
					info.clear();
					info.setUrl(src);
					info.setTitle(alt);
					result.add(info);
				}
				return result;
			}

			@Override
			public String nextPage(String html, ZhuatuConfig config) throws ParserException {
				String nextUrl = null;
				Parser parser = Parser.createParser(html, config.getCharsetString());
				NodeList nexts = parser.parse(new HasAttributeFilter("class", "a1"));
				for (Node node : nexts.toNodeArray()) {
					LinkTag link = (LinkTag) node;
					if ("下一页".equals(link.getLinkText())) {
						nextUrl = config.getWebRoot() + link.getLink();
					}
				}
				return nextUrl;
			}

		});

		// 装载抓图任务
		ZhuatuConfig config=new ZhuatuConfig();
		config.setSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\99renti");
		config.setCharset("GBK");
		config.setProxyConfig("127.0.0.1", 1080);
//		config.setDwonloadType(HttpType.HTTPCLIENT);
//		config.addCheckPoject("国模桂芝2017.07.07超大尺度掰穴人体","美女国模希希2016.10.29超大尺度掰穴人体",
//		"国模张巧丽超大尺度器具自慰人体","国模小雅私拍情趣内衣人体","国模李心艾2017.04.04超大尺度私拍人体","国模辰辰2017.05.20超大尺度掰穴人体","美女国模柳絮2016.08.28超大尺度粉红嫩穴人体");
		ZhuatuFactory.start(url, zhuatuServices, config);
	}

}
