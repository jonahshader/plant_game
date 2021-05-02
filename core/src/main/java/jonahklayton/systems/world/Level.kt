package jonahklayton.systems.world

import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.noise.OctaveSet
import jonahklayton.systems.world.terrain.TerrainGenerator

data class Level(val playerPos: Float, val enemyPos: Float, val generator: TerrainGenerator, val weather: OctaveSet, val levelNumber: Int)