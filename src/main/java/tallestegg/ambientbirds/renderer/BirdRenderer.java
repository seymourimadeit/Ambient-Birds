package tallestegg.ambientbirds.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.util.ResourceLocation;
import tallestegg.ambientbirds.entity.BirdEntity;

//pige
public class BirdRenderer extends MobRenderer<BirdEntity, PigModel<BirdEntity>> {

    private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");

    public BirdRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PigModel<>(), 0.7F);
    }

    public ResourceLocation getEntityTexture(BirdEntity entity) {
        return PIG_TEXTURES;
    }
}
