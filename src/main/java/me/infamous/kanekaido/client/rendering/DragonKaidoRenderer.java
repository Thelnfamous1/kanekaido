package me.infamous.kanekaido.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.infamous.kanekaido.common.entities.DragonKaido;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DragonKaidoRenderer extends GeoEntityRenderer<DragonKaido> {
	public DragonKaidoRenderer(EntityRendererManager renderManager) {
		super(renderManager, new DragonKaidoModel());
	}

	@Override
	public void renderEarly(DragonKaido animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		stackIn.pushPose();
		boolean isMoving = this.isMoving(animatable, partialTicks);
		stackIn.translate(0.0F, isMoving ? -66.0F : -223.0F, isMoving ? 364.0F : 286.0F);
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		stackIn.popPose();
	}

	private boolean isMoving(DragonKaido animatable, float partialTicks) {
		float limbSwingAmount = 0.0F;
		//float limbSwing = 0.0F;
		boolean shouldSit = animatable.isPassenger()
				&& (animatable.getVehicle() != null && animatable.getVehicle().shouldRiderSit());
		if (!shouldSit && animatable.isAlive()) {
			limbSwingAmount = MathHelper.lerp(partialTicks, animatable.animationSpeedOld, animatable.animationSpeed);
			//limbSwing = animatable.animationPosition - animatable.animationSpeed * (1.0F - partialTicks);
			if (animatable.isBaby()) {
				//limbSwing *= 3.0F;
			}

			if (limbSwingAmount > 1.0F) {
				limbSwingAmount = 1.0F;
			}
		}
		return !(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F);
	}

	@Override
	public RenderType getRenderType(DragonKaido animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(this.getTextureLocation(animatable));
	}
}