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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Methods for converting HistoryRecords to/from JSON
 * and reading a file of HistoryRecords in JSON format 
 *
 */
public class HistoryRecordJSONSerialization {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	static {
		MAPPER.setSerializationInclusion(Include.NON_EMPTY);
	}

	/**
	 * Convert a HistoryRecord to a JSON representation
	 * 
	 * @param record the record to convert
	 * @return JSON representation of record
	 */
	public static String toJSONString(HistoryRecord record) {
		try {
			return MAPPER.writeValueAsString(record);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Convert a JSON representation of a HistoryRecord to the actual HistoryRecord
	 * 
	 * @param json JSON representation of a HistoryRecord
	 * @return equivalent HistoryRecord
	 */
	public static HistoryRecord parse(String json) {
		try {
			return MAPPER.readValue(json, HistoryRecord.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Stream a JSON file of HistoryRecords
	 * 
	 * @param filename filename to read
	 * @return a stream of HistoryRecord objects parsed from file
	 * @throws IOException
	 */
	public static Stream<HistoryRecord> parseFile(String filename) throws IOException {
		 return Files.lines(Paths.get(filename))
				 .map(HistoryRecordJSONSerialization::parse);
	}
	
}
