package com.xiaoshabao.zhuatu.ext.service.impl;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoshabao.zhuatu.core.TuInfo;
import com.xiaoshabao.zhuatu.core.ZhuatuCenter;

public class Zhuatu1024Test {
	
	private final static Logger log = LoggerFactory.getLogger(Zhuatu1024Test.class);

//	protected String url = "http://t66y.com/thread0806.php?fid=16";
	protected String url = "http://www.caoliu2049.com/thread0806.php?fid=16";
	
	@Test
	public void test() {
		// 装载抓图任务
		new ZhuatuCenter().createDownloadConfig().setUrl(url).setCharset("gbk")
		.setSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\1024")
		.addExtSavePath("J:\\vm\\图片系列\\名站图片\\1024普通","J:\\vm\\图片系列\\名站图片\\1024知名")
		.addExtSavePath("J:\\vm\\图片系列\\名站图片\\1024记载")//记录后不再下载，源文件夹可能已经改名
		.addExtSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\1024露出")
		.setProxyConfig("127.0.0.1", 1080)
		
		.addFirstProject("拉拉队长","蜜丝原创","花颜","蜀黍原创","护士老婆","茵茵","骚甜甜","赌博少妇","兔精女王")
		.addFirstProject("兔妈妈","贱宝","提香","家有仙妻Lee","娇妻美美","菀晴","恋瑾","球王酥酥","樱玉花子","西安的太阳")
		.addFirstProject("芭芭拉","娇妻美美","黛西","DDCLUB","小棒冰","开放教师","六九公社","太阳花","Facepower","狐狸的小柠檬","爱妻小可爱")
		.addFirstProject("小炮哥","啪照工作室","吾家骚妻","嫩穴媳妇","软萌小仙女","甜甜","旧叙系列","海南小骚","玲玲的鸡巴","太乙归来","美腿娇妻")
		.addFirstProject("上官大人","花花","济南活动","璐璐","辣妈辣嘛","海南小骚逼","时间灰烬","阿育王天瞳","無念出品","沐逸逸")
		.addFirstProject("模特第","骚婷婷","闲愁出品","月儿吖吖","快乐18出品","浪子原创","骚妻养成计划","美腿娇妻","感恩草榴","单纯小婷婷")
		.addFirstProject("魅蓝师傅","母畜多多","抽象孙先生","约神猎物","森系女神","南阳凡哥","包子铺","真空少女","楚榴香","12316757","萌娃M")
		
		.addFirstProject("露出","SM","公园","野外","老司机集结号","Tumblr","美美","一纳疯骚","多多","疯骚贱客","缘分","肉便器","熟女姐姐","小母狗","骚妻情趣睡衣诱惑","人造白虎少妇情人")
		
		/*
		.addNoUrl("https://s19.postimg.org")
		.addNoUrl("https://s20.postimg.org")//https://s20.postimg.org/s7cdqfyel/image.jpg
		.addNoUrl("http://s7tu.com/","http://www.s7tu.com")//http://s7tu.com/images/2018/01/18/DSC021425cfc7.jpg
		.addNoUrl("https://xxx.freeimage.us")//https://xxx.freeimage.us/image.php?id=FECF_5A5812CE&jpg
//		.addNoUrl("http://120.52.72.23")//http://120.52.72.23/c.pic303.com/images/2017/12/05/DSC_2021672a9a56848ff21c.jpg
		.addNoUrl("https://s18.postimg.org")//https://s18.postimg.org/gkemehp61/IMG_5813.jpg
		.addNoUrl("https://s8.postimg.org")//https://s8.postimg.org/vz5r1e3d1/IMG_5708.jpg
		.addNoUrl("https://s26.postimg.org")//https://s26.postimg.org/vy8gxl421/IMG_1486.jpg
		.addNoUrl("https://s1.areyoucereal.com/")//https://s1.areyoucereal.com/xedoR.png
		.addNoUrl("http://ipoock.com")//http://ipoock.com/img/g1/20160904124106xp354.jpeg
		.addNoUrl("https://66.media.tumblr.com/")//https://66.media.tumblr.com/91f270ab0ae9f8f6d7a5f693b8a0beb6/tumblr_ocpvsdWPDa1u1izgro10_1280.jpg
		.addNoUrl("https://65.media.tumblr.com/")
		.addNoUrl("https://67.media.tumblr.com/")
		.addNoUrl("http://www.99kuma.com")//http://www.99kuma.com/1024AAsadfs34qw123qre/001/06.jpg
		.addNoUrl("http://www.795mm.com/")//http://www.795mm.com/upload/photos/tu/10049/20180520/20180520/b/004.jpg
		.addNoUrl("https://go.imgs.co")//https://go.imgs.co/u/2017/08/01/Uh4y0w.jpg
		.addNoUrl("https://www.sxotu.com")//https://www.sxotu.com/u/20180515/15360278.jpg
		.addNoUrl("http://www.sxotu.com")//https://www.sxotu.com/u/20180515/15360278.jpg
		.addNoUrl("https://s17.postimg.org")//https://s17.postimg.org/r5i26sn33/QQ_20180127105634.jpg
		.addNoUrl("http://www.x6img.com")//http://www.x6img.com/u/20180629/11153916.JPG
		
		
		.addNoUrl("http://www.siimg.com")//http://www.siimg.com/u/20180626/16311143.jpg 
		.addNoUrl("http://www.xoimg.club")//http://www.xoimg.club/u/20180707/02294860.jpg
		.addNoUrl("https://www.soxtu.com")//https://www.soxtu.com/upload/photos/tu/10049/20180708/j/3.jpg
		.addNoUrl("https://s25.postimg.org")//访问慢
		.addNoUrl("http://www.sxeimg.com")//访问慢
		
		
		.testNoUrl("https://s6tu.com","https://s6tu.com/images/2018/03/14/D372DDC346E8879227F718E0AB2EDD5E.jpg ")
		.testNoUrl("http://s6tu.com","https://s6tu.com/images/2018/03/14/D372DDC346E8879227F718E0AB2EDD5E.jpg ")//
		.testNoUrl("http://www.s6tu.com","http://www.s6tu.com/images/2018/06/26/20180625_020940.jpg")//
		.testNoUrl("http://www.s2tu.com","http://www.s2tu.com/images/2018/06/29/C.jpg")// 
		.testNoUrl("http://s2tu.com","http://s2tu.com/images/2018/06/26/IMG_9926_.md.jpg")//
		.testNoUrl("http://www.siimg.com","http://www.siimg.com/u/20180626/16311143.jpg")// 
		.testNoUrl("http://www.x6img.com","http://www.x6img.com/u/20180909/21413316.JPG")// 
		.testNoUrl("https://www.privacypic.com","https://www.privacypic.com/images/2018/09/02/IMG_1951877c9f2d9aa46c1a.md.jpg")// 
		.testNoUrl("http://www.vxotu.com","http://www.vxotu.com/u/20180830/21373021.jpg")// 
		*/
		.createService().waitProject(true)//解析具体项目
		.parserResultFunction((html,pageInfo,config,result)->{
			Document doc = Jsoup.parse(html);
			Elements divs = doc.select("td.tal > h3 > a");
			for (Element div : divs) {
				String href = div.attr("href");
				Elements fonts = div.select("font");
				String title=null;
				if(fonts.size()>0){
					Element font=fonts.get(0);
					if("blue".equals(font.attr("color"))||"red".equals(font.attr("color"))){
						continue;
					}
					title=font.text();
				}else{
					title=div.text();
				}
				
				/*TuInfo info = new TuInfo();
				info.setUrl(ZhuatuUtil.formatUrl(config.getWebRootAll() + href));
				info.setTitle(ZhuatuUtil.formatTitleName(title));*/
				result.add(new TuInfo(href,title));
			}
			result.forEach(tu->{
				log.info(tu.getTitle());
			});
		}).next((html,config)->{
			Document doc = Jsoup.parse(html);
			Elements divs = doc.select("div.pages > a");
			for (Element a : divs) {
				if("下一頁".equals(a.text())){
					return config.getWebRootAll()+a.attr("href");
				}
			}
			return null;
		}).createService().downloadUrl(true)
		.parserResultFunction((html,pageInfo,config,result)->{
			Parser parser = Parser.createParser(html, config.getCharsetName());
			NodeList imgagetList = parser.parse(new HasAttributeFilter("type", "image"));
			for (Node node : imgagetList.toNodeArray()) {
				if (node instanceof InputTag) {
					InputTag input = (InputTag) node;
					String src = input.getAttribute("src");
					if(src==null){
						src=input.getAttribute("data-src");
					}
					result.add(new TuInfo(src,pageInfo.getTitle()));
				}
			}
		
		});
	}
}
