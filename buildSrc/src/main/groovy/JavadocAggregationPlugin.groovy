import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.javadoc.Javadoc


public class JavadocAggregationPlugin implements Plugin<Project> {
    static final String TASK_NAME = 'aggregateDocs'

    @Override
    void apply(Project project) {
        Project rootProject = project.rootProject
        rootProject.gradle.projectsEvaluated {
            Set<Project> modules = rootProject.subprojects.findAll {
                module -> module.plugins.findPlugin("com.android.library")
            }
            if (modules.isEmpty()) {
                return
            }
            rootProject.task(TASK_NAME, type: Javadoc) {
                description = 'Aggregates Javadoc API documentation of all subprojects.'
                group = JavaBasePlugin.DOCUMENTATION_GROUP
                dependsOn modules.generateReleaseJavadoc
                source modules.generateReleaseJavadoc.source
                destinationDir rootProject.file("$rootProject.buildDir/docs/html-docs")
                classpath = rootProject.files(modules.generateReleaseJavadoc.classpath)
            }
        }
    }

}