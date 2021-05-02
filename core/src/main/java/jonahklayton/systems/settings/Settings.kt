package jonahklayton.systems.settings

import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object Settings {
    val settings = Properties()

    init {
        settings.load(FileInputStream("./settings.properties"))
    }

    fun save(){
        settings.store(FileOutputStream("./settings.properties"), "")
    }

}