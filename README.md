[![Apache V2 License](http://img.shields.io/badge/license-Apache%20V2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.com/JavaanseHZ/cluecumber-report-gradle-plugin.svg?branch=master)](https://travis-ci.com/JavaanseHZ/cluecumber-report-gradle-plugin)
[![codecov](https://codecov.io/gh/JavaanseHZ/cluecumber-report-gradle-plugin/branch/master/graph/badge.svg)]](https://codecov.io/gh/JavaanseHZ/cluecumber-report-gradle-plugin)
# Cluecumber Report Gradle Plugin
## Description
Gradle Plugin Wrapper for [Cluecumber Report Maven Plugin](https://github.com/trivago/cluecumber-report-plugin)

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
For optional parameters, please consult the [README at the original Maven Plugin](https://github.com/trivago/cluecumber-report-plugin/blob/master/README.md#optional-configuration-parameters).

### Build Task
```
generateCluecumberReports
```

## License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
