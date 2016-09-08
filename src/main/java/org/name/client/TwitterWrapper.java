package org.name.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class TwitterWrapper {
	private HttpTextClient client = new HttpTextClient();
	private static final Pattern FOLLOWERS_PATTERN = Pattern.compile("followers_count&quot;\\:\\d+,");
	
	public Integer getNumberFollowers(String twitterUrl) {
		String text = client.getBlogText(twitterUrl);
		if(StringUtils.isEmpty(text)){
			return null;
		}
		
		Pattern intPattern = Pattern.compile("\\d+");
		Matcher m = FOLLOWERS_PATTERN.matcher(text);
		if(m.find()){
			try{
				String match = m.group();
				Matcher followersCountStringMatcher = intPattern.matcher(match);
				followersCountStringMatcher.find();
				String 	followersCountString = followersCountStringMatcher.group(); //getting lazy
				return Integer.parseInt(followersCountString);
			} catch (Exception e) {
				return null;
			}
		}
		
		return null;
	}
}
