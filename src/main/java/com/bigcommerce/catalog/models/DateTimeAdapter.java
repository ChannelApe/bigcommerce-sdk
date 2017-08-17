package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeAdapter extends XmlAdapter<String, DateTime> {

	public static final String RFC_822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

	@Override
	public DateTime unmarshal(final String timestamp) throws Exception {
		if (StringUtils.isBlank(timestamp)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormat.forPattern(RFC_822_DATE_FORMAT);
		return DateTime.parse(timestamp, formatter);
	}

	@Override
	public String marshal(final DateTime dateTime) throws Exception {
		if (dateTime == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormat.forPattern(RFC_822_DATE_FORMAT);
		return formatter.print(dateTime);

	}

}