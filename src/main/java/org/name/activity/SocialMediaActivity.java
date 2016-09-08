package org.name.activity;

import java.util.List;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.name.business.SocialMediaData;
import org.name.entity.SocialMediaDataEntity;
import org.name.logic.SocialMediaLogic;
import org.name.request.ImportBlogRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j
@Controller
@RequestMapping(value = "/social-media")
@Transactional
public class SocialMediaActivity {

	private static final String APPLICATION_JSON = "application/json";
	@Setter private SocialMediaLogic socialMediaLogic;
	
	@RequestMapping(method = RequestMethod.POST, value = "/import", produces = { APPLICATION_JSON })
    @ResponseBody
    public SocialMediaDataEntity create(@RequestBody ImportBlogRequest r) {
    	SocialMediaData data = socialMediaLogic.importBlog(r.getBlogUrl());
    	if(data == null){
    		return null;
    	}
    	SocialMediaDataEntity entity = new SocialMediaDataEntity();
    	entity.setBlogUrl(data.getBlogUrl());
    	entity.setEmailAddress(data.getEmail());
    	entity.setId(data.getId());
    	if(data.getInstagramData() != null){
    		entity.setInstagramhandle(data.getInstagramData().getHandle());
    		entity.setInstagramFollowerCount(data.getInstagramData().getFollowers());
    	}
    	if(data.getTwitterData() != null) {
    		entity.setTwitterHandle(data.getTwitterData().getHandle());
    		entity.setTwitterFollowerCount(data.getTwitterData().getFollowers());
    	}
    	return entity;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/search", produces = { APPLICATION_JSON })
    @ResponseBody
    public List<SocialMediaDataEntity> search(@RequestParam(required = true) String searchTerm) {
    	return socialMediaLogic.search(searchTerm);
    }	
    
    @RequestMapping(method = RequestMethod.DELETE, value = "re-import/{bloggerId}", produces = { APPLICATION_JSON })
    @ResponseBody
    public List<SocialMediaData> get(@PathVariable("bloggerId") String bloggerId) {
    	return null;
    }
}
