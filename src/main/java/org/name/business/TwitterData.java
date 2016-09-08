package org.name.business;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TwitterData {
	private String handle;
	private int followers;
	private Date sampleDate;
}
