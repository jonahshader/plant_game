package jonahklayton.systems.ui.menu

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahklayton.systems.assets.Assets
import jonahklayton.systems.assets.Assets.MENU_MOUSE_OVER_SOUND
import jonahklayton.systems.assets.Assets.MENU_OPEN_SOUND
import jonahklayton.systems.sound.SoundSystem
import jonahklayton.systems.ui.CustomShapes
import jonahklayton.systems.ui.TextRenderer
import space.earlygrey.shapedrawer.ShapeDrawer
import kotlin.math.pow

open class MenuItem {
    private val MENU_PADDING = 25
    internal var action: () -> Unit
    internal var x: Float
    internal var y: Float
    internal var width: Float
    internal var height: Float
    internal val MOUSE_OVER_INDENT = 5
    internal var label: String
    internal var font: TextRenderer.Font
    private var camera: Camera
    internal var mouseOver = false
    internal val progressPerSecond = 15
    internal var progress = 0f

    // constructor for first menu item
    constructor(
        action: () -> Unit,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        label: String,
        font: TextRenderer.Font,
        camera: Camera
    ) {
        this.action = action
        this.x = x - width / 2
        this.y = y + height / 2
        this.width = width
        this.height = height
        this.label = label
        this.font = font
        this.camera = camera
    }

    // constructor for other items.
    constructor(action: () -> Unit, label: String, previousMenuItem: MenuItem, font: TextRenderer.Font, camera: Camera) {
        this.action = action
        this.label = label
        this.font = font
        this.camera = camera
        x = previousMenuItem.x
        y = previousMenuItem.y - (previousMenuItem.height + MENU_PADDING)
        width = previousMenuItem.width
        height = previousMenuItem.height
    }

    open fun run(offset: Vector2, dt: Float) {
        val xo = offset.x + x
        val yo = offset.y + y
        val m = mouseWorld
        if (m.x >= xo && m.y >= yo && m.x <= xo + width && m.y <= yo + height) {
            if (Gdx.input.justTouched()) {
                SoundSystem.playSoundStandalone(Assets.manager.get(MENU_OPEN_SOUND, Sound::class.java), .8f, 0f)
                action()
            }
            if (!mouseOver) {
                mouseOver = true
                // play mouseOver sound
                SoundSystem.playSoundStandalone(Assets.manager.get(MENU_MOUSE_OVER_SOUND, Sound::class.java), .8f, 0f)
            }

            progress += dt * progressPerSecond
        } else {
            mouseOver = false
            progress -= dt * progressPerSecond
        }
        progress = progress.coerceIn(0f, 1f)
    }

    open fun draw(batch: SpriteBatch, shapeDrawer: ShapeDrawer, viewport: ScalingViewport, offset: Vector2) {
        val xo = offset.x + x
        val yo = offset.y + y
//        val m = mouseWorld
        shapeDrawer.setColor(0.0f, 0.0f, 0.0f, .5f)
        CustomShapes.filledRoundedRect(shapeDrawer, xo, yo, width, height, 6f)

        val progressMapped = progress.pow(1/2f)

//        if (m.x >= xo && m.y >= yo && m.x <= xo + width && m.y <= yo + height) {
        shapeDrawer.setColor(0.8f, 0.8f, 0.8f, 1f)
        CustomShapes.filledRoundedRect(shapeDrawer, xo + MOUSE_OVER_INDENT * progressMapped, yo + MOUSE_OVER_INDENT * progressMapped, width, height, 6f)
        TextRenderer.begin(batch, viewport, font, height * .75f, 0.05f)
        TextRenderer.color = Color.WHITE
        TextRenderer.drawTextCentered(xo + (width/2f) + MOUSE_OVER_INDENT * progressMapped, yo + (height/2f) + MOUSE_OVER_INDENT * progressMapped, label, height * (0.04f + progressMapped/32f), .75f)
        TextRenderer.end()
//        } else {
//            shapeDrawer.setColor(0.8f, 0.8f, 0.8f, 1f)
//            shapeDrawer.filledRectangle(xo, yo, width, height)
//            TextRenderer.begin(batch, viewport, font, height * .75f, 0.00f)
//            TextRenderer.color = Color.WHITE
//            TextRenderer.drawTextCentered(xo + width/2f, yo + height/2f, label, height/48f)
//            TextRenderer.end()
//        }
    }

    internal val mouseWorld: Vector2
        internal get() {
            val mouseWorld = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            return Vector2(mouseWorld.x, mouseWorld.y)
        }
}