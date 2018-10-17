package com.xiaoshabao.zhuatu.ext.service.impl;

import java.nio.charset.Charset;

import org.junit.Test;

import com.xiaoshabao.zhuatu.http.ProxyOkHttp;

public class Test1024UrlTest {
	
	@Test
	public void test(){
		String content=ProxyOkHttp.getInstance("127.0.0.1", 1080).doGetA("http://t66y.com/index.php",Charset.forName("gb2312"));
		System.out.println(content);
	}

}
