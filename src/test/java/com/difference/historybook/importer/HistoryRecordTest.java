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

import org.junit.Test;

public class HistoryRecordTest {

	@Test
	public void testSetTimestampAsEpoch() {
		HistoryRecord record = new HistoryRecord();
		record.setTimestampAsEpoch(1452522277L);
		assertEquals("2016-01-11T14:24:37Z", record.getTimestamp());
	}

}
