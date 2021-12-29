package continuum.mod.entities.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelNPC extends ModelBase {
    public ModelRenderer RightArm;
    public ModelRenderer RightLeg;
    public ModelRenderer Head1;
    public ModelRenderer Body;
    public ModelRenderer LeftArm;
    public ModelRenderer LeftLeg;
    public ModelRenderer Head2;
    
    public ModelNPC() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Head2 = new ModelRenderer(this, 32, 0);
        this.Head2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head2.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.RightLeg = new ModelRenderer(this, 0, 16);
        this.RightLeg.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.RightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.RightArm = new ModelRenderer(this, 40, 16);
        this.RightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.RightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(RightArm, 0.0F, 0.0F, 0.0F); // 0.10000736613927509F
        this.LeftArm = new ModelRenderer(this, 40, 16);
        this.LeftArm.mirror = true;
        this.LeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.LeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(LeftArm, 0.0F, 0.0F, 0.0F); // -0.10000736613927509F
        this.Head1 = new ModelRenderer(this, 0, 0);
        this.Head1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head1.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.Body = new ModelRenderer(this, 16, 16);
        this.Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.LeftLeg = new ModelRenderer(this, 0, 16);
        this.LeftLeg.mirror = true;
        this.LeftLeg.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.LeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Head2.render(f5);
        this.RightLeg.render(f5);
        this.RightArm.render(f5);
        this.LeftArm.render(f5);
        this.Head1.render(f5);
        this.Body.render(f5);
        this.LeftLeg.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) 
    {
    	this.LeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    	this.RightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    	
    	this.LeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    	this.RightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    	
    	this.Head1.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.Head1.rotateAngleX = headPitch * 0.017453292F;
    	this.Head2.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.Head2.rotateAngleX = headPitch * 0.017453292F;
    }
}
