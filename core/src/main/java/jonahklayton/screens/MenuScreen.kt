package jonahklayton.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahklayton.PlantGame
import jonahklayton.systems.screen.ScreenManager
import jonahklayton.systems.ui.TextRenderer
import jonahklayton.systems.ui.menu.Menu
import ktx.app.KtxScreen
import ktx.graphics.begin

class MenuScreen : KtxScreen {
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(640f, 900f, camera)
    private val menu = Menu(TextRenderer.Font.HEAVY, camera, Vector2(), Vector2(500f, 90f))
    init {
        menu.addMenuItem("Singleplayer") {ScreenManager.push(GameScreen())}
        menu.addMenuItem("Settings") {}
        menu.addMenuItem("Exit") {Gdx.app.exit()}
    }

    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)


    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.25f, .25f, .25f, 1f)
        menu.run(delta)
        PlantGame.batch.begin(camera)
        menu.draw(PlantGame.batch, PlantGame.shapeDrawer, viewport)
        PlantGame.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}