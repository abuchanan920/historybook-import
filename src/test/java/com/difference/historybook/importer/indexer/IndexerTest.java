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

package com.difference.historybook.importer.indexer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import org.junit.Rule;
import org.junit.Test;

import com.difference.historybook.importer.HistoryRecord;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class IndexerTest {
	private static final int DUMMY_SERVER_PORT = 8089;
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(DUMMY_SERVER_PORT));

	@Test
	public void testIndex() {
		stubFor(post(urlMatching("/*")).willReturn(aResponse().withStatus(201)));

		Indexer indexer = new Indexer("http://localhost:8089", "testcollection");
		
		//test with timestamp
		HistoryRecord record1 = new HistoryRecord().setUrl("http://does.not.exist")
				.setBody("This is the body for record1")
				.setTimestamp("2016-01-11T12:49:12Z");

		//test without timestamp
		HistoryRecord record2 = new HistoryRecord().setUrl("http://does.not.exist/2")
				.setBody("This is the body for record2");
		
		indexer.index(record1);
		verify(postRequestedFor(
					urlEqualTo("/collections/testcollection/http%3A%2F%2Fdoes.not.exist/2016-01-11T12%3A49%3A12Z"))
				.withRequestBody(equalTo("This is the body for record1")));

		indexer.index(record2);
		verify(postRequestedFor(
					urlEqualTo("/collections/testcollection/http%3A%2F%2Fdoes.not.exist%2F2"))
				.withRequestBody(equalTo("This is the body for record2")));
	}
}
