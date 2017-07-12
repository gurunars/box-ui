import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

class StyledDokka : Plugin<Project> {
    
    override fun apply(project: Project) {
        project.gradle.projectsEvaluated {
            project.task("styledDokka").apply {
                description = "Aggregate API docs of all subprojects with custom styles."
                group = JavaBasePlugin.DOCUMENTATION_GROUP

                val modules = project.subprojects.filter {
                    it.plugins.findPlugin("com.android.library") != null
                }

                setMustRunAfter(
                    modules.map { it.getTasksByName("dokka", true) }.flatten()
                )

                doLast { Beautifier(project, modules).beautify() }
            }
        }
    }

}
