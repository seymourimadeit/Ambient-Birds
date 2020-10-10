package tallestegg.ambientbirds.entity;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
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
    public AgeableEntity func_241840_a(ServerWorld arg0, AgeableEntity arg1) {
        return null;
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(1, new RandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new FlockGoal(this));
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
        p_212810_1_.limit((long) (34 - this.flockSize)).filter((flockController) -> {
            return flockController != this;
        }).forEach((p_212804_1_) -> {
            p_212804_1_.leaderIncreaseSize(this);
        });
    }

    public boolean canGroupGrow() {
        return this.flockSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.getDistanceSq(this.flockLeader) <= 121.0D;
    }

    @Override
    public void travel(Vector3d p_213352_1_) {
        if (this.isInWater()) {
            this.moveRelative(0.02F, p_213352_1_);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale((double) 0.8F));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, p_213352_1_);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.5D));
        } else {
            BlockPos ground = new BlockPos(this.getPosX(), this.getPosY() - 1.0D, this.getPosZ());
            float f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(ground).getSlipperiness(this.world, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(ground).getSlipperiness(this.world, ground, this) * 0.91F;
            }

            this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, p_213352_1_);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale((double) f));
        }

        this.func_233629_a_(this, false);
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }

    // TODO : add something here
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
