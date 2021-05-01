package jonahklayton.systems.plant

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.World


// growth/existence cost is taken care of here not in the nodes themselves
class Plant(position: Vector2, startingEnergy: Float, world: World){
    private val ENERGY_PER_LENGTH_SUSTAINED = 0.1F
    private val ENERGY_PER_LENGTH_GROWN = 1F
    private val ENERGY_PER_LENGTH_KILLED = 0.9F
    private val STORE_RATIO = 0.5F

    var worldPosition = position
        private set

    var world = world
        private set

    var root = Root(Vector2.Zero, null, this, startingEnergy)
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

    init {
        addRoot(root as Root)

        var child = Root(Vector2(0F, -1F), root, this, 0F)
        root.addChild(child)

        addRoot(child)
    }

    //call to get energy from somewhere in the plant, returns the amount of energy gotten
    private fun requestEnergy(energyRequested: Float): Float{
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

    fun update(timePassed: Float){

        //use energy to maintain plant
        for(i in nodes){
            var energyNeeded = i.getLength() * ENERGY_PER_LENGTH_SUSTAINED * timePassed
            energyNeeded -= requestEnergy(energyNeeded)
            if(energyNeeded >= 0) getEnergyFromChildNodes(energyNeeded)
        }

        if(energy > 0) {

            //split energy up
            var storeEnergy = energy*STORE_RATIO
            var growEnergy = energy-storeEnergy

            //grow whatever we can grow

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

            //store energy in roots
            for (i in roots) {
                if (storeEnergy > 0) {
                    storeEnergy -= i.storeEnergy(storeEnergy)
                } else break
            }
        }



        //discard leftover energy
        energy = 0F

        manageLists()
    }

    fun draw(renderer: ShapeRenderer){
        for(i in nodes){
            i.draw(renderer)
        }
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

    fun recieveLight(energyLevel: Float){
        var waterNeeded = energyLevel
        roots.shuffle()
        for(i in roots){
            waterNeeded -= i.getWater(waterNeeded)
            if(waterNeeded <= 0) break;
        }

        energy += energyLevel-waterNeeded
    }

    private fun manageLists(){
        roots.removeIf {x->x.isDead}
        stems.removeIf {x->x.isDead}
        leaves.removeIf {x->x.isDead}
        nodes.removeIf {x->x.isDead}
    }
}