package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.World
import kotlin.math.PI
import kotlin.random.Random

class EnemyPlant(position: Vector2, energy: Float, world: World, APS: Int) : Plant(position, energy, world, PI.toFloat()){

    var currentStem: Node = root
    var timeSinceLastAction = 0F
    var APS = APS
    val maxLeaves = Random.nextInt(2, 6)
    var growthThreshold = 50

    override fun update(timePassed: Float) {
        super.update(timePassed)

        timeSinceLastAction += timePassed

        if(world.getIsMorning()
            && getGrowingNodes().isEmpty()
            && storedEnergy() > growthThreshold
            && timeSinceLastAction*APS >= 1){

                timeSinceLastAction = 0F

                if(currentBottleneck().toUpperCase() == "LIGHT"){
                if(currentStem.children.size >= maxLeaves) makeStem()
                else makeLeaf()
            }else{
                makeRoot()
            }
        }

    }

    fun makeLeaf(){
        var parent = currentStem

        var moveDist = 10

        var lr = if(Random.nextBoolean()) 70F else -70F

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg()+lr)
            .scl(Random.nextFloat()*10+15f))
        while(world.terrain.isUnderground(pos)){
            moveDist += 5
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg()+lr)
                .scl(Random.nextFloat()*10+15f))
        }

        pos.sub(parent.worldPosition)

        var child = Leaf(pos, parent, this)

        parent.addChild(child)
        addLeaf(child)
    }

    fun makeStem(){
        var parent = currentStem


        var moveDist = 10

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg())
            .scl(Random.nextFloat()*5+5f))
        while(world.terrain.isUnderground(pos)){
            moveDist += 5
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg())
                .scl(Random.nextFloat()*5+5f))
        }

        pos.sub(parent.worldPosition)

        var child = Stem(pos, parent, this)

        parent.addChild(child)
        addStem(child)

        currentStem = child
    }

    fun makeRoot(){
        var lr = if(Random.nextBoolean()) 45F else -45F

        roots.shuffle()
        var parent = roots.get(0)
        if(parent == root){
            parent = roots.get(1)
        }

        var moveDist = 10

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg() + lr)
            .scl(Random.nextFloat()*20+5f))
        while(!world.terrain.isUnderground(pos)){
            moveDist += 5
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg() + lr)
                .scl(Random.nextFloat()*20+25f))
        }

        pos.sub(parent.worldPosition)

        var child = Root(pos, parent, this, 0F, world.terrain)

        parent.addChild(child)
        addRoot(child)
    }

}