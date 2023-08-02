package me.infamous.kanekaido.datagen;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.client.keybindings.KKKeyBinding;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class KKLanguageProvider extends LanguageProvider {
    public KKLanguageProvider(DataGenerator gen) {
        super(gen, KaneKaido.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(KKKeyBinding.ABILITY_KEY_CATEGORY, "Kaido Abilities");
        this.add(KKKeyBinding.FIREBALL_DESCRIPTION_KEY, "Fireball");
        this.add(KKKeyBinding.AIR_SLASH_DESCRIPTION_KEY, "Air Slash");
        this.add(KKKeyBinding.FIRE_BEAM_DESCRIPTION_KEY, "Fire Energy Beam");
        this.add(KKKeyBinding.MORPH_KEY_CATEGORY, "Morph Abilities");
    }
}
