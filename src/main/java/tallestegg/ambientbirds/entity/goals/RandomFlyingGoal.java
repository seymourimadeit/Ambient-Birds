package tallestegg.ambientbirds.entity.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.util.math.vector.Vector3d;

public class RandomFlyingGoal extends RandomWalkingGoal {

    public RandomFlyingGoal(CreatureEntity p_i1648_1_, double p_i1648_2_) {
        super(p_i1648_1_, p_i1648_2_);
    }

    @Override
    protected Vector3d getPosition() {
        Vector3d vector3d = creature.getLook(0.0F);
        return RandomPositionGenerator.findAirTarget(this.creature, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
    }
}