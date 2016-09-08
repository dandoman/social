package org.name.business;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class InstagramData {
	private String handle;
	private int followers;
	private Date sampleDate;
}
