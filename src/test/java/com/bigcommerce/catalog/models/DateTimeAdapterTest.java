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
		final DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		final DateTime actualDateTime = dateTimeAdapter.unmarshal("Tue, 20 Nov 2012 00:00:00 +0000");
		assertNotNull(actualDateTime);
		assertEquals(DateTime.parse("2012-11-20T00:00:00.000Z"), actualDateTime);
	}

	@Test
	public void givenDateTimeObjectWhenUnmarshalingThenReturnDateTimeString() throws Exception {
		final DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		final DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeAdapter.RFC_822_DATE_FORMAT);
		final DateTime dateTime = dateTimeAdapter.unmarshal("Tue, 20 Nov 2012 00:00:00 +0000");
		final String dateTimeString = dateTimeAdapter.marshal(dateTime);

		final DateTime dateTimeParsedFromString = DateTime.parse(dateTimeString, formatter);

		assertNotNull(dateTimeString);
		assertEquals(0, dateTimeParsedFromString.compareTo(dateTime));

	}

	@Test
	public void givenNullDateStringWhenUnmarshalingThenReturnNullValue() throws Exception {
		final DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		final DateTime dateTime = dateTimeAdapter.unmarshal(null);
		assertNull(dateTime);

	}

	@Test
	public void givenNullDateWhenMarshalingThenReturnNullValue() throws Exception {
		final DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		final String dateTime = dateTimeAdapter.marshal(null);
		assertNull(dateTime);

	}

}
