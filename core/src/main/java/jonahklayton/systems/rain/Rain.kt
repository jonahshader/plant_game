package jonahklayton.systems.rain

import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.noise.OctaveSet
import jonahklayton.systems.world.World
import space.earlygrey.shapedrawer.ShapeDrawer

class Rain(private val world: World, private val weather: OctaveSet) {
    private val drops = mutableListOf<RainDrop>()
    private val rand = RandomXS128()

    private var spawnLineLength = 10f

    private val spawnCenter = Vector2()
    private var spawnQueue = 0f
    private var time = 0.0f

    fun update(dt: Float) {
        val dropsPerLengthPerSecond: Float = weather.getValue(time, 0f).coerceAtLeast(0.0).toFloat()
        spawnQueue += dropsPerLengthPerSecond * spawnLineLength * dt
        val toSpawn = spawnQueue.toInt()
        spawnQueue -= toSpawn
        updateSpawnLineWidth()
        updateSpawn()

        for (i in 0 until toSpawn) {
            drops += RainDrop(Vector2(0f, -1f), Vector2(spawnCenter).add((rand.nextFloat() - .5f) * spawnLineLength, spawnLineLength * 2), world, .5f, spawnLineLength * 6f)
        }

        drops.forEach {it.update(dt)}
        drops.removeIf {it.queueRemoval}

        time += dt
    }

    private fun updateSpawnLineWidth() {
        spawnLineLength = 10f
        val min = Vector2()
        val max = Vector2()
        if (world.getAllNodes().size > 0) {
            world.getAllNodes().forEach{
                if (it.worldPosition.x < min.x) min.x = it.worldPosition.x
                if (it.worldPosition.x > max.x) max.x = it.worldPosition.x
                if (it.worldPosition.y < min.y) min.y = it.worldPosition.y
                if (it.worldPosition.y > max.y) max.y = it.worldPosition.y
            }

            max.sub(min)
            spawnLineLength = max.len() * 1.5f
        }
    }

    private fun updateSpawn() {
        spawnCenter.set(0f, 0f)
        if (world.getAllLeaves().size > 0) {
            world.getAllLeaves().forEach {
                spawnCenter.add(it.worldPosition)
            }
            spawnCenter.scl(1f/world.getAllLeaves().size)
        }
    }

    fun draw(skyBrightness: Float) {
        drops.forEach {it.draw(skyBrightness)}
    }
}