package me.infamous.kanekaido.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.world.World;

public class DragonKaido extends Kaido{
    public DragonKaido(EntityType<? extends DragonKaido> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return Kaido.createAttributes();
    }
}
