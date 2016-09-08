package org.name.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.name.business.InstagramData;
import org.name.business.SocialMediaData;
import org.name.business.TwitterData;
import org.name.client.HttpTextClient;
import org.name.client.InstagramWrapper;
import org.name.client.TwitterWrapper;
import org.name.dao.SocialMediaDao;
import org.name.entity.SocialMediaDataEntity;
import org.springframework.util.StringUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class SocialMediaLogic {
	@Setter private SocialMediaDao socialMediaDao;
	@Setter private TwitterWrapper twitterWrapper;
	@Setter private InstagramWrapper instagramWrapper;
	@Setter private HttpTextClient blogClient;
	private static final Pattern TWITTER_HANDLE_PATTERN = Pattern.compile("twitter.com/(#!/)?\\w*");
	private static final Pattern INSTAGRAM_HANDLE_PATTERN = Pattern.compile("instagram.com/\\w*");
	private static final String INSTAGRAM_KEY = "instagram";
	private static final String TWITTER_KEY = "twitter";
	private static final String EMAIL_KEY = "email";
	
	private Set<String> visitedUrls = new HashSet<String>();
	
	public SocialMediaData importBlog(String blogUrl) {
		Map<String,String> result = extractHandles(blogUrl);
		if(result.isEmpty()){
			return null;
		}
		
		SocialMediaData data = new SocialMediaData();
		data.setId(UUID.randomUUID().toString());
		data.setBlogUrl(blogUrl);
		
		
		if(result.containsKey(INSTAGRAM_KEY)){
			String instaUrl = result.get(INSTAGRAM_KEY);
			Integer noFollowers = instagramWrapper.getNumberFollowers(instaUrl);
			String [] tokens = instaUrl.split("/");
			String instaHandle = instaUrl;
			if(tokens != null) {
				instaHandle = tokens[tokens.length - 1];
			}
			
			if(noFollowers != null){
				InstagramData instaData = new InstagramData(instaHandle, noFollowers, DateTime.now().toDate());
				data.setInstagramData(instaData);
			}
		}
		
		if(result.containsKey(TWITTER_KEY)){
			String twitterUrl = result.get(TWITTER_KEY);
			Integer noFollowers = twitterWrapper.getNumberFollowers(twitterUrl);
			String [] tokens = twitterUrl.split("/");
			String twitterHandle = twitterUrl;
			if(tokens != null) {
				twitterHandle = tokens[tokens.length - 1];
			}
			if(noFollowers != null){
				TwitterData twitterData = new TwitterData(twitterHandle, noFollowers, DateTime.now().toDate());
				data.setTwitterData(twitterData);
			}
		}
		
		if(result.containsKey(EMAIL_KEY)){
			data.setEmail(result.get(EMAIL_KEY));
		}
		
		String id = UUID.randomUUID().toString();
		socialMediaDao.setForCrawl(id, blogUrl);
		return socialMediaDao.addSocialMediaEntry(data);
	}
	
	private SocialMediaData getDataFromKeys(Map<String, String> result, String blogUrl) {
		SocialMediaData data = new SocialMediaData();
		data.setId(UUID.randomUUID().toString());
		data.setBlogUrl(blogUrl);
		
		
		if(result.containsKey(INSTAGRAM_KEY)){
			String instaUrl = result.get(INSTAGRAM_KEY);
			Integer noFollowers = instagramWrapper.getNumberFollowers(instaUrl);
			String [] tokens = instaUrl.split("/");
			String instaHandle = instaUrl;
			if(tokens != null) {
				instaHandle = tokens[tokens.length - 1];
			}
			
			if(noFollowers != null){
				InstagramData instaData = new InstagramData(instaHandle, noFollowers, DateTime.now().toDate());
				data.setInstagramData(instaData);
			}
		}
		
		if(result.containsKey(TWITTER_KEY)){
			String twitterUrl = result.get(TWITTER_KEY);
			Integer noFollowers = twitterWrapper.getNumberFollowers(twitterUrl);
			String [] tokens = twitterUrl.split("/");
			String twitterHandle = twitterUrl;
			if(tokens != null) {
				twitterHandle = tokens[tokens.length - 1];
			}
			if(noFollowers != null){
				TwitterData twitterData = new TwitterData(twitterHandle, noFollowers, DateTime.now().toDate());
				data.setTwitterData(twitterData);
			}
		}
		
		if(result.containsKey(EMAIL_KEY)){
			data.setEmail(result.get(EMAIL_KEY));
		}
		
		return data;
	}
	
	public List<SocialMediaData> getDummyAllData() {
		return null;
	}

	public List<SocialMediaData> getDummyDataById(String id) {
		return null;
	}
	
	private Map<String, String> extractHandles(String blogUrl) {
		Map<String,String> resultMap = new HashMap<String,String>();
		String  text = blogClient.getBlogText(blogUrl);
		if(StringUtils.isEmpty(text)){
			return resultMap;
		}
		Matcher twitterMatcher = TWITTER_HANDLE_PATTERN.matcher(text);
		Matcher instagramMatcher = INSTAGRAM_HANDLE_PATTERN.matcher(text);
		
		if(twitterMatcher.find()){
			resultMap.put(TWITTER_KEY,twitterMatcher.group().replace("#!/", ""));
		}
		if(instagramMatcher.find()){
			resultMap.put(INSTAGRAM_KEY,instagramMatcher.group());
		}
		String email = emailFixing(text);
		if(email != null){
			resultMap.put(EMAIL_KEY, email);
		}
		
		return resultMap;
	}

	public List<SocialMediaDataEntity> search(String searchTerm) {
		searchTerm = "%" + searchTerm + "%";
		List<SocialMediaDataEntity> data = socialMediaDao.search(searchTerm);
		return data;
	}
	
	protected String emailFixing(String email) {
		String firstPartPattern = "(\\w|_|-|\\+|\\.|\\s*((d|D)(o|O)(t|T))\\s*)";
		String atWithSpacesbetweenPattern = "(\\s*(\\(|\\[|<|\\{)?\\s*)\\s*(@|((\\b(a|A)(t|T))))\\s*(\\s*(\\)|\\]|>|\\})?\\s*)";
		String hostnamePatternWithSpaces = "\\s*\\w+\\s*"; 
		String comDomain = "(c|C)(o|O)(m|M)";
		String orgDomain = "(o|O)(r|R)(g|G)";
		String netDomain = "(n|N)(e|E)(t|T)";
		String topLevelDomain = "(" + comDomain + "|" + orgDomain + "|" + netDomain + ")";
		String dotWithSpacesBetween = "(\\(|\\[|<|\\{)?\\s*(\\.|\\(?((d|D)(o|O)(t|T))\\)?)\\s*(\\)|]|>|\\})?";
		
		Pattern wideMatchPattern = Pattern.compile(firstPartPattern + "+\\s*" + atWithSpacesbetweenPattern + hostnamePatternWithSpaces + dotWithSpacesBetween + "\\s*" + topLevelDomain);
		
		Pattern pattern1 = Pattern.compile("(\\w|_|-|\\+|\\.|\\s*((d|D)(o|O)(t|T))\\s*)+\\s*(\\s*(\\(|\\[|<|\\{)?\\s*)\\s*(@|(((a|A)(t|T))))\\s*(\\s*(\\)|\\]|>|\\})?\\s*)\\s*\\w+\\s*(\\(|\\[|<|\\{)?\\s*(\\.|\\(?((d|D)(o|O)(t|T))\\)?)\\s*(\\)|]|>|\\})?\\s*((c|C)(o|O)(m|M)|(o|O)(r|R)(g|G)|(n|N)(e|E)(t|T))");
		Matcher emailMatcher = wideMatchPattern.matcher(email);
		if(emailMatcher.find()){
			String rawEmail = emailMatcher.group();
			rawEmail = rawEmail.replaceAll(dotWithSpacesBetween, ".");
			rawEmail = rawEmail.replaceAll(atWithSpacesbetweenPattern, "@");
			rawEmail = rawEmail.replaceAll(comDomain, "com");
			rawEmail = rawEmail.replaceAll(netDomain, "net");
			rawEmail = rawEmail.replaceAll(orgDomain, "org");
			rawEmail = rawEmail.replaceAll("\\s", "");
			rawEmail = rawEmail.toLowerCase();
			return rawEmail;
		}
		
		return null;
	}
	
	public SocialMediaData crawlBlog(String url, int depth) {
		if(depth >= 5 || visitedUrls.contains(url)) {
			return null;
		}
		System.out.println("DEPTH=" + depth + " Crawling url: " + url);
		visitedUrls.add(url);
		String text = blogClient.getBlogText(url);
		List<String> urls = getInternalLinks(text, url);
		SocialMediaData data = getData(text, url);
		if(data != null && data.getEmail() != null) {
			System.out.println("Found info at url: " + url);
			return data;
		} 
		
		for(String linkUrl : urls) {
			SocialMediaData d =  crawlBlog(linkUrl, depth + 1);
			if(d != null && d.getEmail() != null) {
				return d;
			}
		}
		
		return null;
	}

	public List<String> getInternalLinks(String text, String url) {
		//Want to make this breadth first
		Pattern ghettoLinkPattern = Pattern.compile("href=(\"|')?(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?(\"|')?");
		Pattern domainPatter = Pattern.compile("(\\w|-|_)+\\.(com|net|org)");
		Matcher m = domainPatter.matcher(url);
		Set<String> links = new HashSet<String>();
		String domain = m.find() ? m.group() : null;
		if(domain == null || text == null) {
			return new ArrayList<String>(links);
		}
		
		Matcher linkMatcher = ghettoLinkPattern.matcher(text);
		while(linkMatcher.find()) {
			String link = linkMatcher.group();
			link = link.trim();
			link = link.replaceAll("href=", "");
			link = link.replaceAll("\"", "");
			link = link.replaceAll("'", "");
			link = link.replaceAll("(\\s|<|>|#|;)", "");
			if(link.contains(domain)) {
				links.add(link);
			}
		}
		return new ArrayList<String>(links);
	}

	private SocialMediaData getData(String text, String blogUrl) {
		if(StringUtils.isEmpty(text)){
			return null;
		}
		Matcher twitterMatcher = TWITTER_HANDLE_PATTERN.matcher(text);
		Matcher instagramMatcher = INSTAGRAM_HANDLE_PATTERN.matcher(text);
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(twitterMatcher.find()){
			resultMap.put(TWITTER_KEY,twitterMatcher.group().replace("#!/", ""));
		}
		if(instagramMatcher.find()){
			resultMap.put(INSTAGRAM_KEY,instagramMatcher.group());
		}
		String email = emailFixing(text);
		if(email != null){
			resultMap.put(EMAIL_KEY, email);
		}
		
		return getDataFromKeys(resultMap, blogUrl);
	}
	
	public static void main(String [] args) {
		SocialMediaLogic logic = new SocialMediaLogic();
		HttpTextClient client = new HttpTextClient();
		InstagramWrapper wrapper = new InstagramWrapper();
		TwitterWrapper twitter = new TwitterWrapper();
		logic.setInstagramWrapper(wrapper);
		logic.setTwitterWrapper(twitter);
		logic.setBlogClient(client);
		SocialMediaData data = logic.crawlBlog("http://montrealmom.com", 0);
		System.out.print(data);
	}
}
