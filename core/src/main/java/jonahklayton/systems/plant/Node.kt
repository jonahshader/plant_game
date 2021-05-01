package jonahklayton.systems.plant

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import space.earlygrey.shapedrawer.ShapeDrawer

open class Node(relativeTargetPosition: Vector2, parent: Node?, plant: Plant){
    var relativePosition = Vector2(relativeTargetPosition).nor().scl(0.0001F) //Not zero so the node doesn't immediately die.
        private set

    private var relativeTargetPosition = relativeTargetPosition.cpy()

    var targetLength = relativeTargetPosition.len()

    var worldPosition: Vector2 = Vector2(relativePosition).add(parent?.worldPosition ?: plant.worldPosition)
        private set

    var isDead = false
        private set

    var parent: Node? = parent
        private set

    var children = ArrayList<Node>()
        private set

    var plant = plant

    var thickness = 3F

    open fun update(timePassed: Float){

    }

    open fun draw(renderer: ShapeDrawer){
        if (parent != null) {
            renderer.line(worldPosition, parent!!.worldPosition, thickness)
        }
    }

    fun addChild(child: Node){
        children.add(child)
    }

    private fun removeChild(child: Node){
        children.remove(child)
    }

    fun die(){
        parent?.removeChild(this)

        isDead = true

        for(i in children) i.die()
    }

    fun updateWorldPosition(){
        worldPosition = Vector2(relativePosition).add(parent?.worldPosition ?: plant.worldPosition)

        for(i in children){
            i.updateWorldPosition();
        }
    }

    fun grow(percent: Float){
        var oldPos = Vector2(relativePosition.x, relativePosition.y)

        relativePosition = Vector2(relativePosition).add(Vector2(relativeTargetPosition).scl(percent/100))

        if (Vector2(relativePosition).dot(oldPos) < 0) die()
    }

    fun isFullyGrown(): Boolean {
        return relativePosition.len() >= relativeTargetPosition.len()
    }

    fun getLength(): Float {
        return relativePosition.len()
    }

}