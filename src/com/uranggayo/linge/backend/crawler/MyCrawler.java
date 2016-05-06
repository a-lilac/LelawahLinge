package com.uranggayo.linge.backend.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler{
	/**
	 * Ekstensi file yang akan difilter
	 */
	private final static Pattern FILTERS =  Pattern.compile(
			".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|))$");
	
	
	// Sekarang, ayo tulis ke text file
	private String crawlStorageFolder = "/home/xhaa/crawledSites/";
	
	//String file = crawlStorageFolder+"CrawledPages.txt";
	private String file;
	private String[] domainsToBeCrawled = {
			"http:///www.lintasgayo.com"
			//"http://www.leuserantara.com"
	};
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url){
		String href = url.getURL().toLowerCase();
		if(FILTERS.matcher(href).matches()){
			return false;
		}
		if(url.toString().endsWith(".html")){
			for(String domainToBeCrawled : domainsToBeCrawled){
				if(href.startsWith(domainToBeCrawled));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void visit(Page page){
			
		// content/news spesific variables
		String author = null;
		String topic;
		String title;
		String content = null; // variable ini butuh pengolahan lebih lanjut untuk data mining!
		HashMap <String, WebURL> imagesA = new HashMap<String, WebURL>();
		HashMap <String, String> images = new HashMap<String, String>(); //for testing
		String date = null;
		String source;
		String url;
		
		PrintWriter printWriter = null;
		FileWriter fileWriter = null;
		
		url = page.getWebURL().getURL();
				
		//System.out.println("URL: "+url);
		
		if(page.getParseData() instanceof HtmlParseData){
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml(); //I got html document.
			
			// JSoup is in action
			Document document =  Jsoup.parseBodyFragment(html);
			title = document.title();
			Element article = document.select("article").first();
			Elements imageEl = article.select("img[alt]"); // image in article
			String image = imageEl.attr("alt"); 
			String imageSource = imageEl.attr("src"); // source to image in article

			if(url.contains("lintasgayo")){
				//file = crawlStorageFolder+"/lintasgayo/lintasgayo.txt";
				
				// tangani konten yang didapat dari Lintas Gayo
				
				// Judul
				if(title.endsWith("| Lintas Gayo") || title.endsWith("| Media Online Gayo")){
					title = title.replace("| Lintas Gayo", "");
					title = title.replace("| Media Online Gayo", "");
				}
				// penulis berita
				author = article.select("span.entry-author").text().toString();
				
				//tanggal berita
				date = article.select("span.entry-date").text().toString();
				
				// Isi berita
				content = article.select("p").text().toString();
				//gambar pada berita
				//TODO:URL harus diarahkan langsung pada gambar
				images.put(image.toString(), imageSource.toString());
				
			}else if(url.contains("leuserantara") && title != null){
				file = crawlStorageFolder+"/leuserantara/leuserantara.txt";
				
				//tangani konten dari  Leuser Antara
			}
			
			// Data telah diolah, sekarang cetak dan simpan.
			System.out.println(title);
			System.out.println(author);
			System.out.println(date);
			System.out.println(url);
			System.out.println(content);
			System.out.println(images.toString());
			System.out.println();
			/*
			try{
				fileWriter  = new FileWriter(file, true); //true means appending enabled!!
				printWriter = new PrintWriter(fileWriter);
				
				printWriter.println(title);
				printWriter.println(date);
				printWriter.println(url);
				printWriter.println(content);
				printWriter.println();
				printWriter.println();
				
			}catch(IOException ioe){
				System.out.println("Tidak bisa menulis ke file");
				ioe.printStackTrace();
			}
			*/
		}
		//printWriter.close();
	}
	/**
	 * Method ini digunakan untuk memperbaiki/menormalkan content yang diperoleh 
	 */
	public void normalizeContent(String content){
		 //= content;
		return;
	}
}
