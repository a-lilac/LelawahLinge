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
		String author = "";
		String topic = "";
		String title = "";
		String content = ""; // variable ini butuh pengolahan lebih lanjut untuk data mining!
		HashMap <String, WebURL> imagesA = new HashMap<String, WebURL>();
		HashMap <String, String> images = new HashMap<String, String>(); //for testing
		String date = "";
		String source = "";
		String url ="";
		
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
			Elements imageEls = article.select("img[alt]"); // image in article
			/**
			 * Percabangan ini digunakan untuk memparsing document html/ news dari situs Lintas Gayo
			 */
			if(url.contains("lintasgayo")){
				file = crawlStorageFolder+"/lintasgayo/lintasgayo.csv";
								
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
				content.replace("\n", "||||");
				
				// TODO tanda tanya berlebihan pada konten.
				//gambar pada berita
				//Sudah OK
				for (Element imageEl: imageEls ){
					String image = imageEl.attr("alt"); 
					String imageSource = imageEl.attr("src"); // source to image in article
					images.put(image.toString(), imageSource.toString());
				}
				
				// TODO Periksa jika ada gambar di awal artikel, caption dari gambar keluarkan dari konten dan masukkan ke caption pada gambar yang ditemuka
				
				
				
			/**
			 * Percabangan untuk memparsing dokumen html/ news yang diperoleh dari Leuser Antara
			 */
			}else if(url.contains("leuserantara") && title != null){
				file = crawlStorageFolder+"/leuserantara/leuserantara.txt";
				
				//tangani konten dari  Leuser Antara
			}
			
			// Data telah diolah, sekarang cetak dan simpan.
			
			// urutan row pada datastore
			// author, title, content, date, topic, images, source, url
			System.out.println(author);
			System.out.println(title);
			System.out.println(content);
			System.out.println(date);
			System.out.println(topic);
			System.out.println(images.toString());
			System.out.println(source);
			System.out.println(url);
			System.out.println();
			
			try{
				fileWriter  = new FileWriter(file, true); //true means appending enabled!!
				printWriter = new PrintWriter(fileWriter);
				
				// tulis ke file CSV
				String splitter = "˛"; //Ogonek - U+02DB
				
				splitter = "¨" ;// Diaresis U+00A8
				//ambil data
				
				// urutan row pada datastore
				// author, title, content, date, topic, images, source, url
				printWriter.print(author+splitter);
				printWriter.print(title+splitter);
				printWriter.print(content+splitter);
				printWriter.print(date+splitter);
				printWriter.print(topic+splitter);
				printWriter.print(images.toString()+splitter);
				printWriter.print(source+splitter);
				printWriter.print(url);
				printWriter.println();
				
			}catch(IOException ioe){
				System.out.println("Tidak bisa menulis ke file");
				ioe.printStackTrace();
			}
		}
		printWriter.close();
		System.out.println("printWriter ditutup");
	}
	/**
	 * Method ini digunakan untuk memperbaiki/menormalkan content yang diperoleh 
	 */
	public void normalizeContent(String content){
		 //= content;
		return;
	}
}
