# BigCommerce SDK

Java SDK for BigCommerce V3 REST APIs

| Service   | Develop | Master |
|-----------|---------|--------|
| CI Status | [![Build Status](https://travis-ci.org/rjdavis3/bigcommerce-sdk.svg?branch=develop)](https://travis-ci.org/rjdavis3/bigcommerce-sdk) | [![Build Status](https://travis-ci.org/rjdavis3/bigcommerce-sdk.svg?branch=master)](https://travis-ci.org/rjdavis3/bigcommerce-sdk) |

## Maven
```xml
	<dependency>
	    <groupId>com.github.rjdavis3</groupId>
	    <artifactId>bigcommerce-sdk</artifactId>
	    <version>1.0.0</version>
	</dependency>
```

## Quickstart
Creating SDK with refresh token then making a sample call:

```java
	final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(storeHash)
			.withClientId(clientId).withAccessToken(accessToken).build();
	final CatalogSummary catalogSummary = bigcommerceSdk.getCatalogSummary();
```

## Building from source

	1. Install Maven
	2. Install JDK 8
	3. Clone the repository.
	3. Navigate to repository directory and run `mvn install`

