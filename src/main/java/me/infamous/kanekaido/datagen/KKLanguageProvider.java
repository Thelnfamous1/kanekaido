package me.infamous.kanekaido.datagen;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.client.keybindings.KKKeyBinding;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import me.infamous.kanekaido.common.registry.KKItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class KKLanguageProvider extends LanguageProvider {
    public KKLanguageProvider(DataGenerator gen) {
        super(gen, KaneKaido.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(KKKeyBinding.ABILITY_KEY_CATEGORY, "Kaido Abilities");
        this.add(KKKeyBinding.FIREBALL_DESCRIPTION_KEY, "Fireballs");
        this.add(KKKeyBinding.AIR_SLASH_DESCRIPTION_KEY, "Air Slash");
        this.add(KKKeyBinding.FIRE_BEAM_DESCRIPTION_KEY, "Fire Energy Beam");
        this.add(KKKeyBinding.MORPH_KEY_CATEGORY, "Morph Abilities");
        this.add(KKKeyBinding.KAIDO_MORPH_DESCRIPTION_KEY, "Kaido Morph");
        this.add(KKKeyBinding.DRAGON_KAIDO_MORPH_DESCRIPTION_KEY, "Dragon Kaido Morph");
        this.add(KKKeyBinding.ATTACK_KEY_CATEGORY, "Kaido Attacks");
        this.add(KKKeyBinding.KAIDO_ATTACK_A_DESCRIPTION_KEY, "Ground Pound");
        this.add(KKKeyBinding.KAIDO_ATTACK_B_DESCRIPTION_KEY, "Hammer Strike");
        this.addEntityType(KKEntityTypes.ENERGY_BEAM, "Energy Beam");
        this.addEntityType(KKEntityTypes.KAIDO, "Kaido");
        this.addEntityType(KKEntityTypes.DRAGON_KAIDO, "Dragon Kaido");
        this.add(KKItems.DEVIL_FRUIT.get(), "Devil Fruit");
    }
}
