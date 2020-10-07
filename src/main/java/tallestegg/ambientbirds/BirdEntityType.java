package tallestegg.ambientbirds;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tallestegg.ambientbirds.entity.BirdEntity;

public class BirdEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, AmbientBirds.MODID);

    public static final RegistryObject<EntityType<BirdEntity>> BIRD = ENTITIES.register("bird", () -> EntityType.Builder.create(BirdEntity::new, EntityClassification.MISC).size(0.9F, 0.9F).setShouldReceiveVelocityUpdates(true).build("ambientbirds:bird"));
}
