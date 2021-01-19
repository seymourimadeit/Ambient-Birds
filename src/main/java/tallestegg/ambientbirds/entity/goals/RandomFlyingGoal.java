package tallestegg.ambientbirds.entity.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import tallestegg.ambientbirds.entity.BirdEntity;

public class RandomFlyingGoal extends RandomWalkingGoal {

    public RandomFlyingGoal(CreatureEntity p_i1648_1_, double p_i1648_2_) {
        super(p_i1648_1_, p_i1648_2_, 10);
    }

    @Override
    protected Vector3d getPosition() {
        Vector3d vector3d = creature.getLook(0.0F);
        return RandomPositionGenerator.findAirTarget(this.creature, 50, 100, vector3d, ((float) Math.PI / 2F), 2, 1);
    }

    @Override
    public void startExecuting() {
        Vector3d vector3d = this.getPosition();
        if (vector3d == null)
            return;
        creature.getNavigator().setPath(creature.getNavigator().getPathToPos(new BlockPos(vector3d), 1),  this.speed);
    }

    @Override
    public boolean shouldExecute() {
        return creature instanceof BirdEntity ? !((BirdEntity) creature).hasGroupLeader() && creature.getRNG().nextInt(10) == 0 : super.shouldExecute();
    }
}