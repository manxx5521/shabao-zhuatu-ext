package com.xiaoshabao.zhuatu.ext.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoshabao.zhuatu.core.ZhuatuCenter;

public class YoukuRenameTest {

	private final static Logger log = LoggerFactory
			.getLogger(YoukuRenameTest.class);
	
	private static DateFormat ddFormat = new SimpleDateFormat("yyyy.MM.dd");

	private Map<String,List<Video>> videos=new HashMap<String, List<Video>>(50);
	
	private List<Project> projectList=new LinkedList<YoukuRenameTest.Project>();
	
	@Test
	public void test(){
		/*
		
		projectList.add(new Project("九江华华", "http://i.youku.com/i/UMzAwMjU2NDkyMA==/videos?spm=a2hzp.8253869.0.0",
				"J:\\vm\\热舞多组\\温柔港湾Z 九江华华\\"+"(专辑)温柔港湾Z的自频道-优酷视频", ""));
		projectList.add(new Project("快乐天使", "http://i.youku.com/i/UMTg2NjA3ODYw/videos?spm=a2hzp.8244740.0.0",
				"J:\\vm\\热舞多组\\快乐天使 leon7968\\"+"(专辑)leon7968的自频道-优酷视频", ""));
		/*projectList.add(new Project("秀舞时代", "http://i.youku.com/i/UMTQ2OTIzMTQ0/videos?spm=a2hzp.8253869.0.0",
				"D:\\soft\\FLV Downloader\\test\\"+"(专辑)20180503195556秀舞时代的自频道-优酷视频", ""));*/
		/*projectList.add(new Project("萍子广场舞", "http://i.youku.com/i/UMTY5MzEwNTk3Mg==/videos?spm=a2hzp.8253869.0.0",
				"D:\\soft\\FLV Downloader\\test\\"+"(专辑)萍子广场舞，你给我的爱，编舞青儿_", ""));*/
		/*projectList.add(new Project("玉美人梦", "http://i.youku.com/i/UMjkxODU0MDE4MA==/videos?spm=a2hzp.8253869.0.0",
				"J:\\vm\\热舞多组\\[玉美人梦]\\"+"(专辑)20171221020814玉美人梦的自频道-优酷视频", ""));*/
		/*projectList.add(new Project("烛英广场舞", "http://i.youku.com/i/UNTM1NTQ1MzI0/videos?spm=a2hzp.8253869.0.0",
				"J:\\vm\\热舞多组\\[烛英广场舞]\\"+"(专辑)20180319182428优雅莹莹的自频道-优酷视频", ""));*/
		/*projectList.add(new Project("冰冰自由舞", "http://i.youku.com/i/UMzYzMjI3MTEwMA==/videos?spm=a2hzp.8253869.0.0",
				"D:\\soft\\FLV Downloader\\test\\"+"(专辑)冰冰自由舞--好听的动感歌曲19S", ""));
		projectList.add(new Project("炫舞世家", "http://i.youku.com/i/UNDEwOTU4NDQxMg==/videos?spm=a2hzp.8253869.0.0",
				"D:\\soft\\FLV Downloader\\test\\"+"(专辑)番茄", ""));
		projectList.add(new Project("荣蓉广场舞", "http://i.youku.com/i/UNTcyNzY5MDgw/videos?spm=a2hzp.8244740.0.0",
				"D:\\soft\\FLV Downloader\\test\\"+"(专辑)广场舞大赛", ""));
				*/
		
		projectList.add(new Project("舞灵美娜子", "http://i.youku.com/i/UMTMyNDY4OTE5Ng==/videos?spm=a2hzp.8253869.0.0",
//				"J:\\vm\\热舞多组\\舞灵美娜子\\"+"(专辑)20180313003458舞灵美娜子的自频道-优酷视频", ""));
				"J:\\vm\\热舞多组\\[舞灵美娜子 精品]", ""));
		
		
		for(Project project :projectList){
			this.start(project);
		}
	}
	
	
	
	
	public void start(Project project) {
		
		File pathFile=new File(project.getDownloadPath());
		for(File file:pathFile.listFiles()){
			Video video=new Video();
			String realName=file.getName();
			video.realName=realName;
			String name=file.getName().replace("_", " ");
			video.title=name;
			
			if(name.startsWith(project.getTitle())&&name.length()>(project.getTitle().length()+5)){
				name=name.replace(project.getTitle(), "");
			}
			video.toName=name;
			video.basePath=project.getDownloadPath();
			video.ProjectName=project.getTitle();
			List<Video> listVideo=videos.get(FilenameUtils.getBaseName(video.realName));
			if(listVideo==null){
				listVideo=new ArrayList<Video>(2);
			}
			listVideo.add(video);
			videos.put(FilenameUtils.getBaseName(video.realName), listVideo);
		}
		new ZhuatuCenter().createConfig().setUrl(project.getUrl()).createService()
		.parserResultFunction((html,pageInfo,config,result)->{
			try {
				Parser parser = Parser.createParser(html,config.getCharsetString());
				NodeList list = parser.parse(new HasAttributeFilter("class","v-meta"));

				for (Node node : list.toNodeArray()) {
					if (node instanceof Div) {
						Video temp=new Video();
						
						node.accept(new NodeVisitor() {
							@Override
							public void visitTag(Tag tag) {
								if (tag instanceof LinkTag) {
									LinkTag link = (LinkTag) tag;
									temp.title=link.getAttribute("title") ;
								}
								if (tag instanceof Span) {
									Span span = (Span) tag;
									if("v-publishtime".equals(span.getAttribute("class"))){
										temp.date=parserDate(span.getStringText());
									}
								}
							}
						});
						if(temp.title!=null){
							List<Video> listVideo=videos.get(temp.title.replace(" ", "_"));
							if(listVideo!=null){
								for(Video video:listVideo){
									video.date=temp.date;
									reName(video);
								}
							}
						}
						
					}

				}
			} catch (Exception e) {
				log.error("解析出错{}", pageInfo.getUrl(), e);
			}
		}).next((html,config)->{
			try {
				Parser parser = Parser.createParser(html, config.getCharsetString());
				NodeList nexts = parser.parse(new HasAttributeFilter(
						"class", "next"));
				for (Node node : nexts.toNodeArray()) {
					if (node instanceof Bullet) {
						Bullet li = (Bullet) node;
						Node [] links=li.getChildrenAsNodeArray();
						if(links.length>0&&links[0] instanceof LinkTag){
							LinkTag link = (LinkTag)links[0];
							String href = link.getLink().replace("amp;", "");
							return href;
						}
					}

				}
			} catch (Exception e) {
				log.error("下一页 解析出错{}", e);
			}
			return null;
		}).start();
	}
	
	public String parserDate(String str){
		String rs=null;
		Date now=new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(now); 
		int day=c.get(Calendar.DATE); 
		int year=c.get(Calendar.YEAR);
		try {
			if(str.startsWith("昨天")){
				c.set(Calendar.DATE,day-1); 
			}else if(str.startsWith("前天")){
				c.set(Calendar.DATE,day-2);
			}else if(str.startsWith("3天前")){
				c.set(Calendar.DATE,day-3);
			}else if(str.startsWith("4天前")){
				c.set(Calendar.DATE,day-4);
			}else if(str.startsWith("5天前")){
				c.set(Calendar.DATE,day-5);
			}else if(str.startsWith("6天前")){
				c.set(Calendar.DATE,day-6);
			}else if(str.startsWith("7天前")){
				c.set(Calendar.DATE,day-7);
			}else if(str.startsWith("8天前")){
				c.set(Calendar.DATE,day-8);
			}else if(str.startsWith("9天前")){
				c.set(Calendar.DATE,day-9);
			}else{
				if(str.matches("\\d{4}-\\d{2}-\\d{2}")){
					c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(str));
				}else{
					c.setTime(new SimpleDateFormat("MM-dd kk:mm").parse(str));
					c.set(Calendar.YEAR, year);
					if(c.getTime().after(now)){
						c.set(Calendar.YEAR, year-1);
					}
				}
			}
			rs=ddFormat.format(c.getTime());
		} catch (Exception e) {
			log.error("日期解析失败{}",str,e);
		}
		return rs;
	}
	
	/**重命名*/
	public void reName(Video video){
		File file=new File(video.basePath+File.separator+video.realName);
		String toName="["+video.ProjectName+"]"+" "+video.date+" "+video.toName;
		file.renameTo(new File(video.basePath+File.separator+toName));
		log.info("==重命名到=>{}",toName);
	}

	
	class Project {
		private String title;
		private String url;
		private String downloadPath;
		private String logpath;
		public Project(String title, String url, String downloadPath,
				String logpath) {
			super();
			this.title = title;
			this.url = url;
			this.downloadPath = downloadPath;
			this.logpath = logpath;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getDownloadPath() {
			return downloadPath;
		}
		public void setDownloadPath(String downloadPath) {
			this.downloadPath = downloadPath;
		}
		public String getLogpath() {
			return logpath;
		}
		public void setLogpath(String logpath) {
			this.logpath = logpath;
		}
	}
	
	class Video{
		/**目录真是名字*/
		public String realName;
		/**页面标题*/
		public String title;
		/**重命名到的名字*/
		public String toName;
		/**时间字符串*/
		public String date;
		/**根目录*/
		public String basePath;
		/**项目名字*/
		public String ProjectName;
		
	}
	
	

}
