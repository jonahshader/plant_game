package jonahklayton.systems.world.terrain

import java.lang.Math.floor
import kotlin.math.roundToInt

class TerrainChunk {
    companion object {
        const val CHUNK_SIZE = 32
        const val CHUNK_SIZE_WORLD_UNITS = CHUNK_SIZE * TerrainCell.SIZE
        fun positionToChunk(pos: Float) : Int = kotlin.math.floor(pos / CHUNK_SIZE_WORLD_UNITS).toInt()
    }
    var queueRemoved = false
}