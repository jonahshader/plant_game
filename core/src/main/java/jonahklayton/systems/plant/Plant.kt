package jonahklayton.systems.plant

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class Plant(position: Vector2){
    private val STARTING_ENERGY = 100.0F
    private val ENERGY_PER_LENGTH = 0.1f;

    var root = Node(Vector2.Zero, null, this)

    var energy = STARTING_ENERGY

    var nodes = mutableListOf<Node>()
        private set

    var roots = mutableListOf<Root>()
        private set

    var stems = mutableListOf<Stem>()
        private set

    var leaves = mutableListOf<Leaf>()
        private set

    fun update(timePassed: Float){



        manageLists()
    }

    fun draw(renderer: ShapeRenderer){

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