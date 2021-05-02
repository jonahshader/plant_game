package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.terrain.Terrain
import space.earlygrey.shapedrawer.ShapeDrawer
import java.lang.Double.min
import kotlin.math.min

class Root(relativeTargetPosition: Vector2, parent: Node?, plant: Plant, storedEnergy: Float, private val terrain: Terrain): Node(relativeTargetPosition, parent, plant) {
    private val storagePerThicknessSq = 100F
    private val waterAvailabilityPerThickness = 10F
    private val THICKNESS_COST = 10F
    private val energyPerLengthScalar = 1/50f

    var storedEnergy = storedEnergy
        private set

    var waterLeftInTick = 0F

    fun getWater(quantity: Float): Float{
        val amount = min(quantity, waterLeftInTick)
        return if (parent != null && amount > 0) {
            val successfulAmount = terrain.takeWater(amount, parent!!.worldPosition, worldPosition)
            waterLeftInTick -= successfulAmount
            successfulAmount
        } else 0f
    }

    override fun draw(renderer: ShapeDrawer, brightness: Float){
        renderer.setColor(0.5f * brightness, 0.4f * brightness, 0f * brightness, 1f)
        super.draw(renderer, brightness)
    }

    override fun update(timePassed: Float){
        waterLeftInTick = getAbsorptionRate()*timePassed
        super.update(timePassed)
    }

    fun thicken(){
        if(plant.requestEnergy(THICKNESS_COST) >= THICKNESS_COST) thickness++
    }

    fun getMaxStoredEnergy(): Float{
        return thickness*thickness*storagePerThicknessSq*getLength()*energyPerLengthScalar
    }

    fun getAbsorptionRate(): Float{
        return thickness*waterAvailabilityPerThickness
    }

    fun storeEnergy(energy: Float): Float{
        var space = getMaxStoredEnergy() - storedEnergy
        if(energy > space){
            storedEnergy = getMaxStoredEnergy()
            return energy - space
        }else storedEnergy += energy

        return 0F
    }

    fun pullEnergy(energyRequested: Float): Float{
        if(storedEnergy >= energyRequested){
            storedEnergy -= energyRequested
            return  energyRequested
        }else{
            var energyToSend = storedEnergy
            storedEnergy = 0F
            return return energyToSend
        }
    }

}