package com.difference.historybook.importer.pocket;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.difference.historybook.importer.HistoryRecord;
import com.difference.historybook.importer.HistoryRecordJSONSerialization;

public class PocketExporter {

	public static void main(String[] args) throws IOException {
		process(args[0]);
	}

	public static void process(String pocketHistoryFilePath) throws IOException {
		Document doc = Jsoup.parse(new File(pocketHistoryFilePath), null);
		Elements entries = doc.select("li a");
		entries.stream()
			.map(PocketExporter::mapElementToHistoryRecord)
			.map(HistoryRecordJSONSerialization::toJSONString)
			.forEach(System.out::println);
	}
	
	private static HistoryRecord mapElementToHistoryRecord(Element element) {
		return new HistoryRecord()
			.setUrl(element.attr("href"))
			.setTimestampAsEpoch(Long.parseLong(element.attr("time_added")));
	}
}
