package de.javaansehz

import com.trivago.cluecumber.exceptions.CluecumberPluginException
import com.trivago.cluecumber.json.pojo.Report
import com.trivago.cluecumber.logging.CluecumberLogger
import com.trivago.cluecumber.rendering.pages.pojos.pagecollections.AllScenariosPageCollection
import org.gradle.api.DefaultTask

import java.nio.file.Path

class CluecumberReportTask extends DefaultTask {

    CluecumberReportTask() throws CluecumberPluginException {
        doLast() {
            if (extension.skip) {
                logger.info("Cluecumber report generation was skipped using the <skip> property.",
                        DEFAULT)
                return
            }

            logger.logInfoSeparator(DEFAULT)
            logger.info(String.format(" Cluecumber Report Maven Plugin, version %s", getClass().getPackage()
                    .getImplementationVersion()), DEFAULT)
            logger.logInfoSeparator(DEFAULT, COMPACT)

            // Create attachment directory here since they are handled during json generation.
            fileSystemManager.createDirectory(propertyManager.getGeneratedHtmlReportDirectory() + "/attachments")

            AllScenariosPageCollection allScenariosPageCollection = new AllScenariosPageCollection(propertyManager.getCustomPageTitle())
            List<Path> jsonFilePaths = fileSystemManager.getJsonFilePaths(propertyManager.getSourceJsonReportDirectory())
            for (Path jsonFilePath : jsonFilePaths) {
                String jsonString = fileIO.readContentFromFile(jsonFilePath.toString())
                try {
                    Report[] reports = jsonPojoConverter.convertJsonToReportPojos(jsonString)
                    allScenariosPageCollection.addReports(reports)
                } catch (CluecumberPluginException e) {
                    logger.warn("Could not parse JSON in file '" + jsonFilePath.toString() + "': " + e.getMessage())
                }
            }
            elementIndexPreProcessor.addScenarioIndices(allScenariosPageCollection.getReports())
            reportGenerator.generateReport(allScenariosPageCollection)
            logger.info(
                    "=> Cluecumber Report: " + propertyManager.getGeneratedHtmlReportDirectory() + "/" +
                            PluginSettings.SCENARIO_SUMMARY_PAGE_PATH + PluginSettings.HTML_FILE_EXTENSION,
                    DEFAULT,
                    COMPACT,
                    CluecumberLogger.CluecumberLogLevel.MINIMAL
            )
        }
    }

}
