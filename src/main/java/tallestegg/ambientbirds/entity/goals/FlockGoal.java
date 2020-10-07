package tallestegg.ambientbirds.entity.goals;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;
import tallestegg.ambientbirds.entity.BirdEntity;

public class FlockGoal extends Goal {
    private final BirdEntity taskOwner;
    private BirdEntity guardtofollow;
    private double x;
    private double y;
    private double z;

    public FlockGoal(BirdEntity taskOwnerIn) {
        this.taskOwner = taskOwnerIn;
    }

    public boolean shouldExecute() {
        List<BirdEntity> list = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
        if (!list.isEmpty()) {
            for (BirdEntity guard : list) {
                if (!guard.isInvisible() && guard.isFlockLeader() && !this.taskOwner.isFlockLeader() && this.taskOwner.world.getTargettableEntitiesWithinAABB(BirdEntity.class, (new EntityPredicate()).setDistance(3.0D), guard, this.taskOwner.getBoundingBox().grow(5.0D)).size() < 5) {
                    this.guardtofollow = guard;
                    Vector3d vec3d = this.getPosition();
                    if (vec3d == null) {
                        return false;
                    } else {
                        this.x = vec3d.x;
                        this.y = vec3d.y;
                        this.z = vec3d.z;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    protected Vector3d getPosition() {
        return RandomPositionGenerator.findRandomTargetBlockTowards(taskOwner, 1, 1, guardtofollow.getPositionVec());
    }

    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath() && !this.taskOwner.isBeingRidden();
    }

    public void resetTask() {
        this.taskOwner.getNavigator().clearPath();
        super.resetTask();
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToEntityLiving(guardtofollow, 1.0D);
    }
}