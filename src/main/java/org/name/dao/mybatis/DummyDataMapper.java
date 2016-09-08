package org.name.dao.mybatis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.name.business.Blogger;
import org.name.business.CrawledEmail;
import org.name.business.CrawledInsta;
import org.name.business.CrawledTwitter;
import org.name.business.SocialMediaData;
import org.name.entity.SocialMediaDataEntity;

public interface DummyDataMapper {
	@Insert("INSERT INTO blogs(id,blog_url,instagram_handle,twitter_handle,email_address) "
			+ "VALUES (#{id},#{blogUrl},#{instagramHandle},#{twitterHandle},#{emailAddress})")
	public int addBlog(@Param("id") String id,@Param("blogUrl") String blogUrl, 
			@Param("instagramHandle") String instagramHandle,@Param("twitterHandle") String twitterHandle,
			@Param("emailAddress") String emailAddress);
	
	@Insert("INSERT INTO twitter_data(handle,number_followers,sample_date) VALUES (#{handle},#{noFollowers},#{sampleDate})")
	public int addTwitterData(@Param("handle") String handle, @Param("noFollowers") int noFollowers, 
			@Param("sampleDate") Date sampleDate);
	
	@Insert("INSERT INTO instagram_data(handle,number_followers,sample_date) VALUES (#{handle},#{noFollowers},#{sampleDate})")
	public int addInstagramData(@Param("handle") String handle, @Param("noFollowers") int noFollowers, 
			@Param("sampleDate") Date sampleDate);
	
	@Select("SELECT id, blog_url as blogUrl, instagram_handle as instagramHandle,"
			+ " twitter_handle as twitterHandle, email_address as emailAddress, "
			+ "(select number_followers from twitter_data where handle = blogs.twitter_handle and sample_date = (select max(sample_date) from twitter_data where handle = blogs.twitter_handle)) as twitterFollowerCount,"
			+ "(select number_followers from instagram_data where handle = blogs.instagram_handle and sample_date = (select max(sample_date) from instagram_data where handle = blogs.instagram_handle)) as instagramFollowerCount "
			+ "FROM blogs WHERE blog_url like #{searchTerm} OR instagram_handle like #{searchTerm} or twitter_handle like #{searchTerm} or email_address like #{searchTerm}")
	public List<SocialMediaDataEntity> getDummyData(@Param("searchTerm") String searchTerm);
	
	//Example for dynamic queries
	@SelectProvider(type = DummyQueryProvider.class,  method = "getDataByIdSQL")
	public List<SocialMediaDataEntity> getDummyDataById(@Param(value = "id") String id);

	@Insert("INSERT INTO bloggers(id,root_page_url) VALUES (#{id},#{blogUrl})")
	public int setForCrawl(@Param(value = "id") String id, @Param(value = "blogUrl")String blogUrl);
	
	@Update("UPDATE bloggers SET last_crawled = now() WHERE id = #{id}")
	public int updateCrawlTime(@Param(value = "id") String id);

	@Select("SELECT root_page_url FROM bloggers WHERE last_crawled is null")
	public List<String> getInProgress();

	@Select("SELECT id, root_page_url as blogUrl, last_crawled as lastCrawled FROM bloggers WHERE root_page_url LIKE #{searchTerm}")
	public List<Blogger> searchBloggers(@Param(value = "searchTerm") String searchTerm);

	@Select("SELECT email, url FROM crawled_emails WHERE blogger_id = #{id}")
	public List<CrawledEmail> getCrawledEmails(@Param(value="id") String id);

	@Select("SELECT handle, url FROM crawled_instagram_profiles WHERE blogger_id = #{id}")
	public List<CrawledInsta> getCrawledInstas(@Param(value="id") String id);

	@Select("SELECT handle, url FROM crawled_twitter_profiles WHERE blogger_id = #{id}")
	public List<CrawledTwitter> getCrawledTwitters(@Param(value="id") String id);
	
	@Select("SELECT id, root_page_url as blogUrl, last_crawled as lastCrawled FROM bloggers WHERE last_crawled is null")
	public List<Blogger> getBlogsToCrawl();

	@Insert("INSERT INTO crawled_emails(blogger_id,email,url) VALUES (#{id},#{email},#{url})")
	public int addCrawledEmail(@Param(value="id") String id, @Param(value="email") String email, @Param(value="url") String url);

	@Insert("INSERT INTO crawled_instagram_profiles(blogger_id,handle,url) VALUES (#{id},#{handle},#{url})")
	public int addCrawledInsta(@Param(value="id") String id, @Param(value="handle") String handle, @Param(value="url") String url);

	@Insert("INSERT INTO crawled_twitter_profiles(blogger_id,handle,url) VALUES (#{id},#{handle},#{url})")
	public int addCrawledTwitter(@Param(value="id") String id, @Param(value="handle") String handle, @Param(value="url") String url);

	@Update("UPDATE bloggers SET last_crawled = null WHERE id = #{id}")
	public void resetCrawlTime(@Param(value = "id") String id);

}
