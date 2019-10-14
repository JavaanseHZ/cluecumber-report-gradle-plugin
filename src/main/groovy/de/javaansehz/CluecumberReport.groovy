package de.javaansehz

import com.trivago.cluecumber.exceptions.CluecumberPluginException
import com.trivago.cluecumber.properties.PropertyManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import javax.inject.Inject

class CluecumberReport implements Plugin<Project>  {

    @Inject
    final PropertyManager propertyManager
    /**
     * Cluecumber Report start method.
     *
     * @throws CluecumberPluginException When thrown, the plugin execution is stopped.
     */
    void apply(Project project) throws CluecumberPluginException {
        def extension = project.extensions.create('cluecumberReports', CluecumberReportExtension)
        initParameters(extension)
        //logger.initialize(getLog(), logLevel)
        Task reportTask = project.task('generateCluecumberReports', type: CluecumberReportTask) {
            description = "Creates cucumber html reports"
            group = "Cucumber reports"
            //projectName = project.displayName
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
