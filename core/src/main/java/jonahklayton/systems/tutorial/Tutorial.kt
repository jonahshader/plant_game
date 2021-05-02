package jonahklayton.systems.tutorial

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ScalingViewport

object Tutorial {
    private val messages = mutableListOf<Message>()
    private var currentIndex = 0
    private var timer = 0f

    init {
        messages += Message("Hello, welcome to PVP!\nPress Space to continue.", Vector2(300f, 50f), Vector2())
        messages += Message("Pan by holding middle mouse button\nand zoom in and out with the scroll wheel.", Vector2(400f, 50f), Vector2())
        messages += Message("Below is your plant's root.\nHold right click and drag up to add a leaf!", Vector2(400f, 50f), Vector2(0f, 100f))
        messages += Message("Leaves cannot be branched off.\nLeft click and drag from the root\ninto the air to make a stem.", Vector2(350f, 80f), Vector2(0f, 40f))
        messages += Message("You can build anything off stems.\nClick and drag from the root into the\nground to make another root.", Vector2(390f, 80f), Vector2(0f, -100f))
        messages += Message("Roots suck up water from the ground.\nWater comes from the soil and is absorbed by stone.\nWater flows through grass the quickest and\nrainfall will occasionally saturate the grass.", Vector2(450f, 100f), Vector2())
        messages += Message("Your plant needs both water and light to grow.\nAbove are indicators that show what the plant needs.", Vector2(450f, 50f), Vector2(0f, 0f))
        messages += Message("The other plant is trying to take your resources!\nKill it by taking its resources to win the level!", Vector2(450f, 50f), Vector2(0f, 0f))
        messages += Message("You can press L to make the light rays visible.\nLeaves absorb some light and stems block some light.", Vector2(400f, 50f), Vector2(0f, 0f))
        messages += Message("Each level gets more difficult.\nHave fun!", Vector2(400f, 50f), Vector2(0f, 0f))
    }

    fun update(dt: Float) {
        if (timer <= 0)
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                timer = 4f
                currentIndex++
            }
        timer -= dt
    }

    fun draw(viewport: ScalingViewport) {
        if (timer <= 0)
        if (currentIndex < messages.size)
            messages[currentIndex].draw(viewport)
    }
}