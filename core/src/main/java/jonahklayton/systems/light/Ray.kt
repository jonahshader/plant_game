package jonahklayton.systems.light

import com.badlogic.gdx.math.Vector2
import external.isIntersecting
import jonahklayton.systems.light.Light.Companion.MAX_STARTING_ENERGY
import jonahklayton.systems.world.World
import jonahklayton.systems.world.terrain.TerrainChunk
import space.earlygrey.shapedrawer.ShapeDrawer

class Ray(private val pos: Vector2, angle: Vector2, var energy: Float, var travelDistanceRemaining: Float, private val world: World) {
    private val efficiency = .75f
    private val stemReduction = .3f
    private val length = 4f
    private val startingEnergy = energy
    private val tipPos = Vector2()
    private val lengthVector = Vector2(angle)
    var queueRemoval = false

    init {
        lengthVector.scl(length)
        tipPos.add(pos).add(lengthVector)

        moveToLoadedChunk()
    }

    private fun move() {
        pos.add(lengthVector)
        tipPos.add(lengthVector)
        travelDistanceRemaining -= length
    }

    private fun moveToLoadedChunk() {
        while (travelDistanceRemaining > 0 && !world.terrain.isInLoadedChunk(tipPos)) {
            move()
        }
        if (travelDistanceRemaining <= 0) {
            queueRemoval = true
            return
        }
    }

    fun update() {
        move()
        if (travelDistanceRemaining <= 0) {
            queueRemoval = true
            return
        }

        if (world.terrain.isInLoadedChunk(tipPos)) {
            // try colliding with leaves
            val chunk = world.terrain.keyToChunk[TerrainChunk.worldPosToKey(tipPos)]
            chunk?.leaves?.forEach {
                if (it.parent != null) {
                    if (isIntersecting(pos, tipPos, it.parent!!.worldPosition, it.worldPosition)) {
                        it.plant.receiveLight(efficiency * energy)
                        energy *= (1 - efficiency)
                    }
                }
            }
//            world.getAllLeaves().forEach {
//                if (it.parent != null) {
//                    if (isIntersecting(pos, tipPos, it.parent!!.worldPosition, it.worldPosition)) {
//                        it.plant.receiveLight(efficiency * energy)
//                        energy *= (1 - efficiency)
//                    }
//                }
//            }
//            world.getAllStems().forEach {
//                if (it.parent != null) {
//                    if (isIntersecting(pos, tipPos, it.parent!!.worldPosition, it.worldPosition)) {
//                        energy *= (1 - stemReduction)
//                    }
//                }
//            }
            chunk?.stems?.forEach {
                if (it.parent != null) {
                    if (isIntersecting(pos, tipPos, it.parent!!.worldPosition, it.worldPosition)) {
                        energy *= (1 - stemReduction)
                    }
                }
            }
        }

        if (energy / startingEnergy < .1) {
            queueRemoval = true
            return
        }

        // try colliding with ground
        if (world.terrain.isUnderground(tipPos)) {
            queueRemoval = true
            return
        }
    }

    fun draw(shapeDrawer: ShapeDrawer) {
        shapeDrawer.setColor(1f, 1f, 0f, .75f * energy / MAX_STARTING_ENERGY)
        shapeDrawer.line(pos, tipPos)
    }
}