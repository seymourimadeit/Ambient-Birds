package tallestegg.ambientbirds;

import net.minecraft.entity.passive.ParrotEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tallestegg.ambientbirds.entity.goals.RandomFlyingGoal;

@Mod.EventBusSubscriber(modid = AmbientBirds.MODID)
public class ABEvents {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ParrotEntity) {
            ParrotEntity parrot = (ParrotEntity)event.getEntity();
            parrot.goalSelector.addGoal(1, new RandomFlyingGoal(parrot, 1.0D));
        }
    }
}
