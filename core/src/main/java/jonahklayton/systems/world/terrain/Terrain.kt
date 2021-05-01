package jonahklayton.systems.world.terrain

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Terrain {
    companion object {
        const val LOAD_CHUNK_RADIUS = 2
        const val UNLOAD_CHUNK_RADIUS = 2
    }

    private val keyToChunk = HashMap<String, TerrainChunk>()


    fun getCell(x: Int, y: Int) {

    }

    fun draw(batch: SpriteBatch) {

    }

    fun update(dt: Float) {
        // iterate through all nodes. keep chunks with nodes loaded
        keyToChunk.values.forEach { it.queueRemoved = true }

    }
}