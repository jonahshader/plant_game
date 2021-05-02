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
import jonahklayton.systems.plant.Plant
import space.earlygrey.shapedrawer.ShapeDrawer
import ktx.graphics.begin
import java.util.Collections.min
import kotlin.math.min

object Hud{
    var camera = OrthographicCamera()
    var viewport = FitViewport(GAME_WIDTH, GAME_HEIGHT, camera)
    var plant: Plant? = null

    fun resize(width: Int, height: Int){
        viewport.update(width, height)
    }

    fun draw(){
        var drawer = shapeDrawer

        var storedEnergy = plant!!.storedEnergy()
        var maxStoredEnergy = plant!!.storedEnergyCapacity()
        var availableEnergy = plant!!.energy
        var maxAvailableEnergy = maxStoredEnergy/20

        var storageRatio = min(1F,storedEnergy/maxStoredEnergy)
        var availableRatio = min(1F, availableEnergy/maxAvailableEnergy)

        viewport.apply()
        batch.begin(camera)

        drawer.filledRectangle(Rectangle(-105F,GAME_HEIGHT/2-30F, 210F, 25F), Color(0.5F, 0.5F, 0.5F, 0.5F))
        drawer.filledRectangle(Rectangle(-105F,GAME_HEIGHT/2-60F, 210F, 25F), Color(0.5F, 0.5F, 0.5F, 0.5F))
        drawer.filledRectangle(Rectangle(-100F,GAME_HEIGHT/2-25F, 200F*storageRatio, 15F), Color.BROWN)
        drawer.filledRectangle(Rectangle(-100F,GAME_HEIGHT/2-55F, 200F*availableRatio, 15F), Color.YELLOW)

        batch.end()
    }
}