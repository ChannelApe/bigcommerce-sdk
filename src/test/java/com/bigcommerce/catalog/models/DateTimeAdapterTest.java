package com.bigcommerce.catalog.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class DateTimeAdapterTest {

	@Test
	public void givenRFC822DateFormatStringWhenUnmarshalingThenReturnDateTime() throws Exception {
		DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		DateTime actualDateTime = dateTimeAdapter.unmarshal("Tue, 20 Nov 2012 00:00:00 +0000");
		assertNotNull(actualDateTime);
		assertEquals(DateTime.parse("2012-11-20T00:00:00.000Z"), actualDateTime);
	}

	@Test
	public void givenDateTimeObjectWhenUnmarshalingThenReturnDateTimeString() throws Exception {
		DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeAdapter.RFC_822_DATE_FORMAT);
		DateTime dateTime = dateTimeAdapter.unmarshal("Tue, 20 Nov 2012 00:00:00 +0000");
		String dateTimeString = dateTimeAdapter.marshal(dateTime);

		DateTime dateTimeParsedFromString = DateTime.parse(dateTimeString, formatter);

		assertNotNull(dateTimeString);
		assertEquals(dateTimeParsedFromString.compareTo(dateTime), 0);

	}

	@Test
	public void givenNullDateStringWhenUnmarshalingThenReturnNullValue() throws Exception {
		DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		DateTime dateTime = dateTimeAdapter.unmarshal(null);
		assertNull(dateTime);

	}

	@Test
	public void givenNullDateWhenMarshalingThenReturnNullValue() throws Exception {
		DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		String dateTime = dateTimeAdapter.marshal(null);
		assertNull(dateTime);

	}

}
