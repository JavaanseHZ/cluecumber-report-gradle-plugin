package de.javaansehz

import com.google.inject.Guice
import com.trivago.cluecumber.exceptions.CluecumberPluginException
import com.trivago.cluecumber.filesystem.FileIO
import com.trivago.cluecumber.filesystem.FileSystemManager
import com.trivago.cluecumber.json.JsonPojoConverter
import com.trivago.cluecumber.json.pojo.Report
import com.trivago.cluecumber.json.processors.ElementIndexPreProcessor
import com.trivago.cluecumber.logging.CluecumberLogger
import com.trivago.cluecumber.properties.PropertyManager
import com.trivago.cluecumber.rendering.ReportGenerator
import com.trivago.cluecumber.rendering.pages.pojos.pagecollections.AllScenariosPageCollection
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject
import java.nio.file.Path

class CluecumberReportTask extends DefaultTask {

    @Inject
    CluecumberLogger ccLogger
    @Inject
    PropertyManager propertyManager
    @Inject
    FileSystemManager fileSystemManager
    @Inject
    FileIO fileIO
    @Inject
    JsonPojoConverter jsonPojoConverter
    @Inject
    ElementIndexPreProcessor elementIndexPreProcessor
    @Inject
    ReportGenerator reportGenerator

    CluecumberReportTask() {
        def injector = Guice.createInjector(new CluecumberModule())
        injector.injectMembers(this)
    }

    @TaskAction
    void run() throws CluecumberPluginException {
        initParameters()
        fileSystemManager.createDirectory(propertyManager.getGeneratedHtmlReportDirectory() + "/attachments")
        AllScenariosPageCollection allScenariosPageCollection = new AllScenariosPageCollection(propertyManager.getCustomPageTitle())
        List<Path> jsonFilePaths = fileSystemManager.getJsonFilePaths(propertyManager.getSourceJsonReportDirectory())
        for (Path jsonFilePath : jsonFilePaths) {
            String jsonString = fileIO.readContentFromFile(jsonFilePath.toString())
            try {
                Report[] reports = jsonPojoConverter.convertJsonToReportPojos(jsonString)
                allScenariosPageCollection.addReports(reports)
            } catch (CluecumberPluginException e) {
                ccLogger.warn("Could not parse JSON in file '" + jsonFilePath.toString() + "': " + e.getMessage())
            }
        }
        elementIndexPreProcessor.addScenarioIndices(allScenariosPageCollection.getReports())
        reportGenerator.generateReport(allScenariosPageCollection)
    }

    void initParameters() {
        def extensionParams = project.extensions.cluecumberReports
        propertyManager.setSourceJsonReportDirectory(extensionParams.sourceJsonReportDirectory)
        propertyManager.setGeneratedHtmlReportDirectory(extensionParams.generatedHtmlReportDirectory)
        propertyManager.setCustomParametersFile(extensionParams.customParametersFile)
        propertyManager.setFailScenariosOnPendingOrUndefinedSteps(extensionParams.failScenariosOnPendingOrUndefinedSteps)
        propertyManager.setExpandBeforeAfterHooks(extensionParams.expandBeforeAfterHooks)
        propertyManager.setExpandStepHooks(extensionParams.expandStepHooks)
        propertyManager.setExpandDocStrings(extensionParams.expandDocStrings)
        propertyManager.setCustomCssFile(extensionParams.customCss)
        propertyManager.setCustomStatusColorPassed(extensionParams.customStatusColorPassed)
        propertyManager.setCustomStatusColorFailed(extensionParams.customStatusColorFailed)
        propertyManager.setCustomStatusColorSkipped(extensionParams.customStatusColorSkipped)
        propertyManager.setCustomPageTitle(extensionParams.customPageTitle)
        propertyManager.setStartPage(Optional.ofNullable(extensionParams.startPage).orElse("ALL_SCENARIOS"))
    }
}
