package me.infamous.kanekaido.mixin;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ActiveRenderInfo.class)
public interface ActiveRenderInfoAccess {

    @Invoker
    void callSetPosition(double x, double y, double z);

    @Invoker
    void callMove(double x, double y, double z);

    @Invoker
    double callGetMaxZoom(double startingDistance);

    @Accessor
    float getEyeHeightOld();

    @Accessor
    float getEyeHeight();

    @Accessor
    Vector3f getLeft();

    @Accessor
    boolean getMirror();
}
