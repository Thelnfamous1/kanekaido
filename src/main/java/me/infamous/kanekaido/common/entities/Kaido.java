package me.infamous.kanekaido.common.entities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class Kaido extends CreatureEntity {
    public Kaido(EntityType<? extends Kaido> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }
}
