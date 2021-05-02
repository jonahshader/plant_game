package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.World
import kotlin.random.Random

class EnemyPlant(position: Vector2, energy: Float, world: World) : Plant(position, energy, world){

    companion object{
        const val MAX_LEAVES = 2
        const val GROWTH_THRESHOLD = 25
    }

    var currentStem: Node = root

    override fun update(timePassed: Float) {
        super.update(timePassed)

        if(world.getIsMorning() && getGrowingNodes().isEmpty() && storedEnergy() > GROWTH_THRESHOLD){
            if(currentBottleneck().toUpperCase() == "LIGHT"){
                if(currentStem.children.size >= MAX_LEAVES) makeStem()
                else makeLeaf()
            }else{
                makeRoot()
            }
        }

    }

    fun makeLeaf(){
        var parent = currentStem

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat())
            .scl(Random.nextFloat()*10+15f))
        while(world.terrain.isUnderground(pos)){
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*360)
                .scl(Random.nextFloat()*10+15f))
        }

        pos.sub(parent.worldPosition)

        var child = Leaf(pos, parent, this)

        parent.addChild(child)
        addLeaf(child)
    }

    fun makeStem(){
        var parent = currentStem

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat())
            .scl(Random.nextFloat()*5+5f))
        while(world.terrain.isUnderground(pos)){
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*180)
                .scl(Random.nextFloat()*5+5f))
        }

        pos.sub(parent.worldPosition)

        var child = Stem(pos, parent, this)

        parent.addChild(child)
        addStem(child)

        currentStem = child
    }

    fun makeRoot(){
        roots.shuffle()
        var parent = roots.get(0)

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat())
            .scl(Random.nextFloat()*20+5f))
        while(!world.terrain.isUnderground(pos)){
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*360)
                .scl(Random.nextFloat()*20+5f))
        }

        pos.sub(parent.worldPosition)

        var child = Root(pos, parent, this, 0F, world.terrain)

        parent.addChild(child)
        addRoot(child)
    }

}