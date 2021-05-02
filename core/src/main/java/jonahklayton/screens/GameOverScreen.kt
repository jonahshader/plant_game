package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahklayton.PlantGame
import jonahklayton.systems.screen.ScreenManager
import jonahklayton.systems.ui.TextRenderer
import jonahklayton.systems.ui.menu.Menu
import ktx.app.KtxScreen
import ktx.graphics.begin
import kotlin.math.PI

class GameOverScreen(private val failedLevel: Int) : KtxScreen {
    private var camera = OrthographicCamera()
    private var viewport = FitViewport(640f, 360f, camera)
    private var menu = Menu(TextRenderer.Font.HEAVY, camera, Vector2(0f, -100f), Vector2(400f, 80f))

    private var time = 0f

    init {
        menu.addMenuItem("Redo Level $failedLevel") {ScreenManager.switchTo(GameScreen(failedLevel))}
        menu.addMenuItem("Main Menu") { ScreenManager.pop()}
    }

    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.5f, .5f, .5f, 1f)
        viewport.apply()
        PlantGame.batch.begin(camera)
        TextRenderer.begin(
            PlantGame.batch, viewport, TextRenderer.Font.HEAVY, 48f, .05f + kotlin.math.sin(time * 2 * PI * 2)
            .toFloat() * .05f)
        TextRenderer.drawTextCentered(0f, 100f, "You Failed Level $failedLevel!", 3f, 1f)
        menu.run(delta)
        menu.draw(PlantGame.batch, PlantGame.shapeDrawer, viewport)
        TextRenderer.end()
        PlantGame.batch.end()

        time += delta
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}