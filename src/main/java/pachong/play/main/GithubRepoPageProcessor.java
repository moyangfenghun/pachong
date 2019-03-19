package pachong.play.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import pachong.play.HttpClientDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class GithubRepoPageProcessor implements PageProcessor {
	
	private Logger Logger=LoggerFactory.getLogger(this.getClass());

	private Site site = Site.me().setDomain("gushiwen.org").setSleepTime(2000);
	
	private String auther="李白";
	private String autherEncode="";
	public GithubRepoPageProcessor(){
		try {
			autherEncode=URLEncoder.encode(auther,"UTF-8").toLowerCase();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Autowired
	@Override
	public void process(Page page) {

		
		String urlregex="https://so\\.gushiwen\\.org/search\\.aspx\\?type=author&page=\\d{1,3}&value="+autherEncode;
		
		List<String> urls = page.getHtml().$("a.amore").links().regex(urlregex).all();
		if(!CollectionUtils.isEmpty(urls)){
			page.addTargetRequest(urls.get(0));
		}
		if(page.getUrl().regex("https://so\\.gushiwen\\.org/search\\.aspx\\?value="+autherEncode+"").match()){
			//解析作者简介
			page.putField("auther",page.getHtml().$("div.sonspic").$("div.cont b").css("span[style]").xpath("allText()").toString());
			page.putField("digest",page.getHtml().$("div.sonspic").$("div.cont p[style='margin:0px;']").xpath("allText()").toString());
			//解析古诗列表
			page.putField("titles",parsingList(page));
		}else{
			page.putField("titles",parsingList(page));
		}
		
	}

	@Override
	public Site getSite() {
		return site;
	}

	public  List<Map<String, Object>> parsingList(Page page){
		List<String> all = page.getHtml().$("div.sons").all();
		List<Map<String, Object>> asfdlist=new ArrayList<>();
		for (String string : all) {
			Html html = new Html(string);
			String title = html.$("a[style]").xpath("allText()").toString();
			String content = html.$("div.contson").xpath("tidyText()").toString();
			String dynasty=html.$("p.source a:eq(0)").xpath("allText()").toString();
			String auther=html.$("p.source a span").xpath("allText()").toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title);
			map.put("auther", auther);
			map.put("dynasty", dynasty);
			map.put("content", content);
			asfdlist.add(map);
		}
		return asfdlist;
	}
	
	public static void main(String[] args) {
//		URLEncoder.encode(auther, "UTF-8");//编码 
//      URLDecoder.decode(, "UTF-8");//解码W
		String auther1="李白";
		try {
			auther1=URLEncoder.encode(auther1,"UTF-8");
			auther1=auther1.toLowerCase();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Spider.create(new GithubRepoPageProcessor()).setDownloader(new HttpClientDownloader())
		.addUrl("https://so.gushiwen.org/search.aspx?value="+auther1+"")
//		.thread(2)
		.run();
	}
}