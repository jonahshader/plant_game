package jonahklayton.systems.tutorial

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahklayton.PlantGame
import jonahklayton.systems.ui.CustomShapes
import jonahklayton.systems.ui.TextRenderer

class Message(private val text: String, private val size: Vector2, private val position: Vector2) {
    fun draw(viewport: ScalingViewport) {
        val lines = text.filterIndexed{ _: Int, c: Char -> c == '\n'}.length
        val lineHeight = (size.y-8)/(lines+1)

        PlantGame.shapeDrawer.setColor(.8f, .8f, .8f, .8f)
        CustomShapes.filledRoundedRect(PlantGame.shapeDrawer, position.x - size.x/2f, position.y - size.y/2f, size.x, size.y, 8f)

        TextRenderer.begin(PlantGame.batch, viewport, TextRenderer.Font.NORMAL, lineHeight, 0f)
        TextRenderer.color.set(1f, 1f, 1f, 1f)
        TextRenderer.drawTextCentered(position.x, position.y + (size.y/2f) - lineHeight*.5f, text, 1f, .75f)
        TextRenderer.end()
    }
}