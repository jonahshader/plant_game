package jonahklayton.systems.plant

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import jdk.javadoc.internal.doclets.toolkit.util.DocFinder
import jonahklayton.screens.GameScreen
import jonahklayton.systems.world.World
import ktx.app.KtxInputAdapter

class PlayerPlant(position: Vector2, energy: Float, world: World, camera: Camera) : Plant(position, energy, world), KtxInputAdapter {

    var selectedNode: Node? = null

    var camera = camera

    override fun update(timePassed: Float){
        super.update(timePassed)

    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        var worldPos = mouseToWorldVec(Vector2(screenX.toFloat(), screenY.toFloat()))

        for(i in nodes){
            if(Vector2(i.worldPosition).sub(worldPos).len() < 20){
                print(i.plant.root.storedEnergy)
                selectedNode = i
                break
            }
        }

        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (selectedNode != null) {
            var worldPos = mouseToWorldVec(Vector2(screenX.toFloat(), screenY.toFloat()))
            var relPos = worldPos.cpy().sub(selectedNode!!.worldPosition)

            if(selectedNode)
            if(button == Input.Buttons.LEFT) {
                var child = Stem(relPos, selectedNode!!, selectedNode!!.plant)
                selectedNode!!.addChild(child)
                selectedNode!!.plant.addStem(child)
            }
            if(button == Input.Buttons.RIGHT) {
                var child = Leaf(relPos, selectedNode!!, selectedNode!!.plant)
                selectedNode!!.addChild(child)
                selectedNode!!.plant.addLeaf(child)
            }
        }

        return super.touchUp(screenX, screenY, pointer, button)
    }

    fun mouseToWorldVec(input: Vector2) : Vector2 {
        val vec = camera.unproject(Vector3(input.x, input.y, 0f))
        return Vector2(vec.x, vec.y)
    }

}