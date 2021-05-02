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
import jonahklayton.systems.tutorial.Tutorial
import jonahklayton.systems.ui.Hud
import jonahklayton.systems.world.terrain.Terrain
import ktx.graphics.center
import space.earlygrey.shapedrawer.ShapeDrawer

class World(private val level: Level, inputMultiplexer: InputMultiplexer, camera: Camera, menu: Boolean = false) {
    val terrain = Terrain(this, level.generator)
    private val playerPlant = PlayerPlant(level.playerPos, 100F + if(level.levelNumber==0) 900f else 0f, this, camera)
    private val enemyPlant = EnemyPlant(level.enemyPos, 150F + (level.levelNumber-1) * 20, this, level.levelNumber)

    private val light = Light(this)
    private val rain = Rain(this, level.weather)

    private val dayLength = 60f

    private val menu = menu
    private val camera = camera

    var time = 0.0
        private set

    init {
        // register player plant as an input processor
        if(!menu) {
            inputMultiplexer.addProcessor(playerPlant)

            Hud.plant = playerPlant

            playerPlant.placePlant()

            playerPlant.centerCamera()
        }
        enemyPlant.placePlant()

        if(menu){
            enemyPlant.branchChance = 0.1f
            camera.position.set(enemyPlant.worldPosition.x-70, enemyPlant.worldPosition.y+50, 0f)
            camera.update()
        }
    }

    fun getAllNodes() : MutableList<Node> {
        val nodes = mutableListOf<Node>()
        if(!menu) nodes += playerPlant.nodes
        nodes += enemyPlant.nodes
        return nodes
    }

    fun getAllLeaves() : MutableList<Leaf> {
        val leaves = mutableListOf<Leaf>()
        if(!menu) leaves += playerPlant.leaves
        leaves += enemyPlant.leaves
        return leaves
    }
    fun getAllStems(): MutableList<Stem> {
        val stems = mutableListOf<Stem>()
        if(!menu) stems += playerPlant.stems
        stems += enemyPlant.stems
        return stems
    }

    fun update(dt: Float) {
        terrain.update(dt)
        if(!menu) playerPlant.update(dt)
        enemyPlant.update(dt)
        light.update(dt)
        rain.update(dt)


        if(!menu) {
            if (playerPlant.isDead()) {
                ScreenManager.switchTo(GameOverScreen(level.levelNumber))
            } else if (enemyPlant.isDead()) {
                ScreenManager.switchTo(WinScreen(level.levelNumber))
            }
        }

        time += dt
    }

    fun draw(renderer: ShapeDrawer) {
//        playerPlant.drawShadows(renderer, getSkyBrightness())
//        enemyPlant.drawShadows(renderer, getSkyBrightness())
        terrain.draw(getSkyBrightness())
        enemyPlant.draw(renderer, getSkyBrightness())
        if(!menu) playerPlant.draw(renderer, getSkyBrightness())
        light.draw(renderer)
        rain.draw(getSkyBrightness())
    }

    fun getDay() : Int = (time / dayLength).toInt()
    fun getDayProgress() : Float = (time / dayLength).toFloat() - getDay()
    fun getIsMorning() : Boolean = getDayProgress() < 0.25F
    fun getSkyBrightness() : Float = sin(Light.dayLightRadians(this)) * .4f + .6f

}