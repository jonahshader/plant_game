package jonahklayton.systems.ui.menu

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ScalingViewport
import jonahklayton.systems.ui.TextRenderer
import space.earlygrey.shapedrawer.ShapeDrawer

class Menu(private val font: TextRenderer.Font, private val camera: Camera, private val position: Vector2, private val menuItemSize: Vector2) {
    private val items = ArrayList<MenuItem>()
    fun addMenuItem(label: String, action: () -> Unit) {
        // TODO: add menu item to items arraylist
        val newItem: MenuItem = if (items.size == 0) {
            //Call first menu item constructor
            MenuItem(action, -menuItemSize.x/2f, 0f, menuItemSize.x, menuItemSize.y, label, font, camera)
        } else {
            //Call other items constructor
            MenuItem(action, label, items[items.size - 1], font, camera)
        }
        items.add(newItem)
    }

    fun draw(batch: SpriteBatch, shapeDrawer: ShapeDrawer, viewport: ScalingViewport) {
        for (item in items) item.draw(batch, shapeDrawer, viewport, position)
    }

    fun run(dt: Float) {
        for (item in items) item.run(position, dt)
    }
}