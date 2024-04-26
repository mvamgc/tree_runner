package io.twobeers

/**
 * Hello world!
 *
 */
object App {
    @JvmStatic
    fun main(args: Array<String>) {
        treeRunner()
    }

    fun treeRunner() {
        val nCreated = TreeRunner(".", Template.QUARKUS)
            .generateNodes("", 3, 5)
        println("Created files: $nCreated")
    }
}
