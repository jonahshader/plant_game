package jonahklayton.systems.plant

import com.badlogic.gdx.math.Vector2
import jonahklayton.systems.world.World
import kotlin.math.PI
import kotlin.random.Random

class EnemyPlant(xPosition: Float, energy: Float, world: World, difficulty: Int) : Plant(xPosition, energy, world, PI.toFloat()){

    var currentStem: Node = root
    var timeSinceLastAction = 0F
    var APS = (difficulty*.75) + .75f
    val maxLeaves = Random.nextInt(2, 6)
    var growthThreshold = (100/(difficulty+1))+7
    var branchChance = (1.9F)/(difficulty+1)

    override fun update(timePassed: Float) {
        super.update(timePassed)

        timeSinceLastAction += timePassed

        if(world.getDayProgress() < 0.5
            && getGrowingNodes().isEmpty()
            && storedEnergy() > growthThreshold
            && timeSinceLastAction*APS >= 1){

                timeSinceLastAction = 0F

                if(currentBottleneck().toUpperCase() == "LIGHT"){
                if(currentStem.children.size >= maxLeaves){
                    if(stems.size > 1 && Random.nextFloat() < branchChance){
                        stems.shuffle()
                        currentStem = stems[0]
                        makeStem(true)
                    }
                    else makeStem(false)
                }
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

    fun makeStem(branch: Boolean){
        var parent = currentStem

        var lr = 0F

        if(branch){
            lr += 45F - Random.nextInt(0, 2)*90F
        }

        var moveDist = 10

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg() + lr)
            .scl(Random.nextFloat()*5+5f))
        while(world.terrain.isUnderground(pos)){
            moveDist += 5
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg() + lr)
                .scl(Random.nextFloat()*(if(moveDist>180) 20 else 5)+5f))
        }

        pos.sub(parent.worldPosition)

        var child = Stem(pos, parent, this)

        parent.addChild(child)
        addStem(child)

        currentStem = child
    }

    fun makeRoot(){
        var lr = if(Random.nextBoolean()) 45F else -45F

//        var trying = true
//        while (trying) {
//
//        }

        roots.shuffle()
        var parent = roots[0]
        if(parent == root){
            parent = roots[1]
        }

        var moveDist = 10

        var pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
            .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg() + lr)
            .scl(Random.nextFloat()*20+5f))
        var tries = 0
        while(!world.terrain.isUnderground(pos) && tries < 50){
            moveDist += 5
            pos = parent.worldPosition.cpy().add(Vector2(1f,1f).nor()
                .setAngleDeg(Random.nextFloat()*moveDist-moveDist/2+parent.relativePosition.angleDeg() + lr)
                .scl(Random.nextFloat()*20+20f))
            tries++
        }
        if (tries < 50) {
            pos.sub(parent.worldPosition)

            var child = Root(pos, parent, this, 0F, world.terrain)

            parent.addChild(child)
            addRoot(child)
        }


    }

}