package me.infamous.kanekaido.client.rendering;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.entities.DragonKaido;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DragonKaidoModel extends AnimatedGeoModel<DragonKaido> {
	   
		@Override
		public ResourceLocation getAnimationFileLocation(DragonKaido entity) {
			return new ResourceLocation(KaneKaido.MODID, "animations/dragon_kaido.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(DragonKaido entity) {
			return new ResourceLocation(KaneKaido.MODID, "geo/dragon_kaido.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(DragonKaido entity) {
				return new ResourceLocation(KaneKaido.MODID, "textures/entity/dragon_kaido.png");
		}

		@Override
		public void setLivingAnimations(DragonKaido entity, Integer uniqueID, AnimationEvent event) {
			super.setLivingAnimations(entity, uniqueID, event);
			IBone head = this.getAnimationProcessor().getBone("head");

			EntityModelData extraData = (EntityModelData) event.getExtraDataOfType(EntityModelData.class).get(0);
			if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
				head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
				head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
			}
		}
	}