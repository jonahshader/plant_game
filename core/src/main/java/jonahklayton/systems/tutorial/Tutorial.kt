package jonahklayton.systems.tutorial

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ScalingViewport

object Tutorial {
    private val messages = mutableListOf<Message>()
    private var currentIndex = 0

    init {
        messages += Message("Hello, welcome to PVP!\nPress Space to continue.", Vector2(300f, 50f), Vector2())
        messages += Message("Pan by holding middle mouse button\nand zoom in and out with the scroll wheel.", Vector2(400f, 50f), Vector2())
        messages += Message("Below is your plant's root.\nHold right click and drag up to add a leaf!", Vector2(400f, 50f), Vector2(0f, 100f))
        messages += Message("Leaves cannot be branched off.\nLeft click and drag from the root\ninto the air to make a stem.", Vector2(300f, 80f), Vector2(0f, 40f))
        messages += Message("You can build anything off stems.\nClick and drag from the root into the\nground to make another root.", Vector2(400f, 50f), Vector2(0f, -100f))
        messages += Message("Your plant needs both water and light to grow.\n", Vector2(400f, 50f), Vector2(0f, -200f))
    }

    fun update(dt: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) currentIndex++
    }

    fun draw(viewport: ScalingViewport) {
        if (currentIndex < messages.size)
            messages[currentIndex].draw(viewport)
    }
}