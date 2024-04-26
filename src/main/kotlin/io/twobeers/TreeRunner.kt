package io.twobeers

import org.apache.commons.text.StringSubstitutor
import java.io.File

enum class Template(val templateId: String, val childImport: String? = null, val childFields: String? = null, val childCalls: String? = null) {
    QUARKUS("/quarkus/quarkus-java.txt"),
    QUARKUS_CHILD("/quarkus/quarkus-children-java.txt",
        "/quarkus/quarkus-children-import-java.txt",
        "/quarkus/quarkus-children-fields-java.txt",
        "/quarkus/quarkus-children-calls-java.txt",
        ),
    SPRING_BOOT("/springboot/springboot-java.txt"),
}

class TreeRunner(val basePath: String, val rootPackage: String, val template: Template ) {
    val numberMap = mapOf("0" to "Zero", "1" to "One", "2" to "Two", "3" to "Three", "4" to "Four", "5" to "Five")

    fun classNameFromId(id: String): String {
        val classPostfix =  if (!id.equals("")) {
            val elements = id.split("-")
            elements.map { numberMap[it] }.reduceRight { s, acc -> "$acc$s" }
        } else {
            "Default"
        }

        return "${classPostfix}Service"
    }

    private fun packageFromId(id: String): String {
        val packagePostfix = if (!id.equals("")) {
            val elements = id.split("-")
            "." + elements.map { numberMap.getOrDefault(it, it).lowercase() }.reduceRight { s, acc -> "$acc.$s" }
        } else {
            ""
        }

        return "$rootPackage$packagePostfix"
    }

    fun pathFromId(id: String) : String {
        return this.basePath + "/" +packageFromId(id).replace(".", "/")
    }

    fun filenameFromId(id: String) : String {
        return classNameFromId(id) + ".java"
    }

    private fun generateImportTemplate(childrenIds: Array<String>, template: Template): String {
        return if (template.childImport != null) {
            val templateContent = TreeRunner::class.java.getResource(template.childImport)?.readText()
            childrenIds.map {
                val s = StringSubstitutor(mapOf(
                    "child_service_package" to packageFromId(it),
                    "child_service_class" to classNameFromId(it)))
                s.replace(templateContent)
            }.joinToString("")
        } else {
            ""
        }
    }

    private fun generateFieldsTemplate(childrenIds: Array<String>, template: Template): String {
        return if (template.childFields != null) {
            val templateContent = TreeRunner::class.java.getResource(template.childFields)?.readText()
            childrenIds.map {
                val s = StringSubstitutor(mapOf(
                    "child_service_class" to classNameFromId(it),
                    "child_service" to classNameFromId(it).lowercase()))
                s.replace(templateContent)
            }.joinToString("")
        } else {
            ""
        }
    }
    private fun generateCallsTemplate(childrenIds: Array<String>, template: Template): String {
        return if (template.childCalls != null) {
            val templateContent = TreeRunner::class.java.getResource(template.childCalls)?.readText()
            childrenIds.map {
                val s = StringSubstitutor(mapOf(
                    "child_service" to classNameFromId(it).lowercase()))
                s.replace(templateContent)
            }.joinToString("")
        } else {
            ""
        }
    }

    fun generateClassFromTemplate(id: String, parentId: String, childrenIds: Array<String>, template: Template): String {
        val fields = generateFieldsTemplate(childrenIds, template)
        val imports = generateImportTemplate(childrenIds, template)
        val calls = generateCallsTemplate(childrenIds, template)
        val s = StringSubstitutor(mapOf(
            "service_package" to packageFromId(id),
            "service_class" to classNameFromId(id),
            "parent_service_class" to classNameFromId(parentId),
            "parent_service_package" to packageFromId(parentId),
            "children_inject" to fields,
            "children_import" to imports,
            "children_call" to calls,
        ))

        val templateContent = TreeRunner::class.java.getResource(template.templateId)?.readText()
        val content = s.replace(templateContent)

        return content

    }
    fun generateNodes(parentId: String, children: Int, level: Int): Pair<Int, Array<String>> {
        var counter = 0
        var childrenIds: Array<String> = arrayOf()
        for (i in 1..children) {
            val id = if (parentId.equals("")) {
                i.toString()
            } else {
                "$parentId-$i"
            }
            childrenIds += id;


//            createFile(pathFromId(id), filenameFromId(id), content)
//            counter++

            val generated = if(level > 0) {
                generateNodes(id, children, level - 1)
            } else {
                Pair(0, arrayOf())
            }
            counter += generated.first

            val content = generateClassFromTemplate(id, parentId, generated.second, template)
            createFile(pathFromId(id), filenameFromId(id), content)
            counter++
        }

        return Pair(counter, childrenIds)
    }

    fun createFile(path: String, filename: String, content: String) {
        File(path).mkdirs()
        File(path, filename).printWriter().use { out ->
            out.print(content)
        }
    }

}

