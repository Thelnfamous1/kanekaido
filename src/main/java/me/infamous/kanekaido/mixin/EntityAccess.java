package me.infamous.kanekaido.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccess {

    @Invoker
    Vector3d callCalculateViewVector(float yaw, float pitch);
}
