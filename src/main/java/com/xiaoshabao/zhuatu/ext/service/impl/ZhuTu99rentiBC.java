package com.xiaoshabao.zhuatu.ext.service.impl;

import org.junit.Test;

import com.xiaoshabao.zhuatu.core.DownloadUrlCenter;

/**
 * 补充下载<br>
 * 已知url下载
 */
public class ZhuTu99rentiBC {

	@Test
	public void test() {
		new DownloadUrlCenter()
		
		.addDownloadUrl("","")
		
		.createDownloadConfig()
		.setSavePath("E:\\test\\shabao-m\\resources\\plugins\\mm\\99renti")
		.setProxyConfig("127.0.0.1", 1080).getCenter().start();
	}

}
