package me.infamous.kanekaido.client.events;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.client.keybindings.KKKeyBinding;
import me.infamous.kanekaido.common.abilities.KaidoAttack;
import me.infamous.kanekaido.common.entities.Kaido;
import me.infamous.kanekaido.common.network.NetworkHandler;
import me.infamous.kanekaido.common.network.ServerboundSpecialAttackPacket;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import me.infamous.kanekaido.mixin.ActiveRenderInfoAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = KaneKaido.MODID, value = Dist.CLIENT)
public class ForgeClientEventHandler {
    public static final double OFFSET_X = 0.0D;
    public static final double KAIDO_OFFSET_Y = 6.0D;
    public static final double KAIDO_OFFSET_Z = 12.0D;
    public static final double DRAGON_OFFSET_Y = 9.0D;
    public static final double DRAGON_OFFSET_Z = 12.0D;
    public static final double DRAGON_OFFSET_Y_MIRRORED = 6.0D;
    public static final double DRAGON_OFFSET_Z_MIRRORED = 18.0D;

    @SubscribeEvent
    static void onKeyInput(InputEvent.KeyInputEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity clientPlayer = minecraft.player;
        if(clientPlayer != null){
            int key = event.getKey();
            handleInput(clientPlayer, key);
        }
    }

    private static void handleInput(ClientPlayerEntity clientPlayer, int key) {
        KKKeyBinding.handleAllKeys(key, clientPlayer);
        if(key == Minecraft.getInstance().options.keyAttack.getKey().getValue()){
            LivingEntity activeMorphEntity = MorphHandler.INSTANCE.getActiveMorphEntity(clientPlayer);
            if(activeMorphEntity != null && activeMorphEntity.getType() == KKEntityTypes.KAIDO.get()){
                NetworkHandler.INSTANCE.sendToServer(new ServerboundSpecialAttackPacket(KaidoAttack.ATTACK_B));
            }
        }
    }

    @SubscribeEvent
    static void onClickInput(InputEvent.ClickInputEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity clientPlayer = minecraft.player;
        if(clientPlayer != null){
            KeyBinding keyBinding = event.getKeyBinding();
            handleInput(clientPlayer, keyBinding.getKey().getValue());
        }
    }

    @SubscribeEvent
    static void onCameraSetup(EntityViewRenderEvent.CameraSetup event){
        ActiveRenderInfo info = event.getInfo();
        Entity entity = info.getEntity();
        if(entity instanceof PlayerEntity) {
            LivingEntity activeMorphEntity = MorphHandler.INSTANCE.getActiveMorphEntity((PlayerEntity) entity);
            if (activeMorphEntity instanceof Kaido && info.isDetached()) {
                boolean isDragon = activeMorphEntity.getType() == KKEntityTypes.DRAGON_KAIDO.get();
                ActiveRenderInfoAccess accessor = (ActiveRenderInfoAccess) info;
                boolean isMirror = accessor.getMirror();
                double partialTick = event.getRenderPartialTicks();
                double x = MathHelper.lerp(partialTick, activeMorphEntity.xo, activeMorphEntity.getX());
                double y = MathHelper.lerp(partialTick, activeMorphEntity.yo, activeMorphEntity.getY()) + MathHelper.lerp(partialTick, accessor.getEyeHeightOld(), accessor.getEyeHeight());
                double z = MathHelper.lerp(partialTick, activeMorphEntity.zo, activeMorphEntity.getZ());
                accessor.callSetPosition(x, y, z);
                double offsetY = isDragon ? isMirror ? DRAGON_OFFSET_Y_MIRRORED : DRAGON_OFFSET_Y : KAIDO_OFFSET_Y;
                double offsetZ = isDragon ? isMirror ? DRAGON_OFFSET_Z_MIRRORED : DRAGON_OFFSET_Z : KAIDO_OFFSET_Z;
                Vector3d offset = new Vector3d(-offsetZ, offsetY, OFFSET_X);
                double cameraDistance = calcCameraDistance(info, entity.level, accessor.callGetMaxZoom(offset.length()), OFFSET_X, offsetY, offsetZ);
                Vector3d scaled = offset.normalize().scale(cameraDistance);
                accessor.callMove(scaled.x, scaled.y, scaled.z);
            }
        }
    }

    private static double calcCameraDistance(ActiveRenderInfo camera, World level, double distance, double offsetX, double offsetY, double offsetZ)
    {
        Vector3d cameraPos = camera.getPosition();
        Vector3d cameraOffset = calcCameraOffset(camera, distance, offsetX, offsetY, offsetZ);

        for(int i = 0; i < 8; i++)
        {
            Vector3d offset = new Vector3d(i & 1, i >> 1 & 1, i >> 2 & 1)
                    .scale(2)
                    .subtract(1, 1, 1)
                    .scale(0.075);
            Vector3d from = cameraPos.add(offset);
            Vector3d to = from.add(cameraOffset);
            RayTraceContext context = new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, camera.getEntity());
            RayTraceResult hitResult = level.clip(context);

            if(hitResult != null)
            {
                double newDistance = hitResult.getLocation().distanceTo(cameraPos);

                if(newDistance < distance)
                {
                    distance = newDistance - 0.2D;
                }
            }
        }

        return distance;
    }

    public static Vector3d calcCameraOffset(ActiveRenderInfo info, double distance, double offsetX, double offsetY, double offsetZ)
    {
        ActiveRenderInfoAccess accessor = (ActiveRenderInfoAccess) info;
        double dX = info.getUpVector().x() * offsetY + accessor.getLeft().x() * offsetX + info.getLookVector().x() * -offsetZ;
        double dY = info.getUpVector().y() * offsetY + accessor.getLeft().y() * offsetX + info.getLookVector().y() * -offsetZ;
        double dZ = info.getUpVector().z() * offsetY + accessor.getLeft().z() * offsetX + info.getLookVector().z() * -offsetZ;
        return new Vector3d(dX, dY, dZ).normalize().scale(distance);
    }
}
