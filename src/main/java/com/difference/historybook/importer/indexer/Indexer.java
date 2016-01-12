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

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.codec.Charsets;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.difference.historybook.importer.HistoryRecord;
import com.difference.historybook.importer.HistoryRecordJSONSerialization;

/**
 * Reads a file of @HistoryRecord in JSON format and uploads to search index service 
 *
 */
public class Indexer {
	private static final String DEFAULT_BASE_URL = "http://127.0.0.1:8080";
	private static final String DEFAULT_COLLECTION = "default";
	
	private final String baseUrl;
	private final String collection;
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("usage: Indexer <inputfile>");
			System.exit(1);
		}
		
		String fileName = args[0];
		Indexer app = new Indexer(DEFAULT_BASE_URL, DEFAULT_COLLECTION);
		app.process(fileName);
	}
	
	/**
	 * 
	 * @param baseUrl URL of indexer web service
	 * @param collection the index collection to add the pages to
	 */
	public Indexer(String baseUrl, String collection) {
		this.baseUrl = baseUrl;
		this.collection = collection;
	}
	
	/**
	 * Reads a file of @HistoryRecord in JSON format and uploads to search index service 
	 * 
	 * @param fileName file name of @HistoryRecord in JSON form (one per line)
	 * @throws IOException
	 */
	public void process(String fileName) throws IOException {
		HistoryRecordJSONSerialization.parseFile(fileName)
			.forEach((r) -> index(r));
	}
	
	/**
	 * Uploads the given HistoryRecord to the search index service
	 * 
	 * @param record HistoryRecord to send to index. Should have a body.
	 */
	public void index(HistoryRecord record) {
		try {
			if (record.getBody() != null) {
				String url = baseUrl + "/collections/" 
						+ collection + "/" 
						+ URLEncoder.encode(record.getUrl(), Charsets.UTF_8.name());
				if (record.getTimestamp() != null) {
					url = url + "/" + URLEncoder.encode(record.getTimestamp(), Charsets.UTF_8.name());
				}
				Request.Post(url).bodyString(record.getBody(), ContentType.TEXT_HTML).execute();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
