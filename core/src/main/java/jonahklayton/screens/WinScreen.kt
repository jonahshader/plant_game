package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahklayton.PlantGame
import jonahklayton.screens.GameScreen.Companion.GAME_HEIGHT
import jonahklayton.screens.GameScreen.Companion.GAME_WIDTH
import jonahklayton.systems.screen.ScreenManager
import jonahklayton.systems.sound.SoundSystem
import jonahklayton.systems.ui.TextRenderer
import jonahklayton.systems.ui.menu.Menu
import ktx.app.KtxScreen
import ktx.graphics.begin
import kotlin.math.PI

class WinScreen(private val passedLevel: Int) : KtxScreen {
    private var camera = OrthographicCamera()
    private var viewport = FitViewport(640f, 360f, camera)
    private var menu = Menu(TextRenderer.Font.HEAVY, camera, Vector2(0f, -100f), Vector2(400f, 80f))

    private var time = 0f

    init {
        menu.addMenuItem("Level ${passedLevel+1}") {ScreenManager.switchTo(GameScreen(passedLevel+1))}
        menu.addMenuItem("Main Menu") {ScreenManager.pop()}
    }

    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
        SoundSystem.stopMusic()
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.5f, .5f, .5f, 1f)
        viewport.apply()
        PlantGame.batch.begin(camera)
        TextRenderer.begin(PlantGame.batch, viewport, TextRenderer.Font.NORMAL, 48f, .05f + kotlin.math.sin(time * 2 * PI * 2)
            .toFloat() * .05f)
        TextRenderer.drawTextCentered(0f, 100f, "You Beat Level $passedLevel!", 3f, 1f)
        menu.run(delta, viewport)
        menu.draw(PlantGame.batch, PlantGame.shapeDrawer, viewport)
        TextRenderer.end()
        PlantGame.batch.end()

        time += delta
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}