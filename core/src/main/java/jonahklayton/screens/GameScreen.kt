package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.*
import jonahklayton.PlantGame
import jonahklayton.systems.ui.Hud
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
    private lateinit var viewport: Viewport
    private lateinit var inputMultiplexer: InputMultiplexer
    private lateinit var world: World

    override fun show() {
        worldCamera = OrthographicCamera()
        viewport = FillViewport(GAME_WIDTH, GAME_HEIGHT, worldCamera)
        val gen = TerrainGenerator(151253)
        gen.octaveSet.addTwisterOctaveFractal(.01, 1.0, .5, .5, 5)
        gen.octaveSet.addOctaveFractal(.005, 1.0, .5, .5, 4)
        inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer
        world = World(Level(Vector2(), Vector2(50f, 13f), gen), inputMultiplexer, worldCamera)
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun render(delta: Float) {
        // run stuff
        world.update(delta)

        ScreenUtils.clear(.2f, .5f, 1f, 1f)
        viewport.apply()
        PlantGame.batch.begin(worldCamera)
//        TextRenderer.begin(PlantGame.batch, viewport, TextRenderer.Font.NORMAL, 32f, 0f)
//        TextRenderer.color = Color.BLACK
//        TextRenderer.drawTextCentered(0f, 0f, "HELooolOOOLHfiodh")
//        TextRenderer.end()
        world.draw(PlantGame.shapeDrawer)
        PlantGame.batch.end()

        Hud.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        Hud.resize(width, height)
    }

    fun mouseToWorldVec() : Vector2 {
        val vec = worldCamera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        return Vector2(vec.x, vec.y)
    }
}