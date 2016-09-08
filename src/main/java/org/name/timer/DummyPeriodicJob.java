package org.name.timer;

import org.name.logic.SocialMediaLogic;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class DummyPeriodicJob {
	@Setter private SocialMediaLogic socialMediaLogic;
	public void run() {
		log.info("Running job");
	}
}
