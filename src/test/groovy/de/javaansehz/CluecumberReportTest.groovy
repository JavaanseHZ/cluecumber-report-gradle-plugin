package de.javaansehz

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class CluecumberReportTest extends Specification {

    def "Applying Plugin creates Task"() {
        when:
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'de.javaansehz.cluecumber-report-gradle-plugin'
        then:
        assert project.tasks.generateCluecumberReports instanceof CluecumberReportTask
    }
}
