package tallestegg.ambientbirds.entity.goals;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.ai.goal.Goal;
import tallestegg.ambientbirds.entity.BirdEntity;

public class FlockGoal extends Goal {
    private final BirdEntity taskOwner;
    private int navigateTimer;
    private int field_222740_c;

    public FlockGoal(BirdEntity taskOwnerIn) {
        this.taskOwner = taskOwnerIn;
        this.field_222740_c = this.func_212825_a(taskOwnerIn);
    }

    protected int func_212825_a(BirdEntity taskOwnerIn) {
        return 200 + taskOwnerIn.getRNG().nextInt(200) % 20;
    }

    @Override
    public boolean shouldExecute() {
        if (this.taskOwner.flockSize > 1) {
            return false;
        } else if (this.taskOwner.hasGroupLeader()) {
            return true;
        } else if (this.field_222740_c > 0) {
            --this.field_222740_c;
            return false;
        } else {
            this.field_222740_c = this.func_212825_a(this.taskOwner);
            Predicate<BirdEntity> predicate = (p_212824_0_) -> {
                return p_212824_0_.canGroupGrow() || !p_212824_0_.hasGroupLeader();
            };
            List<BirdEntity> list = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().grow(8.0D, 8.0D, 8.0D), predicate);
            BirdEntity bird = list.stream().filter(BirdEntity::canGroupGrow).findAny().orElse(this.taskOwner);
            bird.joinSquad(list.stream().filter((p_212823_0_) -> {
                return !p_212823_0_.hasGroupLeader();
            }));
            return this.taskOwner.hasGroupLeader();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.taskOwner.hasGroupLeader() && this.taskOwner.inRangeOfGroupLeader();
    }

    @Override
    public void startExecuting() {
        this.navigateTimer = 0;
    }

    @Override
    public void resetTask() {
        this.taskOwner.leaveGroup();
    }

    @Override
    public void tick() {
        if (--this.navigateTimer <= 0) {
            this.navigateTimer = 10;
            if (taskOwner.hasGroupLeader())
                this.taskOwner.getNavigator().tryMoveToEntityLiving(taskOwner.flockLeader, 10.0D);
        }
    }
}