package com.fayuan.crawler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;

public class HuaBanCrawler extends BreadthCrawler {

	private final static String BASE_URL = "http://img.hb.aicdn.com/";
	
	AtomicInteger id=new AtomicInteger(0);
	
	public HuaBanCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		
	}
	@Override
	public void visit(Page page, Links nextLinks) {	
		if (page.getUrl().matches("http://img.hb.aicdn.com/.*")) {
			//将图片内容保存到文件，page.getContent()获取的是文件的byte数组  
	        try {  
	            FileUtils.writeFileWithParent("C:\\Users\\fayuan\\Desktop\\Crawler\\download\\" + id.incrementAndGet()+".jpg",page.getContent());   
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }
		} else {
			try {
				JSONArray pins = new JSONObject(page.getHtml()).getJSONArray("pins");
				for (int i = 0, size = pins.length(); i < size; i++) {
					String key = (String) pins.getJSONObject(i).getJSONObject("file").get("key");
					nextLinks.add(BASE_URL + key);
				}
			} catch (Exception e) {
				
			}		
		}       		
	}


	public static void main(String[] args) throws Exception {
		HuaBanCrawler crawler = new HuaBanCrawler("C:\\Users\\fayuan\\Desktop\\Crawler", false);
		HttpRequesterImpl httpRequest = (HttpRequesterImpl) crawler.getHttpRequester();
		httpRequest.addHeader("X-Request", "JSON");
		httpRequest.addHeader("X-Requested-With", "XMLHttpRequest");
		
		for (int i = 1; i < 400; i++) {
			crawler.addSeed("http://huaban.com/explore/qingchunmeinv/?page=" + i + "&per_page=500");
		}
        crawler.setThreads(50);
        crawler.start(2);
	}
}
