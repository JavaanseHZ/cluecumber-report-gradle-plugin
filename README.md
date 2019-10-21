# Cluecumber Report Gradle Plugin
## Description
Gradle Plugin Wrapper for [Cluecumber Report Maven Plugin](https://github.com/trivago/cluecumber-report-plugin)

Based on https://github.com/trivago/cluecumber-report-plugin

## Usage

### Add the following lines to build.gradle

```
plugins {
    id 'de.javaansehz.cluecumber-report-gradle-plugin'
}

cluecumberReports {
    sourceJsonReportDirectory = [path-to-cucumber-json-files]
    generatedHtmlReportDirectory = [path-for-generated-reports]
}
```

### Build Task
```
generateCluecumberReports
```

## License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.