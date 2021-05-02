package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import space.earlygrey.shapedrawer.ShapeDrawer
import java.awt.Color

class Leaf(relativeTargetPosition: Vector2, parent: Node, plant: Plant): Node(relativeTargetPosition, parent, plant) {
    override fun draw(renderer: ShapeDrawer, brightness: Float, hue: Float){
        color.set(0.1f, brightness * .9f, 0.1f, 1f)
        super.draw(renderer, brightness, hue)
    }
}