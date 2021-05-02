package jonahklayton.systems.world

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import jonahklayton.PlantGame
import jonahklayton.systems.light.Light
import jonahklayton.systems.plant.Leaf
import jonahklayton.systems.plant.Node
import jonahklayton.systems.plant.Plant
import jonahklayton.systems.plant.PlayerPlant
import jonahklayton.systems.ui.Hud
import jonahklayton.systems.world.terrain.Terrain
import space.earlygrey.shapedrawer.ShapeDrawer

class World(level: Level, inputMultiplexer: InputMultiplexer, camera: Camera) {
    val terrain = Terrain(this, level.generator)
    private val playerPlant = PlayerPlant(level.playerPos, 100F, this, camera)
    private val enemyPlant = Plant(level.enemyPos, 100F, this)

    private val light = Light(this)

    private val dayLength = 60f

    var time = 0.0
        private set

    init {
        // register player plant as an input processor
        inputMultiplexer.addProcessor(playerPlant)

        Hud.plant = playerPlant
    }

    fun getAllNodes() : MutableList<Node> {
        val nodes = mutableListOf<Node>()
        nodes += playerPlant.nodes
        nodes += enemyPlant.nodes
        return nodes
    }

    fun getAllLeaves() : MutableList<Leaf> {
        val leaves = mutableListOf<Leaf>()
        leaves += playerPlant.leaves
        leaves += enemyPlant.leaves
        return leaves
    }

    fun update(dt: Float) {
        terrain.update(dt)
        playerPlant.update(dt)
        enemyPlant.update(dt)
        light.update(dt)

        time += dt
    }

    fun draw(renderer: ShapeDrawer) {
        terrain.draw(PlantGame.batch)
        playerPlant.draw(renderer)
        enemyPlant.draw(renderer)
        light.draw(renderer)
    }

    fun getDay() : Int = (time / dayLength).toInt()
    fun getDayProgress() : Float = (time / dayLength).toFloat() - getDay()
}