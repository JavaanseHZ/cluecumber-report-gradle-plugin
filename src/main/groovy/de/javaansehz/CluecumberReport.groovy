package de.javaansehz


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class CluecumberReport implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extensionParams = project.extensions.create('cluecumberReports', CluecumberReportExtension)
        Task reportTask = project.task('generateCluecumberReports', type: CluecumberReportTask) {
            description = "Creates cucumber html reports"
            group = "Cucumber reports"
        }
        reportTask.onlyIf { !extensionParams.skip }

    }
}