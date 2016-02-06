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

package com.difference.historybook.importer.pocket;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.difference.historybook.importer.HistoryRecord;
import com.difference.historybook.importer.HistoryRecordJSONSerialization;

/**
 * A utility to read a Pocket export file and convert to a stream of HistoryRecords in JSON format 
 *
 */
public class PocketExporter {

	public static void main(String[] args) throws IOException {
		process(args[0]);
	}

	/**
	 * Read read a Pocket export file and convert to a stream of HistoryRecord in JSON format
	 * 
	 * @param pocketHistoryFilePath path of the Pocket export file to read history from
	 */
	public static void process(String pocketHistoryFilePath) throws IOException {
		Document doc = Jsoup.parse(new File(pocketHistoryFilePath), null);
		Elements entries = doc.select("li a");
		entries.stream()
			.map(PocketExporter::mapElementToHistoryRecord)
			.map(HistoryRecordJSONSerialization::toJSONString)
			.forEach(System.out::println);
	}
	
	// Converts an element in the history file to a HistoryRecord
	private static HistoryRecord mapElementToHistoryRecord(Element element) {
		return new HistoryRecord()
			.setUrl(element.attr("href"))
			.setTimestampAsEpoch(Long.parseLong(element.attr("time_added")));
	}
}
