package io.twobeers

import org.apache.commons.text.StringSubstitutor
import java.io.File

enum class Template(val templateId: String) {
    QUARKUS("/quarkus/quarkus-java.txt"),
    SPRING_BOOT("/springboot/springboot-java.txt"),
}

class TreeRunner(val basePath: String, val template: Template ) {
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

    fun packageFromId(id: String): String {
        val packagePostfix = if (!id.equals("")) {
            val elements = id.split("-")
            "." + elements.map { numberMap.getOrDefault(it, it).lowercase() }.reduceRight { s, acc -> "$acc.$s" }
        } else {
            ""
        }

        return "io.twobeers.service$packagePostfix"
    }

    fun pathFromId(id: String) : String {
        return this.basePath + "/" +packageFromId(id).replace(".", "/")
    }

    fun filenameFromId(id: String) : String {
        return classNameFromId(id) + ".java"
    }

    fun generateClassFromTemplate(id: String, parentId: String, templateId: String): String {
        val s = StringSubstitutor(mapOf(
            "service_package" to packageFromId(id),
            "service_class" to classNameFromId(id),
            "parent_service_class" to classNameFromId(parentId),
            "parent_service_package" to packageFromId(parentId),
        ))

        val template = TreeRunner::class.java.getResource(templateId)?.readText()
        val content = s.replace(template)

        return content

    }
    fun generateNodes(parentId: String, children: Int, level: Int): Int {
        var counter = 0
        for (i in 1..children) {
            val id = if (parentId.equals("")) {
                i.toString()
            } else {
                "$parentId-$i"
            }

            val content = generateClassFromTemplate(id, parentId, template.templateId)

            createFile(pathFromId(id), filenameFromId(id), content)
            counter++

            if(level > 0) {
                counter += generateNodes(id, children, level - 1)
            }
        }
        return counter
    }

    fun createFile(path: String, filename: String, content: String) {
        File(path).mkdirs()
        File(path, filename).printWriter().use { out ->
            out.print(content)
        }
    }

}

