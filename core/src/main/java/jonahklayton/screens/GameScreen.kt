package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import jonahklayton.PlantGame
import jonahklayton.systems.ui.TextRenderer
import ktx.app.KtxScreen
import ktx.graphics.begin

class GameScreen : KtxScreen {
    companion object {
        const val GAME_WIDTH = 640f
        const val GAME_HEIGHT = 360f
    }

    private lateinit var worldCamera: Camera
    private lateinit var viewport: ScalingViewport

    override fun show() {
        worldCamera = OrthographicCamera()
        viewport = FillViewport(GAME_WIDTH, GAME_HEIGHT, worldCamera)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(1f, 1f, 1f, 1f)
        PlantGame.batch.begin(worldCamera)
        TextRenderer.begin(PlantGame.batch, viewport, TextRenderer.Font.NORMAL, 32f, 0f)
        TextRenderer.color = Color.BLACK
        TextRenderer.drawTextCentered(0f, 0f, "HELooolOOOLHfiodh")
        TextRenderer.end()
        PlantGame.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    fun mouseToWorldVec() : Vector2 {
        val vec = worldCamera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        return Vector2(vec.x, vec.y)
    }
}