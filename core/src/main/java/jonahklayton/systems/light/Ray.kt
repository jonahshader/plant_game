package jonahklayton.systems.light

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import space.earlygrey.shapedrawer.ShapeDrawer

class Ray(private val pos: Vector2, angle: Vector2, var energy: Float) {
    private val length = 4f

    private val endPos = Vector2()
    private val lengthVector = Vector2(angle)

    init {
        lengthVector.scl(length)
        endPos.add(pos).add(lengthVector)
    }

    fun update() {
        pos.add(lengthVector)
        endPos.add(lengthVector)
    }

    fun draw(shapeDrawer: ShapeDrawer) {
        shapeDrawer.setColor(Color.YELLOW)
        shapeDrawer.line(pos, endPos)
    }
}