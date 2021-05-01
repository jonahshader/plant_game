package jonahklayton.systems.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import java.util.*

object ScreenManager {
    private val stack = Stack<Screen>()
    lateinit var game: Game

    fun push(screen: Screen) {
        stack.push(screen)
        game.screen = screen
    }

    fun pop() {
        stack.pop().dispose()
        game.screen = stack.peek()
    }
}