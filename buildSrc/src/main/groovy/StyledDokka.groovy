import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

class StyledDokka implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.gradle.projectsEvaluated {
            project.task('styledDokka') {
                description 'Aggregate API docs of all subprojects with custom styles.'
                group JavaBasePlugin.DOCUMENTATION_GROUP

                Set<Project> modules = project.subprojects.findAll {
                    module -> module.plugins.findPlugin("com.android.library")
                }

                def dokkas = modules.collect { it.getTasksByName("dokka", true) }.flatten()

                def beautifier = new Beautifier(project, modules)

                //dependsOn dokkas
                mustRunAfter dokkas

                doLast { beautifier.beautify() }
            }
        }
    }

}