package jonahklayton.systems.world.terrain

import com.badlogic.gdx.math.Vector2
import jonahklayton.PlantGame
import jonahklayton.systems.plant.Leaf
import jonahklayton.systems.plant.Stem
import jonahklayton.systems.world.World

class TerrainChunk(val xChunk: Int, val yChunk: Int, private val generator: TerrainGenerator, world: World) {
    companion object {
        const val CHUNK_SIZE = 32
        const val CHUNK_SIZE_WORLD_UNITS = CHUNK_SIZE * TerrainCell.SIZE
        fun cellPosToChunkPos(cellPos: Int) : Int = kotlin.math.floor(cellPos / CHUNK_SIZE.toFloat()).toInt()
        fun worldPosToChunkPos(pos: Float) : Int = kotlin.math.floor(pos / CHUNK_SIZE_WORLD_UNITS).toInt()
        fun worldPosToKey(pos: Vector2) : String = chunkPosToKey(worldPosToChunkPos(pos.x), worldPosToChunkPos(pos.y))
        fun chunkPosToKey(xChunk: Int, yChunk: Int) = "$xChunk $yChunk"
        fun chunkPosToCellPos(xChunk: Int) : Int = xChunk * CHUNK_SIZE
        fun chunkPosToWorldPos(xChunk: Int, yChunk: Int) : Vector2 = Vector2(xChunk * CHUNK_SIZE_WORLD_UNITS, yChunk * CHUNK_SIZE_WORLD_UNITS)
        fun worldPosToCellPos(worldPos: Float) : Int = kotlin.math.floor(worldPos / TerrainCell.SIZE).toInt()
    }
    var queueRemoved = false

    private val cells = mutableListOf<TerrainCell?>()
    val leaves = mutableListOf<Leaf>()
    val stems = mutableListOf<Stem>()

    init {
        generate(world.terrain)
    }

    fun getCell(xCell: Int, yCell: Int) : TerrainCell? = cells[cellPosToIndex(xCell, yCell)]

    fun draw(brightness: Float) {
        cells.forEach { it?.draw(brightness) }
//        PlantGame.shapeDrawer.setColor(1f, 1f, 1f, 1f)
//        PlantGame.shapeDrawer.line(chunkPosToWorldPos(xChunk, yChunk), chunkPosToWorldPos(xChunk, yChunk + 1))
//        PlantGame.shapeDrawer.line(chunkPosToWorldPos(xChunk, yChunk), chunkPosToWorldPos(xChunk + 1, yChunk))
    }

    fun update(dt: Float) {
        cells.forEach { it?.update(dt) }
    }

    private fun generate(terrain: Terrain) {
        for (i in 0 until CHUNK_SIZE * CHUNK_SIZE) {
            val xCell = chunkPosToCellPos(xChunk) + indexToX(i)
            val yCell = chunkPosToCellPos(yChunk) + indexToY(i)
            cells += generator.makeCellAt(xCell, yCell, terrain)
        }
    }

    private fun inThis(worldPos: Vector2): Boolean = (worldPosToChunkPos(worldPos.x) == xChunk && worldPosToChunkPos(worldPos.y) == yChunk)
    private fun cellPosToIndex(xCell: Int, yCell: Int) : Int = xCell + yCell * CHUNK_SIZE
    private fun indexToX(index: Int) : Int = index % CHUNK_SIZE
    private fun indexToY(index: Int) : Int = index / CHUNK_SIZE
}