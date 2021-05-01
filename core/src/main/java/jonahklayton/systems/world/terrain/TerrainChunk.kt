package jonahklayton.systems.world.terrain

import com.badlogic.gdx.math.Vector2
import java.lang.Math.floor
import kotlin.math.roundToInt

class TerrainChunk {
    companion object {
        const val CHUNK_SIZE = 32
        const val CHUNK_SIZE_WORLD_UNITS = CHUNK_SIZE * TerrainCell.SIZE
        fun worldPositionToChunk(pos: Float) : Int = kotlin.math.floor(pos / CHUNK_SIZE_WORLD_UNITS).toInt()
        fun worldPositionToKey(pos: Vector2) : String {
            val xChunk = worldPositionToChunk(pos.x)
            val yChunk = worldPositionToChunk(pos.y)
            return "$xChunk $yChunk"
        }


    }
    var queueRemoved = false
}