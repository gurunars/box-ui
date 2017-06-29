import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

import groovy.xml.XmlUtil
import groovy.xml.StreamingMarkupBuilder

import org.cyberneko.html.parsers.SAXParser


class StyledDokka implements Plugin<Project> {

    private void beautifyHtml(File file) {
        def page = parser.parse(it)
        def links = page."**".findAll { it.name() == "A" && it.text() =~ /^\d+x\d+$/ }

        if(links.isEmpty()) {
            return
        }

        links.collect {
            def node = it
            def parts = node.text().split("x")

            node.replaceNode { img(
                    src: node.@href.text(),
                    width: parts[0],
                    height: parts[1]
            ) }
        }
        file.write(XmlUtil.serialize(new StreamingMarkupBuilder().bind {
            mkp.yield page
        }))
    }

    private void doJob(Project project, Set<Project> modules) {
        project.copy {
            from "buildSrc"
            into "html-docs"
            include "*.css"
        }

        def parser = new XmlSlurper(new SAXParser())

        modules.each {
            def module = it

            project.copy {
                from "${module.name}/static"
                into "html-docs/${module.name}/static"
            }

            def projectDocs = new File("html-docs/${module.name}")
            // Nasty hack to treat specially formatted links as inline images
            projectDocs.eachFileRecurse {
                if (it.isFile() && it.name.endsWith(".html")) { beautifyHtml(it) }
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

        def indexFile = new File("html-docs/index.html")

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
            project.task('styledDokka') {
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