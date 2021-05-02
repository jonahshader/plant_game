package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahklayton.PlantGame
import jonahklayton.systems.noise.OctaveSet
import jonahklayton.systems.screen.ScreenManager
import jonahklayton.systems.sound.SoundSystem
import jonahklayton.systems.ui.TextRenderer
import jonahklayton.systems.ui.menu.Menu
import jonahklayton.systems.world.Level
import jonahklayton.systems.world.World
import jonahklayton.systems.world.terrain.TerrainGenerator
import ktx.app.KtxScreen
import ktx.graphics.begin

class MenuScreen : KtxScreen {
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(640f, 900f, camera)
    private val menu = Menu(TextRenderer.Font.HEAVY, camera, Vector2(), Vector2(500f, 90f))
    private lateinit var world: World
    private lateinit var worldCamera: OrthographicCamera
    private lateinit var bgViewport: FillViewport

    init {
        menu.addMenuItem("Singleplayer") {ScreenManager.push(GameScreen(1))}
        menu.addMenuItem("Settings") {}
        menu.addMenuItem("Exit") {Gdx.app.exit()}
    }

    override fun show() {
        worldCamera = OrthographicCamera()
        SoundSystem.camera = worldCamera
        bgViewport = FillViewport(GameScreen.GAME_WIDTH, GameScreen.GAME_HEIGHT, worldCamera)
        val gen = TerrainGenerator(1)
        gen.octaveSet.addTwisterOctaveFractal(.01, 1.0, .5, .5, 5)
        gen.octaveSet.addOctaveFractal(.005, 1.0, .5, .5, 4)
        val weather = OctaveSet(RandomXS128())
        weather.addOctaveFractal(0.05, 20.0, .5, .5, 3)
        world = World(Level(0f, 250f+50f*1, gen, weather, 1), InputMultiplexer(), worldCamera, true)

        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
        bgViewport.update(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.25f, .25f, .25f, 1f)
        menu.run(delta)
        world.update(delta)

        val brightness = world.getSkyBrightness()
        ScreenUtils.clear(.2f * brightness, .5f * brightness, 1f * brightness, 1f)

        bgViewport.apply()

        PlantGame.batch.begin(worldCamera)
        world.draw(PlantGame.shapeDrawer)
        PlantGame.batch.end()

        viewport.apply()

        PlantGame.batch.begin(camera)
        menu.draw(PlantGame.batch, PlantGame.shapeDrawer, viewport)
        PlantGame.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        bgViewport.update(width, height)
    }
}