package jonahklayton.systems.noise

import java.util.*

class TwistedOctaveSet(ran: Random, private val twistAmount: Float) : OctaveSet(ran) {
    private val octaveXShift = ArrayList<Octave>()
    private val octaveYShift = ArrayList<Octave>()
    private var twistMagnitude = 0f


    fun addTwisterOctave(spacialScalar: Double, magnitudeScalar: Double) {
        octaveXShift.add(Octave(ran.nextLong(), spacialScalar, magnitudeScalar))
        octaveYShift.add(Octave(ran.nextLong(), spacialScalar, magnitudeScalar))
        twistMagnitude += magnitudeScalar.toFloat()
    }

    fun addTwisterOctaveFractal(
        spacialScalar: Double, magnitudeScalar: Double,
        spacialCompound: Double, magnitudeCompound: Double,
        octaves: Int
    ) {
        for (i in 0 until octaves) {
            addTwisterOctave(
                spacialScalar * Math.pow(spacialCompound, i.toDouble()),
                magnitudeScalar * Math.pow(magnitudeCompound, i.toDouble())
            )
        }
    }

    override fun getValue(x: Float, y: Float): Double {
        var xTwisted = 0f
        for (o in octaveXShift) {
            xTwisted += o.getValue(x, y).toFloat()
        }
        xTwisted /= twistMagnitude
        xTwisted *= twistAmount
        var yTwisted = 0f
        for (o in octaveYShift) {
            yTwisted += o.getValue(x, y).toFloat()
        }
        yTwisted /= twistMagnitude
        yTwisted *= twistAmount
        return super.getValue(x + xTwisted, y + yTwisted)
    }
}