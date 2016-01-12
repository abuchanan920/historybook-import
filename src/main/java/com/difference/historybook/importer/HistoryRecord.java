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

import java.time.Instant;

/**
 * A container for the data associated with a given web page 
 * 
 */
public class HistoryRecord {
	private String url;
	private String timestamp;
	private String body;
	
	public HistoryRecord setUrl(String url) {
		this.url = url;
		return this;
	}
	
	public String getUrl() {
		return url;
	}
	
	/**
	 * Convenience method for setting timestamp by epoch seconds
	 * 
	 * @param epochSeconds timestamp in UNIX epoch format
	 * @return this for method chaining
	 */
	public HistoryRecord setTimestampAsEpoch(long epochSeconds) {
		this.timestamp = Instant.ofEpochSecond(epochSeconds).toString();
		return this;
	}
	
	/**
	 * Timestamps are in ISO-8601 format
	 * 
	 * @param timestamp
	 * @return this for method chaining
	 */
	public HistoryRecord setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public HistoryRecord setBody(String body) {
		this.body = body;
		return this;
	}
	
	public String getBody() {
		return body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryRecord other = (HistoryRecord) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
}
