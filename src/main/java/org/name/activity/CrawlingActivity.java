package org.name.activity;

import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.name.business.SocialMediaData;
import org.name.entity.SocialMediaDataEntity;
import org.name.logic.CrawlingLogic;
import org.name.request.ImportBlogRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/social-media")
@Transactional
public class CrawlingActivity {

	private static final String APPLICATION_JSON = "application/json";
	@Setter private CrawlingLogic crawlingLogic;
	
	@RequestMapping(method = RequestMethod.POST, value = "/crawl", produces = { APPLICATION_JSON })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public String create(@RequestBody ImportBlogRequest r) {
    	return crawlingLogic.stageLaterCrawl(r.getBlogUrl());
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/crawl/waiting", produces = { APPLICATION_JSON })
    @ResponseBody
    public List<String> getInProgress() {
    	return crawlingLogic.getInProgress();
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/crawl", produces = { APPLICATION_JSON })
    @ResponseBody
    public List<Map<?, ?>> search(@RequestParam(required = true) String searchTerm) {
    	return crawlingLogic.search(searchTerm);
    }	
}
