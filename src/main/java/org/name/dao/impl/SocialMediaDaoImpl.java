package org.name.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Setter;
import lombok.SneakyThrows;

import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.name.business.Blogger;
import org.name.business.SocialMediaData;
import org.name.dao.SocialMediaDao;
import org.name.dao.mybatis.DummyDataMapper;
import org.name.entity.SocialMediaDataEntity;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.util.StringUtils;

public class SocialMediaDaoImpl implements SocialMediaDao {
	@Setter
	private DummyDataMapper dummyDataMapper;

	@Override
	public SocialMediaData addSocialMediaEntry(SocialMediaData data) {
		Date sampleDate = new Date();
		String instagramHandle = null;
		String twitterHandle = null;
		if (data.getInstagramData() != null) {
			instagramHandle = data.getInstagramData().getHandle();
		}
		if (data.getTwitterData() != null) {
			twitterHandle = data.getTwitterData().getHandle();
		}

		dummyDataMapper.addBlog(data.getId(), data.getBlogUrl(),
				instagramHandle, twitterHandle, data.getEmail());

		if (instagramHandle != null) {
			dummyDataMapper.addInstagramData(instagramHandle, data
					.getInstagramData().getFollowers(), sampleDate);
		}

		if (twitterHandle != null) {
			dummyDataMapper.addTwitterData(twitterHandle, data.getTwitterData()
					.getFollowers(), sampleDate);
		}

		return data;
	}

	@Override
	public List<SocialMediaDataEntity> search(String searchTerm) {
		return dummyDataMapper.getDummyData(searchTerm);
	}

	@Override
	public void addCrawlResults(String id,
			HashMap<String, String> emailMap,
			HashMap<String, String> twitterMap, HashMap<String, String> instaMap) {
		
		for(Map.Entry<String, String> entry : emailMap.entrySet()) {
			dummyDataMapper.addCrawledEmail(id, entry.getKey(), entry.getValue());
		}
		
		for(Map.Entry<String, String> entry : instaMap.entrySet()) {
			dummyDataMapper.addCrawledInsta(id, entry.getKey(), entry.getValue());
		}
		
		for(Map.Entry<String, String> entry : twitterMap.entrySet()) {
			dummyDataMapper.addCrawledTwitter(id, entry.getKey(), entry.getValue());
		}
		
	}

	@Override
	public void setForCrawl(String id, String blogUrl) {
		dummyDataMapper.setForCrawl(id, blogUrl);
	}

	@Override
	public List<String> getInProgress() {
		return dummyDataMapper.getInProgress();
	}

	@Override
	public List<Map<?, ?>> getCrawled(String searchTerm) {
		List<Blogger> bloggers = dummyDataMapper.searchBloggers(searchTerm);
		List<Map<?, ?>> maps = new ArrayList<Map<?, ?>>();
		for (Blogger blogger : bloggers) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("id", blogger.getId());
			returnMap.put("homepage", blogger.getBlogUrl());
			returnMap.put("emails",
					dummyDataMapper.getCrawledEmails(blogger.getId()));
			returnMap.put("instagrams",
					dummyDataMapper.getCrawledInstas(blogger.getId()));
			returnMap.put("twitters",
					dummyDataMapper.getCrawledTwitters(blogger.getId()));
			maps.add(returnMap);
		}
		return maps;
	}

	@Override
	public List<Blogger> getBlogsToCrawl() {
		return dummyDataMapper.getBlogsToCrawl();
	}

	@Override
	public void updateCrawlTime(String id) {
		dummyDataMapper.updateCrawlTime(id);
	}

	@Override
	public void resetCrawlTime(String id) {
		dummyDataMapper.resetCrawlTime(id);
	}
}
