package jonahklayton.systems.plant

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import jonahklayton.screens.GameScreen
import jonahklayton.systems.world.World
import ktx.app.KtxInputAdapter
import space.earlygrey.shapedrawer.ShapeDrawer

class PlayerPlant(position: Vector2, energy: Float, world: World, camera: Camera) : Plant(position, energy, world),
    KtxInputAdapter {

    private val MAX_SIZE = 50F
    private val CLICK_DISTANCE = 3F

    var selectedNode: Node? = null

    var camera = camera

    override fun update(timePassed: Float) {
        super.update(timePassed)

    }

    override fun draw(renderer: ShapeDrawer, brightness: Float) {
        if (selectedNode != null) {
            renderer.setColor(0.5f, 0.5f, 0.5f, 0.5f)
            var relPos = mouseToWorldVec(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).sub(selectedNode!!.worldPosition)
                    if(relPos.len() > MAX_SIZE) relPos.nor().scl(MAX_SIZE)
            renderer.line(
                selectedNode!!.worldPosition,
                selectedNode!!.worldPosition.cpy().add(relPos),
                3f
            )
        }

        super.draw(renderer, brightness)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        var worldPos = mouseToWorldVec(Vector2(screenX.toFloat(), screenY.toFloat()))

        for (i in (roots + stems)) {
            if (Vector2(i.worldPosition).sub(worldPos).len() < CLICK_DISTANCE) {
                selectedNode = i
                break
            }
        }

        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        var worldPos = mouseToWorldVec(Vector2(screenX.toFloat(), screenY.toFloat()))

        if (button == Input.Buttons.RIGHT) {
            for (i in roots) {
                if (i.worldPosition.cpy().sub(worldPos).len() < CLICK_DISTANCE) {
                    i.thicken()
                }
            }
        }

        if (selectedNode != null) {
            var relPos = worldPos.cpy().sub(selectedNode!!.worldPosition)

            if(relPos.len() > MAX_SIZE){
                relPos.nor().scl(MAX_SIZE)
            }

            if (relPos.len() > CLICK_DISTANCE) when {
                selectedNode!!.plant.world.terrain.isUnderground(worldPos) -> {
                    var child = Root(relPos, selectedNode!!, selectedNode!!.plant, 0F, world.terrain)
                    selectedNode!!.addChild(child)
                    selectedNode!!.plant.addRoot(child)
//                    print("root")
                }
                button == Input.Buttons.LEFT -> {
                    var child = Stem(relPos, selectedNode!!, selectedNode!!.plant)
                    selectedNode!!.addChild(child)
                    selectedNode!!.plant.addStem(child)
//                    print("stem")
                }
                button == Input.Buttons.RIGHT -> {
                    var child = Leaf(relPos, selectedNode!!, selectedNode!!.plant)
                    selectedNode!!.addChild(child)
                    selectedNode!!.plant.addLeaf(child)
//                    print("leaf")
                }

            }
        }

        selectedNode = null
        return super.touchUp(screenX, screenY, pointer, button)
    }

    fun mouseToWorldVec(input: Vector2): Vector2 {
        val vec = camera.unproject(Vector3(input.x, input.y, 0f))
        return Vector2(vec.x, vec.y)
    }

}