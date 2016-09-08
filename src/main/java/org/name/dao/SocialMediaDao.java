package org.name.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.name.business.Blogger;
import org.name.business.SocialMediaData;
import org.name.entity.SocialMediaDataEntity;

public interface SocialMediaDao {
	public SocialMediaData addSocialMediaEntry(SocialMediaData data);
	public List<SocialMediaDataEntity> search(String searchTerm);
	public void addCrawlResults(String id, 
			HashMap<String, String> emailMap, HashMap<String, String> twitterMap,
			HashMap<String, String> instaMap);
	public void setForCrawl(String id, String blogUrl);
	public List<String> getInProgress();
	public List<Map<?,?>> getCrawled(String searchTerm);
	public List<Blogger> getBlogsToCrawl();
	public void updateCrawlTime(String id);
	public void resetCrawlTime(String id);
}
