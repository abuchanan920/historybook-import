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

import java.io.IOException;
import java.io.PrintStream;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.codec.Charsets;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import com.difference.historybook.importer.HistoryRecord;
import com.difference.historybook.importer.HistoryRecordJSONSerialization;

/**
 * Utility to read a file of JSON HistoryRecords and crawl the pages to fetch the content 
 *
 */
public class Crawler {
	private static final int THROTTLE_MILLIS = 3000;
	
	public static void main(String[] args) throws IOException{
		if (args.length < 1) {
			System.err.println("usage: Crawler <inputfile>");
			System.exit(1);
		}
		
		String fileName = args[0];
		process(fileName);
	}
	
	/**
	 * Reads a HistoryRecord JSON file and fetch each with default throttle config
	 * 
	 * @param filename HistoryRecord JSON file to read from
	 * @throws IOException
	 */
	public static void process(String filename) throws IOException {
		process(filename, THROTTLE_MILLIS);
	}
	
	/**
	 * Reads a HistoryRecord JSON file and fetch each with specified throttle config
	 * 
	 * @param filename HistoryRecord JSON file to read from
	 * @param throttleMillis time for each thread to pause between fetches (how nice to be to the servers)
	 * @throws IOException
	 */
	public static void process(String filename, int throttleMillis) throws IOException{
		process(filename, throttleMillis, Crawler::fetchBody, System.err, System.out);
	}
	
	/**
	 * Reads a HistoryRecord JSON file and run a process against each
	 * 
	 * @param filename HistoryRecord JSON file to read from
	 * @param throttleMillis time for each thread to pause between fetches (how nice to be to the servers)
	 * @param processor process to run against each HistoryRecord
	 * @param progress where to print out the urls as we fetch
	 * @param output where to print the new HistoryRecord JSON with the body content
	 * @throws IOException
	 */
	public static void process(
			String filename, 
			int throttleMillis, 
			UnaryOperator<HistoryRecord> processor,
			PrintStream progress, 
			PrintStream output
			) throws IOException{
		getUrlStream(filename).parallel()
			.map(new Throttle<HistoryRecord>(throttleMillis))
			.map((r) -> printUrl(r, progress))
			.map(processor)
			.map(HistoryRecordJSONSerialization::toJSONString)
			.forEach(output::println);
	}
	
	/**
	 * Get Stream of HistoryRecord filtered to valid fetchable urls
	 * 
	 * @param filename HistoryRecord JSON file to read from
	 * @return
	 * @throws IOException
	 */
	protected static Stream<HistoryRecord> getUrlStream(String filename) throws IOException {
		return HistoryRecordJSONSerialization.parseFile(filename)
			.filter(Crawler::isValidProtocol)
			.map(Crawler::removeFragment);
	}
	
	// filter HistoryRecords to web pages
	private static boolean isValidProtocol(HistoryRecord record) {
		String lCaseUrl = record.getUrl().toLowerCase();
		return lCaseUrl.startsWith("http:") || lCaseUrl.startsWith("https:");
	}
	
	// Modify HistoryRecords to remove fragment portions of URL
	private static HistoryRecord removeFragment(HistoryRecord record) {
		String url = record.getUrl();
		int fragmentStart = url.indexOf('#');
		if (fragmentStart >= 0) {
			record.setUrl(url.substring(0, fragmentStart));
		}
		return record;
	}
	
	// print the url from a HistoryRecord to the specified PrintStream
	private static HistoryRecord printUrl(HistoryRecord record, PrintStream output) {
		output.println(record.getUrl());
		return record;
	}
	
	/**
	 * Fetch the body for a given HistoryRecord and add it to the record
	 * 
	 * @param record HistoryRecord containing a URL to fetch
	 * @return
	 */
	protected static HistoryRecord fetchBody(HistoryRecord record) {
		try {
			Content content = 
					Request.Get(encodeSpecialChars(record.getUrl()))
						.execute()
						.returnContent();
			if (content != null) {
				record.setBody(content.asString(Charsets.UTF_8));
			}
		} catch (IOException e) {
			System.err.println("Failed to fetch " + record.getUrl() + ": " + e.getLocalizedMessage());
		}
		return record;
	}
	
	// encode special characters that seem to show up in otherwise URL encoded URLs...
	// TODO: Do we still need the # encode? That might have been from before filtering them out...
	private static String encodeSpecialChars(String url) {
		String result = url
				.replace("{", "%7B")
				.replace("}", "%7D")
				.replace("#", "%23")
				.replace("|", "%7C")
				.replace(" ", "%20");
		return result;
	}
	
}
