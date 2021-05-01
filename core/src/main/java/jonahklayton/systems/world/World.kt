package jonahklayton.systems.world

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import jonahklayton.PlantGame
import jonahklayton.systems.plant.Node
import jonahklayton.systems.plant.Plant
import jonahklayton.systems.plant.PlayerPlant
import jonahklayton.systems.world.terrain.Terrain
import jonahklayton.systems.world.terrain.TerrainChunk
import space.earlygrey.shapedrawer.ShapeDrawer

class World(level: Level, inputMultiplexer: InputMultiplexer, camera: Camera) {
    private val playerPlant = PlayerPlant(level.playerPos, 100F, this, camera)
    private val enemyPlant = Plant(level.enemyPos, 100F, this)
    private val terrain = Terrain(this, level.generator)

    init {
        // register player plant as an input processor
        inputMultiplexer.addProcessor(playerPlant)
    }

    fun getAllNodes() : MutableList<Node> {
        val nodes = mutableListOf<Node>()
        nodes += playerPlant.nodes
        nodes += enemyPlant.nodes
        return nodes
    }

    fun update(dt: Float) {
        terrain.update(dt)
        playerPlant.update(dt)
        enemyPlant.update(dt)
    }

    fun draw(renderer: ShapeDrawer) {
        terrain.draw(PlantGame.batch)
        playerPlant.draw(renderer)
        enemyPlant.draw(renderer)
    }
}