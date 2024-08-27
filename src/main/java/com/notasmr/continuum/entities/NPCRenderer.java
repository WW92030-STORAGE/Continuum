package com.notasmr.continuum.entities;

import com.notasmr.continuum.util.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;

public class NPCRenderer extends MobRenderer<NPCEntity, BipedModel<NPCEntity>> {
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(Reference.MODID, "textures/entity/darkness.png");

    public NPCRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PlayerModel<>(0, true), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(NPCEntity entity) {
        return TEXTURE;
    }
}
