package de.javaansehz

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Paths

class CluecumberReportTaskTest extends Specification {

    @Subject
    CluecumberReportTask cluecumberReportTask

    Project project = ProjectBuilder.builder().build()

    def basePath =  Paths.get(".").toAbsolutePath().toString()
    def reportPath = "$basePath/build/test/"
    def sourcePath = "$basePath/src/test/resources/"

    def setup() {
        def extensionParams = project.extensions.create('cluecumberReports', CluecumberReportExtension)
        extensionParams.setSourceJsonReportDirectory(sourcePath)
        extensionParams.setGeneratedHtmlReportDirectory(reportPath)
        cluecumberReportTask = project.task('generateCluecumberReports', type: CluecumberReportTask)
    }

    def "Generate Example Report 'testreport.json'"() {
        when:
        cluecumberReportTask.run()
        then:
        assert Paths.get("$reportPath/index.html").toFile().exists()
    }
}
