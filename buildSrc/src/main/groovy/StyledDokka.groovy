import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

import groovy.xml.XmlUtil
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder

import org.cyberneko.html.parsers.SAXParser

import java.util.function.Consumer


class StyledDokka implements Plugin<Project> {

    private replaceImageLinksWithImgs(GPathResult page) {
        def links = page."**".findAll { it.name() == "A" && it.text() =~ /^\d+x\d+$/ }

        links.collect {
            def node = it
            def parts = node.text().split("x")

            node.replaceNode { img(
                src: node.@href.text(),
                width: parts[0],
                height: parts[1]
            ) }
        }
    }

    private void formatBreadCrumbs(GPathResult page) {
        page.BODY."*".findAll { it.name() == "BR" }.collect {
            it.replaceNode { }
        }

        List tags = []

        for (tag in page.BODY.children()) {
            if (tag.name() != "A") {
                break
            }
            tags.add(tag)
        }

        page.BODY.appendNode {
            div(class: "breadcrumbs") {
                span("/")
                a("index", href: "/")
                tags.each {
                    span("/")
                    a(it.text(), href: it.@href)
                }
            }
        }

        tags.collect { it.replaceNode { } }

    }

    class ParamParseStateMachine {

        private enum ParamParseState {
            NOT_STARTED,
            STARTED,
            LINK,
            CODE
        }

        private List<GPathResult> externalCollection
        private Consumer<GPathResult> consumer

        ParamParseStateMachine(
            List<GPathResult> externalCollection,
            Consumer<List<GPathResult>> consumer
        ) {
            this.externalCollection = externalCollection
            this.consumer = consumer
        }

        private ParamParseState state = ParamParseState.NOT_STARTED
        private List<GPathResult> resultAccumulator = new ArrayList<>()

        private ParamParseState transit(GPathResult child) {
            switch (state) {
                case ParamParseState.NOT_STARTED:
                    if(child.name() == "H3" && child.text() == "Parameters") {
                        return ParamParseState.STARTED
                    }
                    break
                case ParamParseState.STARTED:
                    if (child.name() == "A") {
                        resultAccumulator.add(child)
                        return ParamParseState.LINK
                    }
                    break
                case ParamParseState.LINK:
                    if (child.name() == "CODE") {
                        resultAccumulator.add(child)
                        return ParamParseState.CODE
                    }
                    break
                case ParamParseState.CODE:
                    resultAccumulator.add(child)
                    consumer.accept(resultAccumulator)
                    resultAccumulator = new ArrayList<>()
                    return ParamParseState.STARTED
            }
            return state
        }

        void process(GPathResult child) {
            state = transit(child)
        }

    }

    private void beautifyParameters(GPathResult page) {

        ParamParseStateMachine stateMachine = ParamParseStateMachine(
            new Consumer<List<GPathResult>>() {
                @Override
                void accept(List<GPathResult> tags) {

                    tags.collect() { it.replaceNode { } }
                }
            }
        )
        
    }

    private void beautifyHtml(File file) {
        def beautifier = new Beautifier()
        beautifier.beautify()
        /*

        def parser = new XmlSlurper(new SAXParser())
        def page = parser.parseText(file.text.replaceAll(/&nbsp;\/&nbsp;/, ""))

        replaceImageLinksWithImgs(page)
        beautifyParameters(page)
        formatBreadCrumbs(page)

        println(XmlUtil.serialize(new StreamingMarkupBuilder().bind {
            mkp.yield page
        }))
        */
    }

    private void doJob(Project project, Set<Project> modules) {
        project.copy {
            from "buildSrc"
            into "html-docs"
            include "*.css"
        }

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
                <div class="breadcrumbs">
                    / <a href="/">index</a>
                </div>
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