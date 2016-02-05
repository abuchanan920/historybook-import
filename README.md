# Overview
HistoryBook-Import is a collection of utilities to enable bulk importing of browsing history into the HistoryBook application.

# Getting Started
Build the tools:

```
prompt> ./gradlew build
````
You will find a zip file containing the app in ```build/distributions/historybook-import.zip```. You can unzip this and run the scripts from within that directory.

## Step 1a: Chrome History Export

Example usage:

```
prompt> ./chrome-exporter.sh >> export.json
```
This will create a json file containing the browsing history of the user as extracted from Google Chrome.

**NOTE:** The sqllite database used by Chrome can only be accessed by one process at a time. If you have Chrome running while running this exporter, you will get an error message. You must close Chrome before running the exporter for it to be able to access the history database.

## Step 1b: Pocket History Export

Example usage:

```
prompt> ./pocket-exporter.sh
```
This will open your browser to the page at Pocket where you can export your history as an HTML file. This will need to be converted to the standard HistoryBook import file format in the next step.

Assuming you saved the output file as ```ril_export.html```, step 2 is:

```
prompt> ./pocket-transformer.sh ril_export.html >> export.json
```

## Step 2: Crawling page content

```
prompt> ./crawler.sh export.json > crawl.json
```
This will read the history records from export.json, fetch the pages, and create a new dump file that adds the page content to the history records.

## Step 3: Indexing page content

```
prompt> ./indexer.sh crawl.json
```
This will read the history records from crawl.json and upload to the search index via the HistoryBook web service.

# Architecture

The import process is broken up into multiple tools to allow the user to restart the process from various points, which is handy for the development process.

# License
Copyright 2016 Andrew W. Buchanan (buchanan@difference.com)

Licensed under the Apache License, Version 2.0