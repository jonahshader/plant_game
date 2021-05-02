package jonahklayton.systems.world.terrain

import com.badlogic.gdx.math.RandomXS128
import jonahklayton.systems.noise.TwistedOctaveSet

/**
 * must add octaves to octaveSet and
 */
class TerrainGenerator(seed: Long) {
    private val rand = RandomXS128(seed)

    val octaveSet = TwistedOctaveSet(rand, 0.1f)
    val gradient = 0.01

    fun makeCellAt(xCell: Int, yCell: Int, terrain: Terrain) : TerrainCell? {
        val cellType = mapValueToTile(cellPosToValue(xCell, yCell))
        return if (cellType != null) {
            TerrainCell(xCell, yCell, cellType, rand, terrain)
        } else null
    }

    private fun cellPosToValue(xCell: Int, yCell: Int) : Float {
        val unmodded = (octaveSet.getValue(xCell * TerrainCell.SIZE, yCell * TerrainCell.SIZE) + yCell * TerrainCell.SIZE * gradient).toFloat()
        return unmodded + (rand.nextFloat() - 0.5f) * unmodded
    }

    private fun mapValueToTile(value: Float) : TerrainCell.TerrainType? {
         return when {
             value < -1.5 -> TerrainCell.TerrainType.STONE
             value < -.2 -> TerrainCell.TerrainType.DIRT
             value < 0 -> TerrainCell.TerrainType.GRASS
             else -> null
         }
    }
}