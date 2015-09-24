package com.fayuan.crawler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;

public class NipicDeepCrawler extends DeepCrawler{

	AtomicInteger id=new AtomicInteger(0);
	
	public NipicDeepCrawler(String crawlPath) {
		super(crawlPath);
		
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		Links nextLinks = new Links();
		if(!Pattern.matches(".*jpg$",page.getUrl())){
        	try {      		
        		String picUrl = page.getDoc().select("img[class=works-img]").first().attr("src");       		
                nextLinks.add(picUrl);
        	} catch (Exception e) {}
            return nextLinks;  
        }
		
		
        
		//将图片内容保存到文件，page.getContent()获取的是文件的byte数组  
        try {  
            FileUtils.writeFileWithParent("C:\\Users\\fayuan\\Desktop\\Crawler\\download\\" + id.incrementAndGet()+".jpg",page.getContent());  
            System.out.println("download:"+page.getUrl());  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
		return null;
	}
}
