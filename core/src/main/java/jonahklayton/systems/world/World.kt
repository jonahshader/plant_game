package jonahklayton.systems.world

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils.PI
import com.badlogic.gdx.math.MathUtils.sin
import jonahklayton.screens.GameOverScreen
import jonahklayton.screens.WinScreen
import jonahklayton.systems.light.Light
import jonahklayton.systems.plant.*
import jonahklayton.systems.rain.Rain
import jonahklayton.systems.screen.ScreenManager
import jonahklayton.systems.ui.Hud
import jonahklayton.systems.world.terrain.Terrain
import space.earlygrey.shapedrawer.ShapeDrawer

class World(private val level: Level, inputMultiplexer: InputMultiplexer, camera: Camera) {
    val terrain = Terrain(this, level.generator)
    private val playerPlant = PlayerPlant(level.playerPos, 100F, this, camera)
    private val enemyPlant = EnemyPlant(level.enemyPos, 150F, this, level.levelNumber)

    private val light = Light(this)
    private val rain = Rain(this, level.weather)

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
    fun getAllStems(): MutableList<Stem> {
        val stems = mutableListOf<Stem>()
        stems += playerPlant.stems
        stems += enemyPlant.stems
        return stems
    }

    fun update(dt: Float) {
        terrain.update(dt)
        playerPlant.update(dt)
        enemyPlant.update(dt)
        light.update(dt)
        rain.update(dt)

        if (playerPlant.isDead()) {
            ScreenManager.pop()
            ScreenManager.push(GameOverScreen(level.levelNumber))
        } else if (enemyPlant.isDead()) {
            ScreenManager.pop()
            ScreenManager.push(WinScreen(level.levelNumber))
        }

        time += dt
    }

    fun draw(renderer: ShapeDrawer) {
//        playerPlant.drawShadows(renderer, getSkyBrightness())
//        enemyPlant.drawShadows(renderer, getSkyBrightness())
        terrain.draw(getSkyBrightness())
        playerPlant.draw(renderer, getSkyBrightness())
        enemyPlant.draw(renderer, getSkyBrightness())
        light.draw(renderer)
        rain.draw(getSkyBrightness())
    }

    fun getDay() : Int = (time / dayLength).toInt()
    fun getDayProgress() : Float = (time / dayLength).toFloat() - getDay()
    fun getIsMorning() : Boolean = getDayProgress() < 0.25F
    fun getSkyBrightness() : Float = sin(getDayProgress() * 2* PI) * .4f + .6f

}