package jonahklayton.systems.light

import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.World
import space.earlygrey.shapedrawer.ShapeDrawer
import kotlin.math.PI

class Light(private val world: World) {
    private val rays = mutableListOf<Ray>()
    private val rand = RandomXS128()

    private val worldToSunAngle = Vector2()
    private val lightSpawnPosCenter = Vector2()
    private val lightSpawnPosLineCenter = Vector2()
    private var spawnLineLength = 10f

    private var raysPerSecond = 30f
    private var spawnQueue = 0f

    private fun dayLightRadians() : Float = (-world.getDayProgress() * 2 * PI + PI).toFloat()

    private fun updateLightAngle() {
        worldToSunAngle.set(1f, 0f)
        worldToSunAngle.setAngleRad(dayLightRadians())
    }

    private fun updateLightSpawn() {
        lightSpawnPosCenter.set(0f, 0f)
        if (world.getAllLeaves().size > 0) {
            world.getAllLeaves().forEach {
                lightSpawnPosCenter.add(it.worldPosition)
            }
            lightSpawnPosCenter.scl(1f/world.getAllLeaves().size)
        }
    }

    private fun updateLightSpawnLineLength() {
        spawnLineLength = 10f
        val min = Vector2()
        val max = Vector2()
        if (world.getAllLeaves().size > 0) {
            world.getAllLeaves().forEach{
                if (it.worldPosition.x < min.x) min.x = it.worldPosition.x
                if (it.worldPosition.x > max.x) max.x = it.worldPosition.x
                if (it.worldPosition.y < min.y) min.y = it.worldPosition.y
                if (it.worldPosition.y > max.y) max.y = it.worldPosition.y
            }

            max.sub(min)
            spawnLineLength = max.len()
        }
    }

    fun update(dt: Float) {
        spawnQueue += dt * raysPerSecond

        val toSpawn = spawnQueue.toInt()
        spawnQueue -= toSpawn

        updateLightAngle()
        updateLightSpawn()
        updateLightSpawnLineLength()

        if (world.getDayProgress() < .5) {
            val lightLinePerpendicular = Vector2(worldToSunAngle).scl(spawnLineLength)
            lightSpawnPosLineCenter.set(lightSpawnPosCenter)
            lightSpawnPosLineCenter.add(lightLinePerpendicular)

            val lightPerpendicular = Vector2(worldToSunAngle).rotate90(1)
            val perpendicularOffset = Vector2()
            for (i in 0 until toSpawn) {
                perpendicularOffset.set(lightPerpendicular)
                perpendicularOffset.scl(spawnLineLength * (rand.nextFloat() - .5f))
                val rayPos = Vector2(lightSpawnPosLineCenter).add(perpendicularOffset)
                rays += Ray(rayPos, Vector2(worldToSunAngle).scl(-1f), sin(dayLightRadians()))
            }
        }

        // run rays
        rays.forEach {it.update()}
    }

    fun draw(shapeDrawer: ShapeDrawer) {
        rays.forEach{it.draw(shapeDrawer)}
    }
}