package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.*
import jonahklayton.PlantGame
import jonahklayton.systems.noise.OctaveSet
import jonahklayton.systems.sound.SoundSystem
import jonahklayton.systems.ui.Hud
import jonahklayton.systems.world.Level
import jonahklayton.systems.world.World
import jonahklayton.systems.world.terrain.TerrainGenerator
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.graphics.begin
import kotlin.math.pow

class GameScreen(private val levelNumber: Int) : KtxScreen, KtxInputAdapter {
    companion object {
        const val GAME_WIDTH = 640f
        const val GAME_HEIGHT = 360f
    }

    private lateinit var worldCamera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var inputMultiplexer: InputMultiplexer
    private lateinit var world: World

    private val mousePressPos = Vector2()
    private var mouseDown = false

    override fun show() {
        worldCamera = OrthographicCamera()
        SoundSystem.camera = worldCamera
        viewport = FillViewport(GAME_WIDTH, GAME_HEIGHT, worldCamera)
        val gen = TerrainGenerator(151253)
        gen.octaveSet.addTwisterOctaveFractal(.01, 1.0, .5, .5, 5)
        gen.octaveSet.addOctaveFractal(.005, 1.0, .5, .5, 4)
        inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer
        inputMultiplexer.addProcessor(this)
        val weather = OctaveSet(RandomXS128())
        weather.addOctaveFractal(0.1, 20.0, .5, .5, 3)
        world = World(Level(0f, 250f, gen, weather, levelNumber), inputMultiplexer, worldCamera)
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun render(delta: Float) {
        // mouse panning
        if (mouseDown) {
            val tempDelta = mouseToWorldVec().sub(mousePressPos)
            worldCamera.translate(-tempDelta.x, -tempDelta.y, 0f)
            worldCamera.update()
            mousePressPos.set(mouseToWorldVec())
        }

        // run stuff
        world.update(delta)

        val brightness = world.getSkyBrightness()
        ScreenUtils.clear(.2f * brightness, .5f * brightness, 1f * brightness, 1f)
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

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button == Input.Buttons.MIDDLE) {
            mousePressPos.set(mouseToWorldVec())
            mouseDown = true
            return true
        }

        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button == Input.Buttons.MIDDLE) {
            mouseDown = false
            return true
        }
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        worldCamera.zoom *= 2f.pow(amountY * .5f)
        return true
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