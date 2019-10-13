package de.javaansehz


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
import org.gradle.api.Plugin
import org.gradle.api.Project

import javax.inject.Inject
import java.nio.file.Path

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
        super(propertyManager);
        this.propertyManager = propertyManager;
        this.fileSystemManager = fileSystemManager;
        this.fileIO = fileIO;
        this.jsonPojoConverter = jsonPojoConverter;
        this.logger = logger;
        this.elementIndexPreProcessor = elementIndexPreProcessor;
        this.reportGenerator = reportGenerator;
    }

    /**
     * Cluecumber Report start method.
     *
     * @throws CluecumberPluginException When thrown, the plugin execution is stopped.
     */
    void apply(Project project) throws CluecumberPluginException {
        // Initialize logger to be available outside the AbstractMojo class
        def extension = project.extensions.create('cluecumber', CluecumberReportExtension)
        initParameters(extension)
        logger.initialize(getLog(), logLevel);


        if (extension.skip) {
            logger.info("Cluecumber report generation was skipped using the <skip> property.",
                    DEFAULT);
            return;
        }

        logger.logInfoSeparator(DEFAULT);
        logger.info(String.format(" Cluecumber Report Maven Plugin, version %s", getClass().getPackage()
                .getImplementationVersion()), DEFAULT);
        logger.logInfoSeparator(DEFAULT, COMPACT);

        super.execute();

        // Create attachment directory here since they are handled during json generation.
        fileSystemManager.createDirectory(propertyManager.getGeneratedHtmlReportDirectory() + "/attachments");

        AllScenariosPageCollection allScenariosPageCollection = new AllScenariosPageCollection(propertyManager.getCustomPageTitle());
        List<Path> jsonFilePaths = fileSystemManager.getJsonFilePaths(propertyManager.getSourceJsonReportDirectory());
        for (Path jsonFilePath : jsonFilePaths) {
            String jsonString = fileIO.readContentFromFile(jsonFilePath.toString());
            try {
                Report[] reports = jsonPojoConverter.convertJsonToReportPojos(jsonString);
                allScenariosPageCollection.addReports(reports);
            } catch (CluecumberPluginException e) {
                logger.warn("Could not parse JSON in file '" + jsonFilePath.toString() + "': " + e.getMessage());
            }
        }
        elementIndexPreProcessor.addScenarioIndices(allScenariosPageCollection.getReports());
        reportGenerator.generateReport(allScenariosPageCollection);
        logger.info(
                "=> Cluecumber Report: " + propertyManager.getGeneratedHtmlReportDirectory() + "/" +
                        PluginSettings.SCENARIO_SUMMARY_PAGE_PATH + PluginSettings.HTML_FILE_EXTENSION,
                DEFAULT,
                COMPACT,
                CluecumberLogger.CluecumberLogLevel.MINIMAL
        );
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


//    void apply(Project project) {
//        // Add the 'greeting' extension object
//        def extension = project.extensions.create('greeting', GreetingPluginExtension)
//        // Add a task that uses configuration from the extension object
//        project.task('hello') {
//            doLast {
//                println extension.message
//            }
//        }
//    }
}
