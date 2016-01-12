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

import java.util.Date;

import org.junit.Test;

public class ThrottleTest {

	@Test
	public void testThrottle() {
		int delayMillis = 1000;
		Throttle<String> throttle = new Throttle<>(delayMillis);
		
		String testString = "testString";
		long startTime = new Date().getTime();
		String returnedString = throttle.apply(testString);
		long endTime = new Date().getTime();
		
		assertEquals(testString, returnedString);
		assertTrue(endTime - startTime >= delayMillis);
	}

}
