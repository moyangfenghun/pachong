package pachong.play.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pachong.play.HttpClientDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

public class baiduShouLu implements PageProcessor {
	private Logger Logger=LoggerFactory.getLogger(this.getClass());
	private Site site = Site.me().addHeader("","").setSleepTime(2000);

	@Override
	public void process(Page page) {
		Logger.info(page.getHtml().toString());
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

		Spider.create(new baiduShouLu()).setDownloader(new HttpClientDownloader())
		.addUrl("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=https%3A%2F%2Fwww.bkw.cn%2Fjlyks%2Fzixun%2F&oq=2019%25E5%25B9%25B42%25E6%259C%2588%25E6%25B2%25B3%25E5%258D%2597%25E7%259B%2591%25E7%2590%2586%25E5%2591%2598%25E8%2580%2583%25E8%25AF%2595%25E6%2588%2590%25E7%25BB%25A9%25E6%259F%25A5%25E8%25AF%25A2%25E6%2597%25B6%25E9%2597%25B4&rsv_pq=fc0644200002573e&rsv_t=fa71Vz%2Fpl3wbcEm%2FVt%2FQ8CkL9MzR3tlQY0Tr13qIsl44ufo0hFRL7KmJf2E&rqlang=cn&rsv_enter=0&inputT=1496&rsv_sug3=9&rsv_sug1=6&rsv_sug7=000&bs=2019%E5%B9%B42%E6%9C%88%E6%B2%B3%E5%8D%97%E7%9B%91%E7%90%86%E5%91%98%E8%80%83%E8%AF%95%E6%88%90%E7%BB%A9%E6%9F%A5%E8%AF%A2%E6%97%B6%E9%97%B4")
//		.thread(2)
		.run();
	}
}
