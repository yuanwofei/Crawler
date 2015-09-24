package com.fayuan.crawler;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;

public class NipicCrawler extends BreadthCrawler{

	AtomicInteger id=new AtomicInteger(0);
	
	public NipicCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	@Override
	public void visit(Page page, Links nextLinks) {
        if(!Pattern.matches(".*jpg$",page.getUrl())){
        	try {      		
        		String picUrl = page.getDoc().select("img[class=works-img]").first().attr("src");       		
                nextLinks.add(picUrl);
        	} catch (Exception e) {}
            return ;  
        }  
        
		//将图片内容保存到文件，page.getContent()获取的是文件的byte数组  
        try {  
            FileUtils.writeFileWithParent("C:\\Users\\fayuan\\Desktop\\Crawler\\download\\" + id.incrementAndGet()+".jpg",page.getContent());  
            System.out.println("download:"+page.getUrl());  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}


	public static void main(String[] args) throws Exception {
		NipicCrawler crawler = new NipicCrawler("C:\\Users\\fayuan\\Desktop\\Crawler", true);
		
		for (int i = 1; i < 6460; i++) {
			crawler.addSeed("http://www.nipic.com/photo/renwu/mingxing/index.html?page=" + i);
		}
		crawler.addRegex("http://www.nipic.com/show/.*");
        crawler.setThreads(50);  
        crawler.start(3);
	}
}
