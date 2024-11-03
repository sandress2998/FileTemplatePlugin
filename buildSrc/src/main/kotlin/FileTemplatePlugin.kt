import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property

class FileTemplatePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("createFileTemplate", CreateFileTemplate::class.java) {
            fileName.set(target.findProperty("fileName")?.toString() ?: "defaultFile")
        }
    }
}

abstract class CreateFileTemplate: DefaultTask() {
    @get:Input
    abstract val fileName: Property<String>

    private val extension: String = "kt"

    @TaskAction
    fun execute() {
        val file = File("src/main/kotlin/${fileName.get()}.$extension")

        file.parentFile.mkdirs() // Создаем директории, если их нет
        file.createNewFile()
        file.writeText("""
            class Person {
                val name: String = "Ivan"
                val age: Int? = null
            }
            // здесь должен быть какой-то шаблон
        """.trimIndent())
    }
}