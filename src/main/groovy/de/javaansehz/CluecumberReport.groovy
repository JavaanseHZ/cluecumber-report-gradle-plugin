package de.javaansehz

import com.trivago.cluecumber.exceptions.CluecumberPluginException
import com.trivago.cluecumber.filesystem.FileIO
import com.trivago.cluecumber.filesystem.FileSystemManager
import com.trivago.cluecumber.json.JsonPojoConverter
import com.trivago.cluecumber.json.processors.ElementIndexPreProcessor
import com.trivago.cluecumber.logging.CluecumberLogger
import com.trivago.cluecumber.properties.PropertyManager
import com.trivago.cluecumber.rendering.ReportGenerator
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import javax.inject.Inject

class CluecumberReport implements Plugin<Project>  {

    @Inject
    CluecumberReport(
            final CluecumberLogger logger,
            final PropertyManager propertyManager,
            final FileSystemManager fileSystemManager,
            final FileIO fileIO,
            final JsonPojoConverter jsonPojoConverter,
            final ElementIndexPreProcessor elementIndexPreProcessor,
            final ReportGenerator reportGenerator
    ) {
        this.propertyManager = propertyManager
        this.fileSystemManager = fileSystemManager
        this.fileIO = fileIO
        this.jsonPojoConverter = jsonPojoConverter
        this.logger = logger
        this.elementIndexPreProcessor = elementIndexPreProcessor
        this.reportGenerator = reportGenerator
    }

    /**
     * Cluecumber Report start method.
     *
     * @throws CluecumberPluginException When thrown, the plugin execution is stopped.
     */
    void apply(Project project) throws CluecumberPluginException {
        def extension = project.extensions.create('cluecumberReports', CluecumberReportExtension)
        initParameters(extension)
        logger.initialize(getLog(), logLevel)
        Task reportTask = project.task('generateCluecumberReports', type: CluecumberReportTask) {
            description = "Creates cucumber html reports"
            group = "Cucumber reports"
            projectName = project.displayName
            propertyManager = propertyManager
            fileSystemManager = fileSystemManager
            fileIO = fileIO
            jsonPojoConverter = jsonPojoConverter
            logger = logger
            elementIndexPreProcessor = elementIndexPreProcessor
            reportGenerator = reportGenerator
        }

        reportTask.onlyIf { !project.hasProperty('skip') }

    }

    void initParameters(extension) {
        propertyManager.setSourceJsonReportDirectory(extension.sourceJsonReportDirectory);
        propertyManager.setGeneratedHtmlReportDirectory(extension.generatedHtmlReportDirectory);
        propertyManager.setCustomParameters(extension.customParameters);
        propertyManager.setCustomParametersFile(extension.customParametersFile);
        propertyManager.setFailScenariosOnPendingOrUndefinedSteps(extension.failScenariosOnPendingOrUndefinedSteps);
        propertyManager.setExpandBeforeAfterHooks(extension.expandBeforeAfterHooks);
        propertyManager.setExpandStepHooks(extension.expandStepHooks);
        propertyManager.setExpandDocStrings(extension.expandDocStrings);
        propertyManager.setCustomCssFile(extension.customCss);
        propertyManager.setCustomStatusColorPassed(extension.customStatusColorPassed);
        propertyManager.setCustomStatusColorFailed(extension.customStatusColorFailed);
        propertyManager.setCustomStatusColorSkipped(extension.customStatusColorSkipped);
        propertyManager.setCustomPageTitle(extension.customPageTitle);
    }
}
