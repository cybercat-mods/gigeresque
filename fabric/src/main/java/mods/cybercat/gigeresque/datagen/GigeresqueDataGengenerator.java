package mods.cybercat.gigeresque.datagen;

import mods.cybercat.gigeresque.datagen.lang.EnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class GigeresqueDataGengenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		// Language providers
		pack.addProvider(EnglishLanguageProvider::new);

	}
}
