package jonahklayton.systems.noise

import java.util.*

open class OctaveSet(var ran: Random) {
    private val octaves: ArrayList<Octave> = ArrayList()
    private var totalMagnitude = 0.0
    fun addOctave(spacialScalar: Double, magnitudeScalar: Double) {
        octaves.add(Octave(ran.nextLong(), spacialScalar, magnitudeScalar))
        totalMagnitude += magnitudeScalar
    }

    fun addOctaveFractal(
        spacialScalar: Double, magnitudeScalar: Double,
        spacialCompound: Double, magnitudeCompound: Double,
        octaves: Int
    ) {
        for (i in 0 until octaves) {
            addOctave(
                spacialScalar * Math.pow(spacialCompound, i.toDouble()),
                magnitudeScalar * Math.pow(magnitudeCompound, i.toDouble())
            )
        }
    }

    open fun getValue(x: Float, y: Float): Double {
        var `val` = 0.0
        for (octave in octaves) {
            `val` += octave.getValue(x, y)
        }
        return `val` / totalMagnitude
    }

}