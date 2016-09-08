package org.name.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingLogic {

	private static final Pattern TWITTER_HANDLE_PATTERN = Pattern.compile("twitter\\.com/(#!/)?\\w*");
	private static final Pattern INSTAGRAM_HANDLE_PATTERN = Pattern.compile("instagram\\.com/\\w*");
	
	public List<String> extractTwitters(String text) {
		List<String> handles = new ArrayList<String>();
		Matcher twitterMatcher = TWITTER_HANDLE_PATTERN.matcher(text);
		while(twitterMatcher.find()) {
			handles.add(twitterMatcher.group().replace("#!/", ""));
		}
		return handles;
	}

	public List<String> extractInstas(String text) {
		List<String> handles = new ArrayList<String>();
		Matcher instaMatcher = INSTAGRAM_HANDLE_PATTERN.matcher(text);
		while(instaMatcher.find()) {
			handles.add(instaMatcher.group());
		}
		return handles;
	}
	
	public List<String> extractEmails(String text) {
		List<String> emails = new ArrayList<String>();
		
		String firstPartPattern = "(\\w|_|-|\\+|\\.|\\s*((d|D)(o|O)(t|T))\\s*)";
		String atWithSpacesbetweenPattern = "(\\s*(\\(|\\[|<|\\{)?\\s*)\\s*(@|((\\b(a|A)(t|T))))\\s*(\\s*(\\)|\\]|>|\\})?\\s*)";
		String hostnamePatternWithSpaces = "\\s*\\w+\\s*"; 
		String comDomain = "(c|C)(o|O)(m|M)";
		String orgDomain = "(o|O)(r|R)(g|G)";
		String netDomain = "(n|N)(e|E)(t|T)";
		String topLevelDomain = "(" + comDomain + "|" + orgDomain + "|" + netDomain + ")";
		String dotWithSpacesBetween = "(\\(|\\[|<|\\{)?\\s*(\\.|\\(?((d|D)(o|O)(t|T))\\)?)\\s*(\\)|]|>|\\})?";
		
		Pattern wideMatchPattern = Pattern.compile(firstPartPattern + "+\\s*" + atWithSpacesbetweenPattern + hostnamePatternWithSpaces + dotWithSpacesBetween + "\\s*" + topLevelDomain);
		
		Matcher emailMatcher = wideMatchPattern.matcher(text);
		while(emailMatcher.find()){
			String rawEmail = emailMatcher.group();
			rawEmail = rawEmail.replaceAll(dotWithSpacesBetween, ".");
			rawEmail = rawEmail.replaceAll(atWithSpacesbetweenPattern, "@");
			rawEmail = rawEmail.replaceAll(comDomain, "com");
			rawEmail = rawEmail.replaceAll(netDomain, "net");
			rawEmail = rawEmail.replaceAll(orgDomain, "org");
			rawEmail = rawEmail.replaceAll("\\s", "");
			rawEmail = rawEmail.toLowerCase();
			emails.add(rawEmail);
		}
		return emails;
	}

}
