package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import space.earlygrey.shapedrawer.ShapeDrawer

open class Node(relativeTargetPosition: Vector2, parent: Node?, plant: Plant){
    var relativePosition = Vector2(0.1F, 0.1F) //Not zero so the node doesn't immediately die.
        private set

    private var relativeTargetPosition = relativeTargetPosition

    var targetLength = relativeTargetPosition.len()

    var worldPosition: Vector2 = relativePosition.add(parent?.worldPosition ?: plant.worldPosition)
        private set

    var isDead = false
        private set

    var parent: Node? = parent
        private set

    var children = ArrayList<Node>()
        private set

    var plant = plant

    open fun update(timePassed: Float){

    }

    fun draw(renderer: ShapeDrawer){
        if (parent != null) {
            renderer.line(worldPosition, parent!!.worldPosition)
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
        worldPosition = relativePosition.add(parent?.worldPosition ?: plant.worldPosition)

        for(i in children){
            i.updateWorldPosition();
        }
    }

    fun grow(percent: Float){
        var oldPos = Vector2(relativePosition.x, relativePosition.y)

        relativePosition = relativePosition.add(relativeTargetPosition.scl(percent/100))

        if (relativePosition.dot(oldPos) < 0) die()
    }

    fun isFullyGrown(): Boolean {
        return relativePosition.len() >= relativeTargetPosition.len()
    }

    fun getLength(): Float {
        return relativePosition.len()
    }

}