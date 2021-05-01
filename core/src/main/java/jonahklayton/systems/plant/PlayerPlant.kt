package jonahklayton.systems.plant

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import jonahklayton.screens.GameScreen
import jonahklayton.systems.world.World
import ktx.app.KtxInputAdapter

class PlayerPlant(position: Vector2, energy: Float, world: World, camera: Camera) : Plant(position, energy, world), KtxInputAdapter {

    var camera = camera

    override fun update(timePassed: Float){
        super.update(timePassed)

    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        var worldPos = mouseToWorldVec(Vector2(screenX.toFloat(), screenY.toFloat()))

        print(worldPos)

        for(i in nodes){
            if(Vector2(i.worldPosition).sub(worldPos).len() < 10) print(i.plant.root.storedEnergy)
        }

        return super.touchDown(screenX, screenY, pointer, button)
    }

    fun mouseToWorldVec(input: Vector2) : Vector2 {
        val vec = camera.unproject(Vector3(input.x, input.y, 0f))
        return Vector2(vec.x, vec.y)
    }

}