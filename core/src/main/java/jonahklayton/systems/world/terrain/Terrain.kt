package jonahklayton.systems.world.terrain

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.plant.Leaf
import jonahklayton.systems.plant.Stem
import jonahklayton.systems.world.World

class Terrain(private val world: World, private val generator: TerrainGenerator) {
    companion object {
        const val LOAD_CHUNK_RADIUS = 2
    }

    // variables for water absorption algo
    private val iterationsPerCell = 2f
    private val tempCellsWithWater = mutableListOf<TerrainCell>()
    private val tempPos = Vector2()
    private val tempStep = Vector2()
    val keyToChunk = HashMap<String, TerrainChunk>()

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

    fun draw(brightness: Float) {
        keyToChunk.values.forEach { it.draw(brightness) }
    }

    fun update(dt: Float) {
        loadUnloadChunks()
        keyToChunk.values.forEach { it.update(dt) }
    }

    fun addLeaf(leaf: Leaf) {
        val xChunk = TerrainChunk.worldPosToChunkPos(leaf.worldPosition.x)
        val yChunk = TerrainChunk.worldPosToChunkPos(leaf.worldPosition.y)
        for (x in -1..1) for (y in -1..1)
            keyToChunk[TerrainChunk.chunkPosToKey(xChunk + x, yChunk + y)]?.leaves?.plusAssign(leaf)
    }
    // call when leaf is growing
    fun monitorLeaf(leaf: Leaf) {
        keyToChunk.values.forEach {
            it.leaves.remove(leaf)
        }
        addLeaf(leaf)
    }

    fun addStem(stem: Stem) {
        val xChunk = TerrainChunk.worldPosToChunkPos(stem.worldPosition.x)
        val yChunk = TerrainChunk.worldPosToChunkPos(stem.worldPosition.y)
        for (x in -1..1) for (y in -1..1)
            keyToChunk[TerrainChunk.chunkPosToKey(xChunk + x, yChunk + y)]?.stems?.plusAssign(stem)
    }

    fun monitorStem(stem: Stem) {
        keyToChunk.values.forEach {
            it.stems.remove(stem)
        }
        addStem(stem)
    }

    fun loadUnloadChunks() {
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
                        keyToChunk[key] = TerrainChunk(x, y, generator, world)
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

    fun takeWater(amount: Float, startPos: Vector2, endPos: Vector2) : Float {
        // build list of tiles
        tempCellsWithWater.clear()
        tempPos.set(startPos)
        tempStep.set(endPos).sub(startPos)
        val length = tempStep.len()
        tempStep.nor().scl(TerrainCell.SIZE / iterationsPerCell)
        val iterations = ((length/ TerrainCell.SIZE) * iterationsPerCell).toInt()

        val firstCell = getCellFromWorldPos(tempPos)
        if (firstCell != null) {
            tempCellsWithWater += firstCell
        }
        for (i in 0 until iterations) {
            tempPos.add(tempStep)
            val cell = getCellFromWorldPos(tempPos)
            if (cell != null && !tempCellsWithWater.contains(cell)) tempCellsWithWater += cell
        }
        var waterAvailable = 0f
        tempCellsWithWater.forEach {
            waterAvailable += it.water
        }

        val percentNeeded = amount / waterAvailable

        return if (percentNeeded > 1) {
            tempCellsWithWater.forEach { it.takePercentageOfWater(1f) }
            waterAvailable
        } else {
            tempCellsWithWater.forEach { it.takePercentageOfWater(percentNeeded) }
            amount
        }
    }
}