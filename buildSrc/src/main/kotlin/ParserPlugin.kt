import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property


class ParserPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("analyzeCode", AnalyzeCodeTask::class.java) {
            dirName.set(target.findProperty("dirName")?.toString() ?: "src")
        }
    }
}

abstract class AnalyzeCodeTask: DefaultTask() {
    @get:Input
    abstract val dirName: Property<String>

    @TaskAction
    fun analyzeCode() {
        val dirFile = File(dirName.get())

        dirFile.walk().filter { (it.extension == "kt") }.forEach {
            val file = Analyzer(it)
            println("File $it contains ${file.analyzeClasses()} classes")
            println("File $it contains ${file.analyzeFunctions()} functions")
            println("File $it contains ${file.analyzeLines()} lines of code")
        }
    }
}

// анализирует классы и функции конкретного файла
class Analyzer(file: File) {
    companion object {
        const val KOTLIN_FUN = "fun "
        const val KOTLIN_CLASS = "class "
    }

    val lines = file.readLines()

    fun analyzeClasses(): Int {
        var count = 0
        lines.forEach { line ->
            if (line.contains(KOTLIN_CLASS)) ++count
        }
        return count
    }

    fun analyzeFunctions(): Int {
        var count = 0
        lines.forEach { line ->
            if (line.contains(KOTLIN_FUN)) ++count
        }
        return count
    }

    fun analyzeLines(): Int {
        var count = 0
        lines.forEach { line ->
            if (line != "") ++count
        }
        return count
    }
}