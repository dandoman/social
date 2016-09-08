package org.name.business;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialMediaData {
	private String id;
	private String blogUrl;
	private TwitterData twitterData;
	private InstagramData instagramData;
	private String email;
}
