package continuum.mod.util.handlers;

import continuum.mod.entities.EntityNPC;
import continuum.mod.entities.render.RenderNPC;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {
	public static void registerEntityRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new IRenderFactory<EntityNPC>() {
			@Override
			public Render<? super EntityNPC> createRenderFor(RenderManager manager) {
				return new RenderNPC(manager);
			}
		});
	}
}
