package me.infamous.kanekaido.common.registry;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.entities.DragonKaido;
import me.infamous.kanekaido.common.entities.EnergyBeam;
import me.infamous.kanekaido.common.entities.Kaido;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class KKEntityTypes {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, KaneKaido.MODID);

    public static final RegistryObject<EntityType<Kaido>> KAIDO = register("kaido",
            EntityType.Builder.of(Kaido::new, EntityClassification.MONSTER)
                    .sized(3.0F, 7.0F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<DragonKaido>> DRAGON_KAIDO = register("dragon_kaido",
            EntityType.Builder.of(DragonKaido::new, EntityClassification.MONSTER)
                    .sized(3.0F, 7.0F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<EnergyBeam>> ENERGY_BEAM = register("energy_beam",
            EntityType.Builder.<EnergyBeam>of(EnergyBeam::new, EntityClassification.MISC)
                    .fireImmune()
                    .setShouldReceiveVelocityUpdates(false)
                    .sized(2.0F, 1.0F)
                    .clientTrackingRange(6)
                    .updateInterval(2));


    private static <T extends Entity> RegistryObject<EntityType<T>> register(String pKey, EntityType.Builder<T> pBuilder) {
        return ENTITY_TYPES.register(pKey, () -> pBuilder.build(KaneKaido.MODID + ":" + pKey));
    }

    public static void register(IEventBus modEventBus){
        ENTITY_TYPES.register(modEventBus);
    }
}
