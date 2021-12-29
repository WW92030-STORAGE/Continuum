package continuum.mod.entities.render;

import continuum.mod.entities.EntityNPC;
import continuum.mod.util.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderNPC extends RenderLiving<EntityNPC> {
	
	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entities/npcvendor.png");

	public RenderNPC(RenderManager manager) {
		super(manager, new ModelBiped(), 0.5f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityNPC e) {
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityNPC e, float p_77043_2_, float rotationYaw, float partialTicks) {
		super.applyRotations(e, p_77043_2_, rotationYaw, partialTicks);
	}
}

