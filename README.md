# Overview
HistoryBook-Import is a collection of utilities to enable bulk importing of browsing history into the HistoryBook application.

# Getting Started
Build the tools:

```
prompt> ./gradlew build
````
You will find a zip file containing the app in ```build/distributions/historybook-import.zip```. You can unzip this and run the scripts from within that directory.

Example usage:

```
prompt> ./chrome-exporter.sh > export.json
```
This will create a json file containing the browsing history of the user as extracted from Google Chrome.

```
prompt> ./crawler.sh export.json > crawl.json
```
This will read the history records from export.json, fetch the pages, and create a new dump file that adds the page content to the history records.

```
prompt> ./indexer.sh crawl.json
```
This will read the history records from crawl.json and upload to the search index via the HistoryBook web service.

# Architecture

The import process is broken up into multiple tools to allow the user to restart the process from various points, which is handy for the development process.

# License
Copyright 2016 Andrew W. Buchanan (buchanan@difference.com)

Licensed under the Apache License, Version 2.0