package jonahklayton.systems.plant

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import space.earlygrey.shapedrawer.ShapeDrawer

class Root(relativeTargetPosition: Vector2, parent: Node?, plant: Plant, storedEnergy: Float): Node(relativeTargetPosition, parent, plant) {
    private val storagePerThicknessSq = 100F
    private val waterAvailabilityPerThickness = 10F

    var thickness = 1
    var storedEnergy = storedEnergy
        private set

    var waterAbsorbed = 0F

    fun getWater(quantity: Float): Float{
        //TODO: tell terrain water taken (limited by thickness) and return the amount we got (don't forget to add to water absorbed)
        return 0F
    }

    override fun update(timePassed: Float){
        waterAbsorbed -= getMaxWaterAvailable()*timePassed
    }

    fun thicken(){
        thickness++
    }

    fun getMaxStoredEnergy(): Float{
        return thickness*thickness*storagePerThicknessSq
    }

    fun getMaxWaterAvailable(): Float{
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