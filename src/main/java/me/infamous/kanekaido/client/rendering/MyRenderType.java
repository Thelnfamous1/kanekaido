package me.infamous.kanekaido.client.rendering;

import me.infamous.kanekaido.KaneKaido;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MyRenderType extends RenderType {
    private final static ResourceLocation beaconBeamCore = new ResourceLocation(KaneKaido.MODID + ":textures/misc/beacon_beam_core.png");
    private final static ResourceLocation beaconBeamMain = new ResourceLocation(KaneKaido.MODID + ":textures/misc/beacon_beam_main.png");
    private final static ResourceLocation beaconBeamGlow = new ResourceLocation(KaneKaido.MODID + ":textures/misc/beacon_beam_glow.png");
    // Dummy
    public MyRenderType(String name, VertexFormat format, int mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable runnablePre, Runnable runnablePost) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, runnablePre, runnablePost);
    }

    public static final RenderType ENERGY_BEAM_MAIN = create("BeaconBeamMain",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.builder().setTextureState(new TextureState(beaconBeamMain, false, false))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));

    public static final RenderType ENERGY_BEAM_GLOW = create("BeaconBeamGlow",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.builder().setTextureState(new TextureState(beaconBeamGlow, false, false))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));

    public static final RenderType ENERGY_BEAM_CORE = create("BeaconBeamCore",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.builder().setTextureState(new TextureState(beaconBeamCore, false, false))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));
}