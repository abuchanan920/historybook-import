#!/bin/bash

java -cp historybook-import-all.jar -Dorg.slf4j.simpleLogger.defaultLogLevel=error com.difference.historybook.importer.chrome.ChromeExporter "$@"
