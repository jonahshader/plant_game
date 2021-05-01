package jonahklayton.systems.world

import jonahklayton.PlantGame
import jonahklayton.systems.plant.Node
import jonahklayton.systems.plant.Plant
import space.earlygrey.shapedrawer.ShapeDrawer

class World(level: Level) {
    private val playerPlant = Plant(level.playerPos)
    private val enemyPlant = Plant(level.enemyPos)


    // TODO: isUnderground(x, y) method

    fun getAllNodes() : MutableList<Node> {
        val nodes = mutableListOf<Node>()
        nodes += playerPlant.nodes
        nodes += enemyPlant.nodes
        return nodes
    }

    fun update(dt: Float) {
        playerPlant.update(dt)
        enemyPlant.update(dt)
    }

    fun draw(renderer: ShapeDrawer) {
        playerPlant.draw(renderer)
        enemyPlant.draw(renderer)
    }
}