package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import space.earlygrey.shapedrawer.ShapeDrawer

class Stem(relativeTargetPosition: Vector2, parent: Node, plant: Plant): Node(relativeTargetPosition, parent, plant) {
    init {
        plant.world.terrain.addStem(this)
    }
    override fun draw(renderer: ShapeDrawer, brightness: Float, hue: Float){
        color.set(0.19f * brightness, .65f * brightness, .19f * brightness, 1f)
        super.draw(renderer, brightness, hue)
    }

    override fun grow(percent: Float) {
        super.grow(percent)
        plant.world.terrain.monitorStem(this)
    }
}