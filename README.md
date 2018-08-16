# BigCommerce SDK

Java SDK for BigCommerce REST APIs

| Service   | Develop | Master |
|-----------|---------|--------|
| CI Status | [![Build Status](https://travis-ci.org/ChannelApe/bigcommerce-sdk.svg?branch=develop)](https://travis-ci.org/ChannelApe/bigcommerce-sdk) | [![Build Status](https://travis-ci.org/ChannelApe/bigcommerce-sdk.svg?branch=master)](https://travis-ci.org/ChannelApe/bigcommerce-sdk) |

[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=bigcommerce-sdk&metric=alert_status)](https://sonarcloud.io/dashboard?id=bigcommerce-sdk) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=bigcommerce-sdk&metric=coverage)](https://sonarcloud.io/dashboard?id=bigcommerce-sdk)

## Maven
```xml
	<dependency>
	    <groupId>com.channelape</groupId>
	    <artifactId>bigcommerce-sdk</artifactId>
	    <version>1.4.3</version>
	</dependency>
```

## Quickstart
Creating SDK with store hash, client ID, and access token then making a sample call:

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

## Release Notes
Please see our release notes here:  [https://github.com/ChannelApe/bigcommerce-sdk/releases](https://github.com/ChannelApe/bigcommerce-sdk/releases)


