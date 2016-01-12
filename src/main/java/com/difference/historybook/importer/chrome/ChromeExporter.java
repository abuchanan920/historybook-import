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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.jooq.Record;
import org.jooq.impl.DSL;
import org.sqlite.SQLiteConfig;

import com.difference.historybook.importer.HistoryRecord;
import com.difference.historybook.importer.HistoryRecordJSONSerialization;

/**
 * A utility to read the history file of a Chrome browser and convert to a stream of HistoryRecords in JSON format 
 *
 */
public class ChromeExporter {
	public static void main(String[] args) throws Exception {
		String databasePath = System.getProperty("user.home") + "/Library/Application Support/Google/Chrome/Default/History";
		process(databasePath);
	}
	
	/**
	 * Read the history file of a Chrome browser and convert to a stream of HistoryRecord in JSON format
	 * 
	 * @param databasePath path of Sqlite database to read history from
	 */
	public static void process(String databasePath) {
		getHistoryRecordsFromDatabase(databasePath)
	    	.map(HistoryRecordJSONSerialization::toJSONString)
	    	.forEach(System.out::println);
	}
	
	/**
	 * Convert the history of a Chrome browser into a @Stream of @HistoryRecord
	 *  
	 * @param databasePath path of Sqlite database to read history from
	 * @return @Stream of @HistoryRecord
	 */
	public static Stream<HistoryRecord> getHistoryRecordsFromDatabase(String databasePath) {
		return getRecordsFromDatabase(databasePath)
				.map(ChromeExporter::mapRecordToHistoryRecord);
	}
	
	// Convert the history of a Chrome browser to a Stream of JOOQ records
	private static Stream<Record> getRecordsFromDatabase(String databasePath) {
		SQLiteConfig config;
		try {
			Class.forName("org.sqlite.JDBC");
			config = new SQLiteConfig();
			config.setReadOnly(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	    try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + databasePath, config.toProperties())) {
	        String sql =
	        		"SELECT url," +
	        		  "last_visit_time/1000000 + (strftime('%s', '1601-01-01')) AS timestamp " +
	        		"FROM urls " +
	        		"ORDER BY last_visit_time DESC";
	 
        	return DSL.using(c).fetch(sql).stream();
       } catch (SQLException e) {
    	   e.printStackTrace();
    	   return null;
       }	
    }
	
	// Converts a JOOQ record to a HistoryRecord
	private static HistoryRecord mapRecordToHistoryRecord(Record record) {
		return new HistoryRecord()
			.setUrl(record.getValue("url", String.class))
			.setTimestampAsEpoch(record.getValue("timestamp", Long.class));
	}
		
}
