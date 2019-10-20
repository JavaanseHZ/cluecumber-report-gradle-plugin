package de.javaansehz

class CluecumberReportExtension {

    /**
     * The path to the Cucumber JSON files.
     */
    String sourceJsonReportDirectory

    /**
     * The location of the generated report.
     */
    String generatedHtmlReportDirectory

    /**
     * Custom parameters to add to the report.
     */
    Map<String, String> customParameters = new LinkedHashMap<>()

    /**
     * Path to a properties file. The included properties are converted to custom parameters and added to the others.
     * <pre>
     * My_Custom_Parameter=This is my custom value
     * My_Custom_Parameter2=This is another value
     * </pre>
     */
    String customParametersFile

    /**
     * Mark scenarios as failed if they contain pending or undefined steps (default: false).
     */
    boolean failScenariosOnPendingOrUndefinedSteps

    /**
     * Custom CSS that is applied on top of Cluecumber's default styles.
     */
    String customCss

    /**
     * Custom flag that determines if before and after hook sections of scenario detail pages should be expanded (default: false).
     */
    boolean expandBeforeAfterHooks

    /**
     * Custom flag that determines if step hook sections of scenario detail pages should be expanded (default: false).
     */
    boolean expandStepHooks

    /**
     * Custom flag that determines if doc string sections of scenario detail pages should be expanded (default: false).
     */
    boolean expandDocStrings

    /**
     * Custom hex color for passed scenarios (e.g. '#00ff00')'.
     */
    String customStatusColorPassed

    /**
     * Custom hex color for failed scenarios (e.g. '#ff0000')'.
     */
    String customStatusColorFailed

    /**
     * Custom hex color for skipped scenarios (e.g. '#ffff00')'.
     */
    String customStatusColorSkipped

    /**
     * Custom page title for the generated report.
     */
    String customPageTitle

    /**
     * Optional log level to control what information is logged in the console.
     * Allowed values: default, compact, minimal, off
     */
    String logLevel

    boolean skip

    def customParameters(String name, String value) {
        customParameters.put(name, value)
    }

}
