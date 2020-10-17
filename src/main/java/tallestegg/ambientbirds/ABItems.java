package tallestegg.ambientbirds;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tallestegg.ambientbirds.items.DeferredSpawnEggItem;

public class ABItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AmbientBirds.MODID);
    
    public static final RegistryObject<DeferredSpawnEggItem> BIRD_SPAWN_EGG = ITEMS.register("bird_spawn_egg", () -> new DeferredSpawnEggItem(ABEntityType.BIRD, 19815, 9804699, new Item.Properties().group(ItemGroup.MISC)));
}
