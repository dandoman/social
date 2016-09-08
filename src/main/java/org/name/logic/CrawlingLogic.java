package org.name.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.name.business.Blogger;
import org.name.business.SocialMediaData;
import org.name.client.HttpTextClient;
import org.name.dao.SocialMediaDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Log4j
public class CrawlingLogic {

	private ThreadLocal<Set<String>> visitedUrls = new ThreadLocal<Set<String>>();
	private ThreadLocal<Map<String, String>> foundEmails = new ThreadLocal<Map<String, String>>();
	private ThreadLocal<Map<String, String>> foundTwitters = new ThreadLocal<Map<String, String>>();
	private ThreadLocal<Map<String, String>> foundInstas = new ThreadLocal<Map<String, String>>();

	private ExecutorService executor = Executors.newCachedThreadPool();

	@Setter
	private HttpTextClient blogClient;
	@Setter
	private ParsingLogic parsingLogic = new ParsingLogic();
	@Setter SocialMediaDao dao;

	public String stageLaterCrawl(String blogUrl) {
		String id = UUID.randomUUID().toString();
		dao.setForCrawl(id, blogUrl);
		return id;
	}
	
	public void crawlBlog(String id, String blogUrl) {
		try {
			constructThreadLocals();
			performCrawl(blogUrl, 0);
			dao.addCrawlResults(id, new HashMap<String, String>(foundEmails.get()),
					new HashMap<String, String>(foundTwitters.get()), new HashMap<String, String>(foundInstas.get()));
		} finally {
			deconstructThreadLocals();
		}
	}

	private void performCrawl(String blogUrl, int depth) {
		if (depth >= 4 || visitedUrls.get().contains(blogUrl)) {
			return;
		}
		
		visitedUrls.get().add(blogUrl);
		String text = blogClient.getBlogText(blogUrl);
		List<String> urls = getInternalLinks(text, blogUrl);
		addData(text, blogUrl);

		for (String linkUrl : urls) {
			performCrawl(linkUrl, depth + 1);
		}
	}

	private void addData(String text, String blogUrl) {
		if (StringUtils.isEmpty(text)) {
			return;
		}
		List<String> emails = parsingLogic.extractEmails(text);
		for(String email : emails) {
			foundEmails.get().put(email, blogUrl);
		}
		List<String> twitters = parsingLogic.extractTwitters(text);
		for(String handle : twitters) {
			foundTwitters.get().put(handle, blogUrl);
		}
		List<String> instas = parsingLogic.extractInstas(text);
		for(String handle : instas) {
			foundInstas.get().put(handle, blogUrl);
		}
	}

	public List<String> getInternalLinks(String text, String url) {
		// Want to make this breadth first
		Pattern ghettoLinkPattern = Pattern
				.compile("href=(\"|')?(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?(\"|')?");
		Pattern domainPatter = Pattern.compile("(\\w|-|_)+\\.(com|net|org)");
		Matcher m = domainPatter.matcher(url);
		Set<String> links = new HashSet<String>();
		String domain = m.find() ? m.group() : null;
		if (domain == null || text == null) {
			return new ArrayList<String>(links);
		}

		Matcher linkMatcher = ghettoLinkPattern.matcher(text);
		while (linkMatcher.find()) {
			String link = linkMatcher.group();
			link = link.trim();
			link = link.replaceAll("href=", "");
			link = link.replaceAll("\"", "");
			link = link.replaceAll("'", "");
			link = link.replaceAll("(\\s|<|>|#|;)", "");
			if (link.contains(domain)) {
				links.add(link);
			}
		}
		return new ArrayList<String>(links);
	}

	private void constructThreadLocals() {
		visitedUrls.set(new HashSet<String>());
		foundEmails.set(new HashMap<String, String>());
		foundTwitters.set(new HashMap<String, String>());
		foundInstas.set(new HashMap<String, String>());
	}

	private void deconstructThreadLocals() {
		visitedUrls.remove();
		foundEmails.remove();
		foundTwitters.remove();
		foundInstas.remove();
	}

	public List<String> getInProgress() {
		return dao.getInProgress();
	}

	public List<Map<?, ?>> search(String searchTerm) {
		return dao.getCrawled("%" + searchTerm + "%");
	}
	
	public void periodicCrawl() {
		List<Blogger> crawlList = dao.getBlogsToCrawl();
		log.info("Found " + crawlList.size() + " blogs to crawl");
		for(final Blogger blogger : crawlList) {
			executor.submit(new Runnable(){
				@Override
				public void run() {
					crawlAndUpdateBlog(blogger);
				}
			});
		}
	}
	
	private void crawlAndUpdateBlog(Blogger blogger) {
		dao.updateCrawlTime(blogger.getId());
		try {
			crawlBlog(blogger.getId(), blogger.getBlogUrl());
		} catch (Throwable t) {
			dao.resetCrawlTime(blogger.getId());
			throw t;
		}
	}
}
