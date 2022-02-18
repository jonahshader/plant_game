package jonahklayton.systems.plant

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.assets.Assets
import jonahklayton.systems.assets.Assets.DONE_GROWING_SOUND
import jonahklayton.systems.assets.Assets.LIMB_GROW_SOUND
import jonahklayton.systems.sound.SoundSystem
import space.earlygrey.shapedrawer.ShapeDrawer
import java.lang.Math.abs
import kotlin.math.pow

open class Node(relativeTargetPosition: Vector2, parent: Node?, var plant: Plant){
    var relativePosition = Vector2(relativeTargetPosition).nor().scl(0.0001F) //Not zero so the node doesn't immediately die.
        private set

    private var relativeTargetPosition = relativeTargetPosition.cpy()

    var targetLength = relativeTargetPosition.len()

    var worldPosition: Vector2 = Vector2(relativePosition).add(parent?.worldPosition ?: plant.worldPosition)
        private set

    var isDead = false
        private set

    var parent: Node? = parent
        private set

    var children = ArrayList<Node>()
        private set

    var thickness = 3F

    private var soundPlayed = false
    private val doneGrowingSound: Sound = Assets.manager.get(DONE_GROWING_SOUND)
    private val growSound: Sound = Assets.manager.get(LIMB_GROW_SOUND)
    private val growSoundId = SoundSystem.playSoundInWorld(growSound, worldPosition, .5f, .5f)

    var color = Color(1f, 1f, 1f, 1f)

    // used in shadow rendering
    private val offsetStartPos = Vector2()
    private val offsetEndPos = Vector2()

    init {
        growSound.setLooping(growSoundId, true)
    }

    open fun update(timePassed: Float){
        if ((!soundPlayed && isFullyGrown()) || isDead) {
            soundPlayed = true
            SoundSystem.playSoundInWorld(doneGrowingSound, worldPosition, .8f, 2-(1.5f*targetLength/PlayerPlant.MAX_SIZE))
            growSound.stop()
        } else {
            growSound.setPitch(growSoundId, .5f + (getLength() / targetLength) * 2/1.5f)
        }
    }

    open fun draw(renderer: ShapeDrawer, brightness: Float, hue: Float){
        if (parent != null) {
            val hsv = FloatArray(3)
            color.toHsv(hsv)
            hsv[1] += hue
            hsv[0] += hue
            color = color.fromHsv(hsv)
            renderer.setColor(color)
            renderer.line(worldPosition, parent!!.worldPosition, thickness)
        }
    }

    open fun drawShadow(renderer: ShapeDrawer, brightness: Float, offset: Vector2) {
        if (parent != null) {
            renderer.setColor(0f, 0f, 0f, brightness.pow(2f))
            offsetStartPos.set(worldPosition).add(offset)
            offsetEndPos.set(parent!!.worldPosition).add(offset)
            renderer.line(offsetStartPos, offsetEndPos, thickness)
        }
    }

    fun addChild(child: Node){
        children.add(child)
    }

    private fun removeChild(child: Node){
        children.remove(child)
    }

    fun die(){
        parent?.removeChild(this)

        isDead = true

        for(i in children) i.die()
    }

    fun updateWorldPosition(){
        worldPosition = Vector2(relativePosition).add(parent?.worldPosition ?: plant.worldPosition)

        for(i in children){
            i.updateWorldPosition();
        }
    }

    open fun grow(percent: Float){
        var oldPos = Vector2(relativePosition.x, relativePosition.y)

        relativePosition = Vector2(relativePosition).add(Vector2(relativeTargetPosition).scl(percent/100))

        if (abs(relativePosition.angleDeg() - oldPos.angleDeg()) > 10) die()
    }

    fun isFullyGrown(): Boolean {
        return relativePosition.len2() >= relativeTargetPosition.len2()
    }

    fun getLength(): Float {
        return relativePosition.len()
    }

}