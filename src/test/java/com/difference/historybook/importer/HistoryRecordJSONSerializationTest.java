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

package com.difference.historybook.importer;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class HistoryRecordJSONSerializationTest {

	@Test
	public void testJSONRoundTrip() {
		String url = "http://does.not.exist";
		String timestamp = "2016-01-11T12:49:12Z";
		String body = "This is the body";
		
		String json = "{\"url\":\"" + url 
				+ "\",\"timestamp\":\"" + timestamp 
				+"\",\"body\":\"" + body +"\"}";
		
		HistoryRecord record = HistoryRecordJSONSerialization.parse(json);
		assertEquals(url, record.getUrl());
		assertEquals(timestamp, record.getTimestamp());
		assertEquals(body, record.getBody());
		
		String newJson = HistoryRecordJSONSerialization.toJSONString(record);
		assertEquals(json, newJson);
	}

	@Test
	public void testReadFile() throws IOException {
		List<HistoryRecord> results = HistoryRecordJSONSerialization.parseFile("./testdata/serialization/test.json")
				.collect(Collectors.toList());
		
		assertEquals(3, results.size());
		
		HistoryRecord record1 = new HistoryRecord().setUrl("url1").setTimestamp("timestamp1").setBody("body1");
		HistoryRecord record2 = new HistoryRecord().setUrl("url2").setTimestamp("timestamp2").setBody("body2");
		HistoryRecord record3 = new HistoryRecord().setUrl("url3").setTimestamp("timestamp3").setBody("body3");
		
		assertEquals(record1, results.get(0));
		assertEquals(record2, results.get(1));
		assertEquals(record3, results.get(2));
	}
}
