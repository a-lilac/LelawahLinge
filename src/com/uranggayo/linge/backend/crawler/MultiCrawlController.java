package com.uranggayo.linge.backend.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.nc2.dt.radial.RadialDatasetSweepAdapter.MyRadialVariableAdapter;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MultiCrawlController {
	  private static final Logger logger = LoggerFactory.getLogger(MultiCrawlController.class);
	  
	public static void main(String[] args) throws Exception  {
		
		String crawlStorageFolder = "/home/xhaa/crawledSites/";
		int numOfCrawlers = 20;
		
		// extra configs
		int maxDepthOfCrawling = 5;
		Integer maxPagesToFetch = null;
		int politenessDelay = 0;
		String userAgentString = "Kute Kering";
		
		CrawlConfig config1 = new CrawlConfig();
		//CrawlConfig config2 = new CrawlConfig();
		
		config1.setCrawlStorageFolder(crawlStorageFolder+"/lintasgayo");
		//config2.setCrawlStorageFolder(crawlStorageFolder+"/crawler2");
		
		// apply extra configs
		config1.setMaxDepthOfCrawling(maxDepthOfCrawling);
		//config2.setMaxDepthOfCrawling(maxDepthOfCrawling);
		if (maxPagesToFetch != null){
			config1.setMaxPagesToFetch(maxPagesToFetch);
			//config2.setMaxPagesToFetch(maxPagesToFetch);
		}
		if (politenessDelay != 0){
			config1.setPolitenessDelay(politenessDelay);
			//config2.setPolitenessDelay(politenessDelay);
		}
		config1.setUserAgentString(userAgentString);
		//config2.setUserAgentString(userAgentString);
		
		PageFetcher pageFetcher1 = new PageFetcher(config1);
		//PageFetcher pageFetcher2 = new PageFetcher(config2);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		
		RobotstxtServer robotstxtServer1 = new RobotstxtServer(robotstxtConfig, pageFetcher1);
		//RobotstxtServer robotstxtServer2 = new RobotstxtServer(robotstxtConfig, pageFetcher2);
		
		CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer1);
		//CrawlController controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer2);
		
		controller1.setCustomData("http://www.lintasgayo.com");
		//controller2.setCustomData("http://www.leuserantara.com");
		
		controller1.addSeed("http://www.lintasgayo.com");
		//controller2.addSeed("http://www.leuserantara.com");
		
		controller1.start(MyCrawler.class, numOfCrawlers);
		//controller2.start(MyCrawler.class, numOfCrawlers);
		
		/*
	     * The first crawler will have 5 concurrent threads and the second
	     * crawler will have 7 threads.
	     */
	    controller1.startNonBlocking(MyCrawler.class, 5);
	    //controller2.startNonBlocking(MyCrawler.class, 7);
		
	    controller1.waitUntilFinish();
	    logger.info("Crawler 1 is finished.");
	    System.out.println("Crawler 1 is finished.");
	    
	    //controller2.waitUntilFinish();
	    logger.info("Crawler 2 is finished.");
	    System.out.println("Crawler 2 is finished.");
	}
}
