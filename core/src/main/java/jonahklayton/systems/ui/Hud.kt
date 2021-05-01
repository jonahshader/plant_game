package jonahklayton.systems.ui

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahklayton.PlantGame.Companion.batch
import jonahklayton.PlantGame.Companion.shapeDrawer
import jonahklayton.screens.GameScreen.Companion.GAME_HEIGHT
import jonahklayton.screens.GameScreen.Companion.GAME_WIDTH
import space.earlygrey.shapedrawer.ShapeDrawer
import ktx.graphics.begin

object Hud{
    var camera = OrthographicCamera()
    var viewport = FillViewport(GAME_WIDTH, GAME_HEIGHT, camera)

    fun resize(width: Int, height: Int){
        viewport.update(width, height, true)
    }

    fun draw(){
        var drawer = shapeDrawer

        viewport.apply()
        batch.begin(camera)

        drawer.filledRectangle(Rectangle(100F,100F,10F,10F), Color.BLUE)

        batch.end()
    }
}