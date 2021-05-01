package jonahklayton.systems.world.terrain

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.RandomXS128
import jonahklayton.PlantGame
import jonahklayton.systems.assets.Assets

class TerrainCell(private val xCell: Int, private val yCell: Int, type: TerrainType, private val rand: RandomXS128, private val terrain: Terrain) {
    companion object {
        const val SIZE = 4f
    }

    enum class TerrainType {
        DIRT,
        GRASS,
        STONE
    }

    private val waterTint = Color(.3f, .4f, 1f, 1f)
    private val currentColor = Color(1f, 1f, 1f, 1f)


//    private var sprite: Sprite = when (type) {
//        TerrainType.DIRT -> Sprite(Assets.getSprites().findRegion("white_pixel"))
//        TerrainType.GRASS -> Sprite(Assets.getSprites().findRegion("white_pixel"))
//        TerrainType.STONE -> Sprite(Assets.getSprites().findRegion("white_pixel"))
//    }

    private val porousness: Float = when (type) {
        TerrainType.DIRT -> .5f
        TerrainType.GRASS -> 1f
        TerrainType.STONE -> .033f
    }

    private val tint: Color = when (type) {
        TerrainType.DIRT -> Color(.7f, .4f, .2f, 1f)
        TerrainType.GRASS -> Color(.5f, .9f, .2f, 1f)
        TerrainType.STONE -> GRAY
    }

    private val maxWater: Float = when (type) {
        TerrainType.DIRT -> 1.0f
        TerrainType.GRASS -> 1.5f
        TerrainType.STONE -> 0.25f
    }

    private val waterGenPerSecond: Float = when (type) {
        TerrainType.DIRT -> 0.01f
        TerrainType.GRASS -> 0f
        TerrainType.STONE -> 0f
    }

    private val mixTimerInterval: Float = when (type) {
        TerrainType.DIRT -> 0.8f
        TerrainType.GRASS -> 0.2f
        TerrainType.STONE -> 1f
    }

    private var water = 0f
    private var mixTimer = 0f

    init {
//        sprite.setSize(SIZE, SIZE)
//        sprite.setPosition(SIZE * xCell, SIZE * yCell)
        resetTimer()
    }

    private fun mix(otherCell: TerrainCell) {
        val avgPorusness = (porousness + otherCell.porousness)/2f
        val thisNextWater = (1-(avgPorusness * .5f)) * water + (avgPorusness * .5f) * otherCell.water
        val otherNextWater = (1-(avgPorusness * .5f)) * otherCell.water + (avgPorusness * .5f) * water

        water = thisNextWater
        otherCell.water = otherNextWater

        if (water > maxWater) {
            otherCell.water += water - maxWater
            water = maxWater
        }
        if (otherCell.water > otherCell.maxWater) {
            water += otherCell.water - otherCell.maxWater
            otherCell.water = otherCell.maxWater
        }
    }

    fun update(dt: Float) {
        // try mix
        if (mixTimer <= 0) {
            var xMix = xCell
            var yMix = yCell
            when (rand.nextInt(4)) {
                0->xMix--
                1->xMix++
                2->yMix--
                3->yMix++
            }
            val otherCell = terrain.getCell(xMix, yMix)
            if (otherCell != null) mix(otherCell)
            resetTimer()
        }
        mixTimer -= dt

        water += dt * waterGenPerSecond
        water = water.coerceIn(0f, maxWater)
    }

    fun draw() {
        val waterTintness = water / maxWater.coerceAtLeast(1f)
//        sprite.color.set(tint.r, tint.g, tint.b, tint.a)
//        sprite.color.lerp(waterTint, waterTintness)
//        sprite.draw(batch)
        currentColor.set(tint.r, tint.g, tint.b, tint.a)
        currentColor.lerp(waterTint, waterTintness)
        PlantGame.shapeDrawer.setColor(currentColor)
        PlantGame.shapeDrawer.filledRectangle(xCell * SIZE, yCell * SIZE, SIZE, SIZE)
    }

    fun takeWater(requestedAmount: Float) : Float {
        return if (requestedAmount < water) {
            water -= requestedAmount
            requestedAmount
        } else {
            val amount = water
            water = 0f
            amount
        }
    }

    fun putWater(requestedAmount: Float) {
        // TODO: make this less dumb
        water += requestedAmount
    }

    private fun resetTimer() {
        mixTimer += rand.nextFloat() * mixTimerInterval
    }

}