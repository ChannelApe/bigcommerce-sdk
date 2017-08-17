package com.bigcommerce.catalog.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Test;

public class DateTimeAdapterTest {

	@Test
	public void givenRFC822DateFormatStringThenUnmarshalToDateTime() throws Exception {
		DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		DateTime dateTime = dateTimeAdapter.unmarshal("Tue, 20 Nov 2012 00:00:00 +0000");
		assertNotNull(dateTime);
	}

	@Test
	public void givenNonRFC822DateFormatStringThenTryUnmarshalAndThrowException() {
		DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();

		try {
			dateTimeAdapter.unmarshal("Tu0:00 +0000");
			fail();
		} catch (Exception e) {
			assertNotNull(e.getMessage());
		}

	}

	@Test
	public void givenDateTimeObjectMarshalIntoRFC822DateTimeString() throws Exception {
		DateTimeAdapter dateTimeAdapter = new DateTimeAdapter();
		DateTime currentDate = new DateTime();
		String dateTimeString = dateTimeAdapter.marshal(currentDate);

		DateTime dateTimeParsedFromString = DateTime.parse(dateTimeString);

		assertNotNull(dateTimeString);
		assertEquals(dateTimeParsedFromString.compareTo(currentDate), 0);

	}

}
