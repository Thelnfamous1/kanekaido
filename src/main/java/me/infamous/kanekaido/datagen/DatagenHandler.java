package me.infamous.kanekaido.datagen;

import me.infamous.kanekaido.KaneKaido;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = KaneKaido.MODID)
public class DatagenHandler {

    @SubscribeEvent
    static void gatherDataEvent(GatherDataEvent event){
        boolean includeClient = event.includeClient();
        DataGenerator generator = event.getGenerator();
        KKLanguageProvider languageProvider = new KKLanguageProvider(generator);
        if(includeClient){
            generator.addProvider(languageProvider);
        }
    }
}
