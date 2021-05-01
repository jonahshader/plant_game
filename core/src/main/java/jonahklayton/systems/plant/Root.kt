package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2

class Root(relativeTargetPosition: Vector2, parent: Node, plant: Plant): Node(relativeTargetPosition, parent, plant) {
    var thickness = 1

    fun getWater(quantity: Float): Float{
        //TODO: tell terrain water taken and return the amount we got
        return 0F
    }

}