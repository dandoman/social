package org.name.entity;

import org.name.business.SocialMediaData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialMediaDataEntity {
	private String id;
	private String blogUrl;
	private String instagramhandle;
	private String twitterHandle;
	private String emailAddress;
	private Integer twitterFollowerCount;
	private Integer instagramFollowerCount;
}
