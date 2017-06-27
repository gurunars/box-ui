import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

class StyledDokka implements Plugin<Project> {

    private void doJob(Project project, Set<Project> modules) {
        project.copy {
            from "docs-base"
            into "html-docs"
        }

        def parser = new XmlSlurper()

        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)

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
                    def nodes = parser.parse(it)
                    def links = nodes."**".findAll { it.A.text() == "320x570" }
                    println "${links}"
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