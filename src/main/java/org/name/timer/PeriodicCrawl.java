package org.name.timer;

import org.name.logic.CrawlingLogic;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class PeriodicCrawl {
	@Setter private CrawlingLogic logic;
	public void run() {
		log.info("Running periodic crawl");
		logic.periodicCrawl();
	}
}
