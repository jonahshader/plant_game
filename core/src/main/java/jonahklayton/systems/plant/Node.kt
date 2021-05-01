package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2

enum class NodeType {
    Leaf,
    Stem,
    Root
}

class Node(relativeTargetPosition: Vector2, type: NodeType, parent: Node){
    var relativePosition = Vector2(0.1F, 0.1F) //Not zero so the node doesn't immediately die.
        private set

    private var relativeTargetPosition = relativeTargetPosition

    var worldPosition: Vector2 = relativePosition.add(parent.worldPosition)
        private set

    var type = type
        private set

    var isDead = false
        private set

    var parent: Node? = parent
        private set

    var children = ArrayList<Node>()
        private set

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

    private fun updateWorldPosition(){
        worldPosition = relativePosition.add(parent?.worldPosition ?: Vector2.Zero)

        for(i in children){
            i.updateWorldPosition();
        }
    }

    fun grow(percent: Float){
        var oldPos = Vector2(relativePosition.x, relativePosition.y)

        relativePosition = relativePosition.add(relativeTargetPosition.scl(percent/100))

        if (relativePosition.dot(oldPos) < 0) die()

        updateWorldPosition()
    }

    fun isFullyGrown(): Boolean {
        return relativePosition.len() >= relativeTargetPosition.len()
    }

}