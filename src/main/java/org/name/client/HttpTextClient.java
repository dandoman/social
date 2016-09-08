package org.name.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpTextClient {
	public String getBlogText(String blogUrl) {
		if(!blogUrl.contains("http")){
			blogUrl = "http://" + blogUrl;
		}
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(blogUrl);
		try{
			HttpResponse response = client.execute(getRequest);
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e){
			return null;
		}
	}
}
