package jonahklayton

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import jonahklayton.screens.GameScreen
import jonahklayton.screens.MenuScreen
import jonahklayton.systems.assets.Assets
import jonahklayton.systems.screen.ScreenManager
import space.earlygrey.shapedrawer.ShapeDrawer


/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class PlantGame : Game() {
    companion object {
        lateinit var batch: SpriteBatch
            private set
        lateinit var shapeRenderer: ShapeRenderer
            private set
        lateinit var shapeDrawer: ShapeDrawer
            private set
    }

    override fun create() {
        Assets.startLoading()
        Assets.finishLoading()
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        shapeDrawer = ShapeDrawer(batch, Assets.getSprites().findRegion("white_pixel"))
        ScreenManager.game = this       // give screen manager the game instance so that it can change screens
        ScreenManager.push(MenuScreen())
    }
}