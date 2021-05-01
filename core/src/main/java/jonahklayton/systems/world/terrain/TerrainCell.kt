package jonahklayton.systems.world.terrain

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.assets.Assets
import kotlin.math.max

class TerrainCell(type: TerrainType) {
    companion object {
        const val SIZE = 16f
    }
    enum class TerrainType {
        DIRT,
        GRASS,
        STONE
    }

    private val waterTint = Color(.3f, .4f, 1f, 1f)

    private var textureRegion: TextureRegion = when (type) {
        TerrainType.DIRT -> Assets.getSprites().findRegion("white_pixel")
        TerrainType.GRASS -> Assets.getSprites().findRegion("white_pixel")
        TerrainType.STONE -> Assets.getSprites().findRegion("white_pixel")
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

    fun mix(otherCell: TerrainCell) {
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
        water += dt * waterGenPerSecond
        water = water.coerceIn(0f, maxWater)
    }

    fun draw(batch: SpriteBatch, pos: Vector2) {
        val waterTintness = water / maxWater
        batch.color.set(tint.r, tint.g, tint.b, tint.a)
        batch.color.lerp(waterTint, waterTintness)

        batch.draw(textureRegion, pos.x, pos.y, SIZE, SIZE)
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