package jonahklayton.systems.world.terrain

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.World

class Terrain(private val world: World, private val generator: TerrainGenerator) {
    companion object {
        const val LOAD_CHUNK_RADIUS = 1
    }

    private val keyToChunk = HashMap<String, TerrainChunk>()

    // hopefully this returns null if there isn't a cell there. idk if the '?' operator works like that
    fun getCell(xCell: Int, yCell: Int) : TerrainCell? {
        val xChunk = TerrainChunk.cellPosToChunkPos(xCell)
        val yChunk = TerrainChunk.cellPosToChunkPos(yCell)
        val key = TerrainChunk.chunkPosToKey(xChunk, yChunk)
        return keyToChunk[key]?.getCell(xCell - xChunk * TerrainChunk.CHUNK_SIZE, yCell - yChunk * TerrainChunk.CHUNK_SIZE)
    }

    fun getCellFromWorldPos(worldPos: Vector2) : TerrainCell? {
        return getCell(TerrainChunk.worldPosToCellPos(worldPos.x), TerrainChunk.worldPosToCellPos(worldPos.y))
    }

    fun draw(batch: SpriteBatch) {
        keyToChunk.values.forEach { it.draw(batch) }
    }

    fun update(dt: Float) {
        loadUnloadChunks()
        keyToChunk.values.forEach { it.update(dt) }
    }

    private fun loadUnloadChunks() {
        // iterate through all nodes. keep chunks with nodes loaded
        keyToChunk.values.forEach { it.queueRemoved = true }
        world.getAllNodes().forEach {
            val xChunk = TerrainChunk.worldPosToChunkPos(it.worldPosition.x)
            val yChunk = TerrainChunk.worldPosToChunkPos(it.worldPosition.y)

            for (x in (xChunk-LOAD_CHUNK_RADIUS)..(xChunk+LOAD_CHUNK_RADIUS)) {
                for (y in (yChunk- LOAD_CHUNK_RADIUS)..(yChunk+ LOAD_CHUNK_RADIUS)) {
                    // try load or keep loaded
                    val key = TerrainChunk.chunkPosToKey(x, y)
                    if (!keyToChunk.containsKey(key)) {
                        keyToChunk[key] = TerrainChunk(x, y, generator, this)
                    } else {
                        keyToChunk[key]!!.queueRemoved = false
                    }
                }
            }
        }
        // remove unloaded chunks
        keyToChunk.values.removeIf{it.queueRemoved}
    }

    fun isInLoadedChunk(worldPos: Vector2) : Boolean = keyToChunk.containsKey(TerrainChunk.worldPosToKey(worldPos))

    fun isUnderground(worldPos: Vector2) : Boolean = isInLoadedChunk(worldPos) && getCellFromWorldPos(worldPos) != null
}