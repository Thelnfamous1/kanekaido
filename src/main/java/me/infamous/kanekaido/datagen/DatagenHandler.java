package me.infamous.kanekaido.datagen;

import me.infamous.kanekaido.KaneKaido;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = KaneKaido.MODID)
public class DatagenHandler {

    @SubscribeEvent
    static void gatherDataEvent(GatherDataEvent event){
        boolean includeClient = event.includeClient();
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        KKLanguageProvider languageProvider = new KKLanguageProvider(generator);
        KKItemModelProvider itemModelProvider = new KKItemModelProvider(generator, existingFileHelper);
        if(includeClient){
            generator.addProvider(languageProvider);
            generator.addProvider(itemModelProvider);
        }
    }
}
