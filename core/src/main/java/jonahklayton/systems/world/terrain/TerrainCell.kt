package jonahklayton.systems.world.terrain

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.RandomXS128
import jonahklayton.systems.assets.Assets

class TerrainCell(private val xCell: Int, private val yCell: Int, type: TerrainType, private val rand: RandomXS128, private val terrain: Terrain) {
    companion object {
        const val SIZE = 16f
        const val MIX_CHANCE = .25f
    }

    enum class TerrainType {
        DIRT,
        GRASS,
        STONE
    }

    private val waterTint = Color(.3f, .4f, 1f, 1f)

    private var sprite: Sprite = when (type) {
        TerrainType.DIRT -> Sprite(Assets.getSprites().findRegion("white_pixel"))
        TerrainType.GRASS -> Sprite(Assets.getSprites().findRegion("white_pixel"))
        TerrainType.STONE -> Sprite(Assets.getSprites().findRegion("white_pixel"))
    }

    private val porousness: Float = when (type) {
        TerrainType.DIRT -> .5f
        TerrainType.GRASS -> 1f
        TerrainType.STONE -> .05f
    }

    private val tint: Color = when (type) {
        TerrainType.DIRT -> BROWN
        TerrainType.GRASS -> GREEN
        TerrainType.STONE -> GRAY
    }

    private val maxWater: Float = when (type) {
        TerrainType.DIRT -> 1.0f
        TerrainType.GRASS -> 1.5f
        TerrainType.STONE -> 0.1f
    }

    private val waterGenPerSecond: Float = when (type) {
        TerrainType.DIRT -> 0.005f
        TerrainType.GRASS -> 0f
        TerrainType.STONE -> 0f
    }

    private var water = 0f

    init {
        sprite.setSize(SIZE, SIZE)
        sprite.setPosition(SIZE * xCell, SIZE * yCell)
    }

    private fun mix(otherCell: TerrainCell) {
        val avgPorusness = (porousness + otherCell.porousness)/2f
        val thisNextWater = ((1-avgPorusness) * .5f) * water + (avgPorusness * .5f) * otherCell.water
        val otherNextWater = ((1-avgPorusness) * .5f) * otherCell.water + (avgPorusness * .5f) * water

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
        if (rand.nextFloat() < MIX_CHANCE) {
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
        }

        water += dt * waterGenPerSecond
        water = water.coerceIn(0f, maxWater)
    }

    fun draw(batch: SpriteBatch) {
        val waterTintness = water / maxWater
        sprite.color.set(tint.r, tint.g, tint.b, tint.a)
//        sprite.color.lerp(waterTint, waterTintness)
        sprite.draw(batch)
//        batch.draw(textureRegion, xCell * SIZE, yCell * SIZE, SIZE, SIZE)
//        println("drawin at $xCell $yCell")
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

}