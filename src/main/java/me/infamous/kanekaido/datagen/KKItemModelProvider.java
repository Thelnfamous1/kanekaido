package me.infamous.kanekaido.datagen;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.registry.KKItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class KKItemModelProvider extends ItemModelProvider {
    public KKItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KaneKaido.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ResourceLocation id = KKItems.DEVIL_FRUIT.getId();
        this.singleTexture(id.getPath(), mcLoc("item/generated"), new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
    }
}
