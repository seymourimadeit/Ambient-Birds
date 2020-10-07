package tallestegg.ambientbirds.entity;

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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tallestegg.ambientbirds.entity.goals.FlockGoal;
import tallestegg.ambientbirds.entity.goals.RandomFlyingGoal;


//Archemedes! No! Its filthy in zhere!
public class BirdEntity extends AnimalEntity implements IFlyingAnimal {

    protected static final DataParameter<Boolean> flockLeader= EntityDataManager.createKey(BirdEntity.class, DataSerializers.BOOLEAN);
    
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
        this.goalSelector.addGoal(1, new FlockGoal(this));
    }
    
    public boolean isFlockLeader() {
        return this.dataManager.get(flockLeader);
    }

    public void setLeader(boolean isLeader) {
        this.dataManager.set(flockLeader, isLeader);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(flockLeader, false);
    }

    //TODO : add something here
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        //int leaderChance = worldIn.getRandom().nextInt(2);
        //this.setLeader(true);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 6.0D)
                .func_233815_a_(Attributes.field_233822_e_, (double) 0.4F)
                .func_233815_a_(Attributes.field_233821_d_, (double) 0.2F);
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
}
