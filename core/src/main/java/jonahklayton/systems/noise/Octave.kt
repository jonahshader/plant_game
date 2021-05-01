package jonahklayton.systems.noise

import external.OpenSimplex2F

class Octave(seed: Long, private val spacialScalar: Double, private val magnitudeScalar: Double) {
    private val noise: OpenSimplex2F = OpenSimplex2F(seed)
    fun getValue(x: Float, y: Float): Double = noise.noise2(x * spacialScalar, y * spacialScalar) * magnitudeScalar
}