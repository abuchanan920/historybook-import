/*
 * Copyright 2016 Andrew W. Buchanan (buchanan@difference.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.difference.historybook.importer.crawler;

import static org.junit.Assert.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.*;

import org.junit.Rule;
import org.junit.Test;

import com.difference.historybook.importer.HistoryRecord;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class CrawlerTest {
	private static final int DUMMY_SERVER_PORT = 8089;
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(DUMMY_SERVER_PORT));
	
	@Test
	public void testGetUrlStream() throws IOException {
		List<HistoryRecord> records = Crawler.getUrlStream("./testdata/crawlertest/test1.json")
				.collect(toList());
		
		assertEquals(4, records.size());
		
		assertEquals("http://localhost:8089", records.get(0).getUrl());
		assertEquals("https://localhost:8090", records.get(1).getUrl());
		assertEquals("http://localhost:8089/2?q=query", records.get(2).getUrl());
		assertEquals("http://localhost:8089/3?q=query", records.get(3).getUrl());
	}

	@Test
	public void testFetchBody() throws IOException {
		stubFor(get(urlMatching("/"))
	            .willReturn(aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "text/html")
	                .withBody("1")));

		stubFor(get(urlPathEqualTo("/2"))
				.withQueryParam("q", containing("query"))
	            .willReturn(aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "text/html")
	                .withBody("2")));

		stubFor(get(urlPathEqualTo("/3"))
				.withQueryParam("q", containing("query"))
	            .willReturn(aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "text/html")
	                .withBody("3")));
	
		stubFor(get(urlPathEqualTo("/4"))
				.withQueryParam("q", containing("%7Btest%7Csomething%7D"))
	            .willReturn(aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "text/html")
	                .withBody("4")));

		List<HistoryRecord> results = Crawler.getUrlStream("./testdata/crawlertest/test2.json")
			.map(Crawler::fetchBody)
			.collect(toList());
		
		assertEquals(4, results.size());
		
		assertEquals("1", results.get(0).getBody());
		assertEquals("2", results.get(1).getBody());
		assertEquals("3", results.get(2).getBody());
		assertEquals("4", results.get(3).getBody());
	}
}
