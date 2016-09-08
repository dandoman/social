package org.name.business;

import java.util.Date;
import lombok.Data;


@Data
public class Blogger {
	private String id;
	private String blogUrl;
	private Date lastCrawled;
}
