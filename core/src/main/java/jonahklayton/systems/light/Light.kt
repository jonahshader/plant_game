package jonahklayton.systems.light

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.World
import space.earlygrey.shapedrawer.ShapeDrawer
import kotlin.math.PI
import kotlin.math.pow

class Light(private val world: World) {
    companion object {
        const val MAX_STARTING_ENERGY = 3.125f
        fun dayLightRadians(world: World) : Float {
            val angle = Vector2(1f, 0f).rotateRad((-world.getDayProgress() * 2 * PI + PI).toFloat())
            angle.y += .33f
            return angle.angleRad()
        }
    }
    private val rays = mutableListOf<Ray>()
    private val rand = RandomXS128()

    private val worldToSunAngle = Vector2()
    private val lightSpawnPosCenter = Vector2()
    private val lightSpawnPosLineCenter = Vector2()
    private var spawnLineLength = 10f

    private var raysPerLengthPerSecond = 2f
    private var spawnQueue = 0f

    private var renderingEnabled = false

    private fun updateLightAngle() {
        worldToSunAngle.set(1f, 0f)
        worldToSunAngle.setAngleRad(dayLightRadians(world))
    }

//    private fun updateLightSpawn() {
//        lightSpawnPosCenter.set(0f, 0f)
//        if (world.getAllLeaves().size > 0) {
//            world.getAllLeaves().forEach {
//                lightSpawnPosCenter.add(it.worldPosition)
//            }
//            lightSpawnPosCenter.scl(1f/world.getAllLeaves().size)
//        }
//    }

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

            lightSpawnPosCenter.set(min)
            lightSpawnPosCenter.add(max)
            lightSpawnPosCenter.scl(.5f)
            max.sub(min)
            spawnLineLength = max.len() * 1.1f
        }
    }

    fun update(dt: Float) {
        spawnQueue += dt * raysPerLengthPerSecond * spawnLineLength

        val toSpawn = spawnQueue.toInt()
        spawnQueue -= toSpawn

        updateLightAngle()
//        updateLightSpawn()
        updateLightSpawnLineLength()

        if (worldToSunAngle.y > 0) {
            val lightLinePerpendicular = Vector2(worldToSunAngle).scl(spawnLineLength*.5f)
            lightSpawnPosLineCenter.set(lightSpawnPosCenter)
            lightSpawnPosLineCenter.add(lightLinePerpendicular)

            val lightPerpendicular = Vector2(worldToSunAngle).rotate90(1)
            val perpendicularOffset = Vector2()
            for (i in 0 until toSpawn) {
                perpendicularOffset.set(lightPerpendicular)
                perpendicularOffset.scl(spawnLineLength * (rand.nextFloat() - .5f))
                val rayPos = Vector2(lightSpawnPosLineCenter).add(perpendicularOffset)
                rays += Ray(rayPos, Vector2(worldToSunAngle).scl(-1f), MAX_STARTING_ENERGY * sin(dayLightRadians(world)).coerceAtLeast(0f).pow(1/2f), spawnLineLength, world)
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L) || Gdx.input.isKeyJustPressed(Input.Keys.P)) renderingEnabled = !renderingEnabled

        // run rays
//        rays.parallelStream().forEach {it.update()}
        // run two iterations per frame
        rays.forEach {it.update()}
        rays.removeIf{it.queueRemoval}
        rays.forEach {it.update()}
        rays.removeIf{it.queueRemoval}
    }

    fun draw(shapeDrawer: ShapeDrawer) {
        if (renderingEnabled)
            rays.forEach{it.draw(shapeDrawer)}
    }
}