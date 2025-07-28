package mods.cybercat.gigeresque.datagen;

import mods.cybercat.gigeresque.datagen.lang.EnglishNewZealandLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import mods.cybercat.gigeresque.datagen.lang.EnglishLanguageProvider;

public class GigeresqueDataGengenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();

        // Language providers
        pack.addProvider(EnglishLanguageProvider::new);
        pack.addProvider(EnglishNewZealandLanguageProvider::new);

		// TODO
        // pack.addProvider(DeutschLanguageProvider::new);
        // pack.addProvider(MexicanSpanishLanguageProvider::new);
        // pack.addProvider(FrenchLanguageProvider::new);
        // pack.addProvider(JapaneseLanguageProvider::new);
        // pack.addProvider(KoreanLanguageProvider::new);
        // pack.addProvider(BrazilianPortugueseLanguageProvider::new);
        // pack.addProvider(RussianLanguageProvider::new);
        // pack.addProvider(ThaiLanguageProvider::new);
    }
}
