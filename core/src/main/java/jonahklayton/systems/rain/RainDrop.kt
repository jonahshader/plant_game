package jonahklayton.systems.rain

import com.badlogic.gdx.math.Vector2
import jonahklayton.PlantGame
import jonahklayton.systems.world.World

class RainDrop(private val direction: Vector2, private val position: Vector2, private val world: World, private val waterAmount: Float, var travelDistanceRemaining: Float) {
    private val step = Vector2()
    private val speed = 300f
    private val radius = 1f
    var queueRemoval = false


    fun update(dt: Float) {
        step.set(direction).scl(dt * speed)
        position.add(step)

        if (world.terrain.isUnderground(position)) {
            world.terrain.getCellFromWorldPos(position)!!.putWater(waterAmount)
            queueRemoval = true
        }

        travelDistanceRemaining -= step.len()

        if (travelDistanceRemaining <= 0) {
            queueRemoval = true
        }
    }

    fun draw(brightness: Float) {
        PlantGame.shapeDrawer.setColor(0f, 0f, 1f, brightness)
        PlantGame.shapeDrawer.filledCircle(position.x, position.y, radius)
    }
}