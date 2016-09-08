package org.name.logic;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class EmailMatchingLogicTest {

	private ParsingLogic logic;
	private Map<String, String> rawToEmailMap = new HashMap<String, String>();
	
    @Before
    public void before() {
    	logic = new ParsingLogic();
    	rawToEmailMap.put("jen at thesuburbanmom dot com", "jen@thesuburbanmom.com");
    	rawToEmailMap.put("leahsegedie(at)bookieboo(dot)com.", "leahsegedie@bookieboo.com");
    	rawToEmailMap.put("thriftynwmom (at) gmail (dot) com.", "thriftynwmom@gmail.com");
    	rawToEmailMap.put("wholenewmom (at) gmail (dot) com", "wholenewmom@gmail.com");
    	rawToEmailMap.put("colleenpence at socialmediamentoring dot com", "colleenpence@socialmediamentoring.com");
    	rawToEmailMap.put("julee_morrison (at) yahoo (dot) com", "julee_morrison@yahoo.com");
    	rawToEmailMap.put("info(at)dallasmomsblog(dot)org", "info@dallasmomsblog.org");
    	rawToEmailMap.put("thedudemom[at]gmail[dot]com", "thedudemom@gmail.com");
    	rawToEmailMap.put("Heather at hreese @ itsalovelylife . com", "hreese@itsalovelylife.com");
    	rawToEmailMap.put("please email me at valmg2 AT gmail or contact me using the form on this site.", "valmg2@gmail.com");
    	rawToEmailMap.put("crystal<br>{at} simplybeingmommy {dot} com. ", "crystal@simplybeingmommy.com");
    	rawToEmailMap.put("To contact Vicki, you can email her at mum [at] honestmum [dot] com ", "mum@honestmum.com");
    	rawToEmailMap.put("Amy (at) SelfishMom (dot) com ", "Amy@SelfishMom.com");
    	rawToEmailMap.put("theleangreenbean <at> gmail <dot> com. ", "theleangreenbean@gmail.com");
    	rawToEmailMap.put("MEDIA KIT AVAILABLE UPON REQUEST: If you are interested in working together in any capacity email Ellen at Ellen (at) thriftyandchicmom (dot) com ", "Ellen@thriftyandchicmom.com");
    	rawToEmailMap.put("contact me at thepurposefulmom {at}<br>live<br>{dot}<br>com", "thepurposefulmom@live.com");
    	rawToEmailMap.put(" SerendipityandSpice (at) yahoo.com", "SerendipityandSpice@yahoo.com");
    	rawToEmailMap.put("christine(at)thediydreamer(dot)com", "christine@thediydreamer.com");
    	rawToEmailMap.put("Send me an email at aslobcomesclean @ gmail . com (spaces removed)", "aslobcomesclean@gmail.com");
    	rawToEmailMap.put("writingchapterthree [at] gmail [dot] com", "writingchapterthree@gmail.com");
    	rawToEmailMap.put("Contact me at preshusme at gmail dot com.", "preshusme@gmail.com");
    	rawToEmailMap.put("Feel free to contact me using the form below or email me directly at ginny (at) organizinghomelife.com", "ginny@organizinghomelife.com");
    	rawToEmailMap.put("<p>Please email me at <a href=\"mailto:info@frugalmomeh.com\">info@frugalmomeh.com</a> to request my media kit and discuss partnership opportunities. I look forward to hearing from you!</p>", "info@frugalmomeh.com");
    }

    @Test
    public void test() {
    	int matches = 0;
    	
        for(Map.Entry<String, String> entry : rawToEmailMap.entrySet()) {
        	String processedEmail = logic.extractEmails(entry.getKey()).isEmpty() ? null : logic.extractEmails(entry.getKey()).get(0);
        	if(processedEmail != null) {
        		System.out.println("***Found: " + processedEmail);
        	}
        	if(entry.getValue().toLowerCase().equals(processedEmail)) {
        		System.out.println("Successfully extracted: " + processedEmail);
        		matches++;
        	} else {
        		System.out.println("Failed to process: " + entry.getValue());
        	}
        }
        
        assertTrue(matches > 18);
        
        List<String> processedEmail = logic.extractEmails("<script async src=\"pagead2.googlesyndication.com/pagead/js/adsbygoogle.js\"></script>");
        if(processedEmail != null && processedEmail.size() > 0) {
        	System.out.println("+++Matched shit again: " + processedEmail);
        }
    }

}
