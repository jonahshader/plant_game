package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.light.Light
import jonahklayton.systems.world.World
import jonahklayton.systems.world.terrain.TerrainCell
import space.earlygrey.shapedrawer.ShapeDrawer
import kotlin.math.PI


// growth/existence cost is taken care of here not in the nodes themselves
open class Plant(xPosition: Float, startingEnergy: Float, world: World, private val hue: Float){
    private val ENERGY_PER_LENGTH_SUSTAINED = 0.05F
    private val ENERGY_PER_LENGTH_GROWN = 1F
    private val ENERGY_PER_LENGTH_KILLED = 0.9F
    private val STORE_RATIO_FOR_GROW = 0.05F
    private val WATER_PER_LIGHT = .1f

    var worldPosition = Vector2(xPosition, 100*TerrainCell.SIZE-1f)
        private set

    var world = world
        private set

    var root = Root(Vector2(0F, 0.01F), null, this, startingEnergy, world.terrain)
        private set

    var energy = 0F
        private set

    var nodes = mutableListOf<Node>()
        private set

    var roots = mutableListOf<Root>()
        private set

    var stems = mutableListOf<Stem>()
        private set

    var leaves = mutableListOf<Leaf>()
        private set

    private var bottleneck = "light"

    init {
        addRoot(root)

        var child = Root(Vector2(0F, -10F), root, this, 0F, world.terrain)
        root.addChild(child)

        addRoot(child)
    }

    fun placePlant(){
        while(!world.terrain.isUnderground(worldPosition)){
            worldPosition.sub(0f, TerrainCell.SIZE)
            world.terrain.loadUnloadChunks()
            root.updateWorldPosition()
        }


    }

    //call to get energy from somewhere in the plant, returns the amount of energy gotten
    fun requestEnergy(energyRequested: Float): Float{
        var energyNeeded = energyRequested-energy
        if(energyNeeded <= 0){
            energy -= energyRequested
            return energyRequested
        }else{
            energy = 0F

            //get energy from roots as we can
            for(i in roots){
                if(energyNeeded > 0){
                    energyNeeded -= i.pullEnergy(energyNeeded)
                } else break
            }
        }
        return energyRequested-energyNeeded
    }

    private fun getEnergyFromChildNodes(energyRequested: Float) {
        var energyNeeded = energyRequested

        var childNodes = nodes.filter { t -> t.children.isEmpty() }

        while(energyNeeded > 0) {
            val REMOVE_INCREMENT = 0.1F
            for (i in childNodes) {
                if(energyNeeded > 0) {
                    i.grow(-REMOVE_INCREMENT)
                    i.updateWorldPosition()
                    energyNeeded -= REMOVE_INCREMENT * i.targetLength * ENERGY_PER_LENGTH_KILLED
                }else break
            }
        }
    }

    fun getGrowingNodes() = nodes.filter {t->!t.isFullyGrown()}

    open fun update(timePassed: Float){

        //use energy to maintain plant
        for(i in nodes){
            var energyNeeded = i.getLength() * ENERGY_PER_LENGTH_SUSTAINED * timePassed
            energyNeeded -= requestEnergy(energyNeeded)
            if(energyNeeded >= 0) getEnergyFromChildNodes(energyNeeded)
        }

        //grow whatever we can grow

        var growEnergy = energy

        for(i in roots){
            growEnergy += i.pullEnergy(i.storedEnergy*STORE_RATIO_FOR_GROW*timePassed)
        }

        // get growingNodes
        var growingNodes = nodes.filter {t->!t.isFullyGrown()}

        var energyPerNode = (growEnergy/growingNodes.size)
        for(i in growingNodes){
            i.grow(((energyPerNode/ENERGY_PER_LENGTH_GROWN)/i.targetLength)*100F)
        }

        // update position of each root growing node and children
        for(i in growingNodes){
            if(!growingNodes.contains(i.parent)) i.updateWorldPosition()
        }

        if(energy > 0) {
            //store energy in roots
            for (i in roots) {
                if (energy > 0) {
                    energy -= i.storeEnergy(energy)
                } else break
            }
        }

        //discard leftover energy
        energy = 0F

        for(i in nodes){
            i.update(timePassed)
        }

        manageLists()
    }

    open fun draw(renderer: ShapeDrawer, brightness: Float){
        drawShadows(renderer, brightness)
        nodes.forEach {it.draw(renderer, brightness, hue)}
    }

    private fun drawShadows(renderer: ShapeDrawer, brightness: Float) {
        val shadowOffset = Vector2(1f, 0f)
        shadowOffset.rotateRad(Light.dayLightRadians(world)).scl(-1.5f)
        nodes.forEach {it.drawShadow(renderer, brightness, shadowOffset)}
    }

    fun addRoot(root: Root){
        roots.add(root)
        nodes.add(root)
    }

    fun addStem(stem: Stem){
        stems.add(stem)
        nodes.add(stem)
    }

    fun addLeaf(leaf: Leaf){
        leaves.add(leaf)
        nodes.add(leaf)
    }

    fun receiveLight(energyLevel: Float){
        var waterNeeded = energyLevel * WATER_PER_LIGHT
        roots.shuffle()
        for(i in roots){
            waterNeeded -= i.getWater(waterNeeded)
            if(waterNeeded <= 0) break
        }

        if(waterNeeded > 0) bottleneck = "water"
        else bottleneck = "light"

        energy += energyLevel-waterNeeded
    }

    private fun manageLists(){
        roots.removeIf {x->x.isDead}
        stems.removeIf {x->x.isDead}
        leaves.removeIf {x->x.isDead}
        nodes.removeIf {x->x.isDead}
    }

    fun storedEnergy(): Float{
        var storedEnergy = 0F

        for(i in roots){
            storedEnergy += i.storedEnergy
        }

        return storedEnergy
    }

    fun storedEnergyCapacity(): Float{
        var storedEnergyCapacity = 0F

        for(i in roots){
            storedEnergyCapacity += i.getMaxStoredEnergy()
        }

        return storedEnergyCapacity
    }

    fun currentBottleneck(): String {
        return bottleneck
    }

    fun isDead() : Boolean = root.children.isEmpty()
}