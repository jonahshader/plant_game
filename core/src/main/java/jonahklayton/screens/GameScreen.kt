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
import jonahklayton.systems.world.Level
import jonahklayton.systems.world.World
import jonahklayton.systems.world.terrain.TerrainGenerator
import ktx.app.KtxScreen
import ktx.graphics.begin

class GameScreen : KtxScreen {
    companion object {
        const val GAME_WIDTH = 640f
        const val GAME_HEIGHT = 360f
    }

    private lateinit var worldCamera: Camera
    private lateinit var viewport: ScalingViewport
    private lateinit var world: World

    override fun show() {
        worldCamera = OrthographicCamera()
        viewport = FillViewport(GAME_WIDTH, GAME_HEIGHT, worldCamera)
        val gen = TerrainGenerator(69)
        gen.octaveSet.addTwisterOctaveFractal(.05, 1.0, .5, .5, 3)
        gen.octaveSet.addOctaveFractal(.01, 1.0, .5, .5, 2)
        world = World(Level(Vector2(), Vector2(50f, 0f), gen))
    }

    override fun render(delta: Float) {
        // run stuff
        world.update(delta)

        ScreenUtils.clear(.2f, .5f, 1f, 1f)
        PlantGame.batch.begin(worldCamera)
        TextRenderer.begin(PlantGame.batch, viewport, TextRenderer.Font.NORMAL, 32f, 0f)
        TextRenderer.color = Color.BLACK
//        TextRenderer.drawTextCentered(0f, 0f, "HELooolOOOLHfiodh")
        TextRenderer.end()
        world.draw(PlantGame.shapeDrawer)
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