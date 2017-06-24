import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin


class StyledDokka implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('styledDokka') {
            description 'Aggregates API docs of all subprojects with custom styles.'
            group JavaBasePlugin.DOCUMENTATION_GROUP
            dependsOn project.getTasksByName('dokka', true)
            doLast {
                println "Hello from the GreetingPlugin"
            }
        }
    }

}