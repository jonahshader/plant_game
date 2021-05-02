package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahklayton.PlantGame
import jonahklayton.systems.noise.OctaveSet
import jonahklayton.systems.screen.ScreenManager
import jonahklayton.systems.settings.Settings
import jonahklayton.systems.sound.SoundSystem
import jonahklayton.systems.ui.TextRenderer
import jonahklayton.systems.ui.menu.Menu
import jonahklayton.systems.world.Level
import jonahklayton.systems.world.World
import jonahklayton.systems.world.terrain.TerrainGenerator
import ktx.app.KtxScreen
import ktx.graphics.begin

class SettingsScreen : KtxScreen {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(640f, 900f, camera)
    private val menu = Menu(TextRenderer.Font.HEAVY, camera, Vector2(), Vector2(500f, 90f))

    init {
        menu.addSliderItem("Overall Volume", SoundSystem.overallVolume) {
            t->SoundSystem.overallVolume = t
            Settings.settings["overall-volume"] = t.toString()
            Settings.save()
        }
        menu.addSliderItem("Music Volume", SoundSystem.musicVolume) {
            t->SoundSystem.musicVolume = t
            Settings.settings["music-volume"] = t.toString()
            Settings.save()
        }
        menu.addSliderItem("Sound Effect Volume", SoundSystem.soundVolume) {
            t->SoundSystem.soundVolume = t
            Settings.settings["sound-volume"] = t.toString()
            Settings.save()
        }
        menu.addMenuItem("Fullscreen") {
            if(!Gdx.graphics.isFullscreen) Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
            else Gdx.graphics.setWindowedMode(Gdx.graphics.width - 40, Gdx.graphics.height - 40)

            Settings.settings["fullscreen"] = Gdx.graphics.isFullscreen.toString()
            Settings.save()

        }
        menu.addMenuItem("Back") { ScreenManager.pop() }
    }

    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.2f, .5f, 1f, 1f)
        menu.run(delta, viewport)

        viewport.apply()

        PlantGame.batch.begin(camera)

        TextRenderer.begin(PlantGame.batch, viewport, TextRenderer.Font.HEAVY, 125f, 0.05f)
        TextRenderer.color = Color.WHITE
        TextRenderer.drawTextCentered(0f, viewport.worldHeight*.5f - 130f, "PVP:", 10f, 0.75f)
        TextRenderer.end()
        TextRenderer.begin(PlantGame.batch, viewport, TextRenderer.Font.HEAVY, 75f, 0.05f)
        TextRenderer.color = Color.WHITE
        TextRenderer.drawTextCentered(0f, viewport.worldHeight*.5f-225f, "Plant Vs Plant", 4f, 0.75f)
        TextRenderer.end()

        menu.draw(PlantGame.batch, PlantGame.shapeDrawer, viewport)

        PlantGame.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

}