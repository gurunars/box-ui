import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin


class StyledDokka implements Plugin<Project> {

    private void doJob(Project project, Set<Project> modules) {
        project.copy {
            from "docs-base"
            into "html-docs"
        }

        def indexFile = new File("html-docs/index.html")

        modules.each {
            def module = it

            project.copy {
                from "${module.name}/static"
                into "html-docs/${module.name}/static"
            }

            def projectDocs = new File("html-docs/${module.name}")
            projectDocs.eachFileRecurse {
                if (it.isFile() && it.name.endsWith(".html")) {
                    // read from file into a string
                    // find all links with \dx\d as text and replace them with img tags
                    // using the text as a size hint
                    // OR

                }
            }

        }

        def projects = modules.collect {
            def description = ""
            if (it.description != null) {
                description = "<p>${it.description}</p>"
            }
            """
            <div class="section">
                <h3><a href="${it.name}">${it.name} (${it.version})</a></h3>
                ${description}
                <code>
                compile ('${it.group}.${it.name}:${it.version}@aar') {
                    transitive = true
                }
                </code>
            </div>
            """
        }

        indexFile.write("""
        <html>
            <head>
                <meta charset="UTF-8">
                <title>${project.name} ($project.version)</title>
                <link rel="stylesheet" href="style.css">
            </head>
            <body>
                ${projects.join("\n")}
            </body>
        </html>
        """)
    }

    @Override
    void apply(Project project) {
        project.gradle.projectsEvaluated {
            project.task('dokka') {
                description 'Aggregate API docs of all subprojects with custom styles.'
                group JavaBasePlugin.DOCUMENTATION_GROUP

                Set<Project> modules = project.subprojects.findAll {
                    module -> module.plugins.findPlugin("com.android.library")
                }

                def dokkas = modules.collect { it.getTasksByName("dokka", true) }.flatten()

                //dependsOn dokkas
                mustRunAfter dokkas

                doLast { this.doJob(project, modules) }
            }
        }
    }

}