package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import space.earlygrey.shapedrawer.ShapeDrawer
import java.awt.Color

class Leaf(relativeTargetPosition: Vector2, parent: Node, plant: Plant): Node(relativeTargetPosition, parent, plant) {
    override fun draw(renderer: ShapeDrawer){
        renderer.setColor(com.badlogic.gdx.graphics.Color.GREEN)
        super.draw(renderer)
    }
}