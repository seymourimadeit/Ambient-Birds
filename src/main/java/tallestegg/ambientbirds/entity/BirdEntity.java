package tallestegg.ambientbirds.entity;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tallestegg.ambientbirds.ABEntityType;
import tallestegg.ambientbirds.entity.goals.FlockGoal;
import tallestegg.ambientbirds.entity.goals.RandomFlyingGoal;

//Archemedes! No! Its filthy in zhere!
public class BirdEntity extends AnimalEntity implements IFlyingAnimal {

    public BirdEntity flockLeader;
    public int flockSize = -1;

    public BirdEntity(EntityType<? extends BirdEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 20, true);
    }

    @Override
    public BirdEntity func_241840_a(ServerWorld arg0, AgeableEntity arg1) {
        BirdEntity bird = ABEntityType.BIRD.get().create(arg0);
        return bird;
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(1, new RandomFlyingGoal(this, 7.0D));
        this.goalSelector.addGoal(2, new FlockGoal(this));
    }

    public boolean onGround() {
        return this.onGround;
    }

    public BirdEntity leaderIncreaseSize(BirdEntity groupLeaderIn) {
        this.flockLeader = groupLeaderIn;
        groupLeaderIn.increaseFlockSize();
        return groupLeaderIn;
    }

    private void increaseFlockSize() {
        ++this.flockSize;
    }

    public boolean hasGroupLeader() {
        return this.flockLeader != null && this.flockLeader.isAlive();
    }

    public void joinSquad(Stream<BirdEntity> p_212810_1_) {
        p_212810_1_.limit((long) (69 - this.flockSize)).filter((flockController) -> {
            return flockController != this;
        }).forEach((p_212804_1_) -> {
            p_212804_1_.leaderIncreaseSize(this);
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (this.flockSize > 1 && this.world.rand.nextInt(200) == 1) {
            List<BirdEntity> list = this.world.getEntitiesWithinAABB(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.flockSize = 1;
            }
        }

    }

    public boolean canGroupGrow() {
        return this.flockSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.getDistance(this.flockLeader) <= 121.0D;
    }

    @Override
    public void travel(Vector3d travelVector) {
        super.travel(travelVector);
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if (spawnDataIn == null) {
            spawnDataIn = new BirdEntity.GroupData(this);
        } else {
            this.leaderIncreaseSize(((BirdEntity.GroupData) spawnDataIn).groupLeader);
        }
        return spawnDataIn;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.field_233818_a_, 6.0D).func_233815_a_(Attributes.field_233822_e_, (double) 0.4F).func_233815_a_(Attributes.field_233821_d_, (double) 0.2F);
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public void leaveGroup() {
        this.flockLeader.decreaseGroupSize();
        this.flockLeader = null;
    }

    private void decreaseGroupSize() {
        --this.flockSize;
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn) {
            @SuppressWarnings("deprecation")
            public boolean canEntityStandOnPos(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(false);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    public static class GroupData implements ILivingEntityData {
        public final BirdEntity groupLeader;

        public GroupData(BirdEntity groupLeaderIn) {
            this.groupLeader = groupLeaderIn;
        }
    }
}
