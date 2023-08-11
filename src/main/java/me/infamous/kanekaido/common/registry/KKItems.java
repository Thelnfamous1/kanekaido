package me.infamous.kanekaido.common.registry;

import me.infamous.kanekaido.KaneKaido;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class KKItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KaneKaido.MODID);

    private static final Food DEVIL_FRUIT_FOOD = (new Food.Builder()).alwaysEat().build();

    public static final RegistryObject<Item> DEVIL_FRUIT = ITEMS.register("devil_fruit", () -> new Item(new Item.Properties().food(DEVIL_FRUIT_FOOD).tab(ItemGroup.TAB_FOOD)));

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
