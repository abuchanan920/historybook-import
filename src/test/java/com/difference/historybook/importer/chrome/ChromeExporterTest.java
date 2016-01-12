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

package com.difference.historybook.importer.chrome;

import static org.junit.Assert.*;

import java.util.List;

import static java.util.stream.Collectors.*;

import org.junit.Test;

import com.difference.historybook.importer.HistoryRecord;

public class ChromeExporterTest {

	@Test
	public void testGetHistoryRecordsFromDatabase() {
		HistoryRecord record1 = new HistoryRecord().setUrl("https://www.keepassx.org/").setTimestamp("2016-01-07T14:15:45Z");
		HistoryRecord record2 = new HistoryRecord().setUrl("https://www.paypal.com/home").setTimestamp("2015-12-14T03:21:05Z");
		HistoryRecord record3 = new HistoryRecord().setUrl("https://www.virtualbox.org/wiki/Downloads").setTimestamp("2015-10-30T17:43:41Z");
		
		List<HistoryRecord> list = ChromeExporter.getHistoryRecordsFromDatabase("./testdata/chrometest/History")
			.collect(toList());
		assertEquals(3, list.size());
		
		assertEquals(record1, list.get(0));
		assertEquals(record2, list.get(1));
		assertEquals(record3, list.get(2));
	}

}
