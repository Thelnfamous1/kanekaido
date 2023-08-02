package me.infamous.kanekaido.common.registry;

import me.infamous.kanekaido.KaneKaido;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class KKItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KaneKaido.MODID);

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
