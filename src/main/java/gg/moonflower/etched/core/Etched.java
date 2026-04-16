package gg.moonflower.etched.core;

import com.tterrag.registrate.Registrate;
import gg.moonflower.etched.api.sound.download.SoundSourceManager;
import gg.moonflower.etched.common.network.EtchedMessages;
import gg.moonflower.etched.common.sound.download.BandcampSource;
import gg.moonflower.etched.common.sound.download.SoundCloudSource;
import gg.moonflower.etched.core.fabric.EtchedConfig;
import gg.moonflower.etched.core.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Etched {

    public static final String MOD_ID = "etched";
    public static final Registrate REGISTRATE = Registrate.create(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger("Etched/General");

    public static void init() {
        EtchedTags.register();
        EtchedMessages.init();
        EtchedBlocks.register();
        EtchedEntities.register();
        EtchedItems.register();
        EtchedMenus.register();
        EtchedRecipes.register();
        EtchedSounds.register();
        REGISTRATE.register();

        EtchedVillagers.registers(); // POI исправлен — раскомментировано

        EtchedConfig.HANDLER.load();
        SoundSourceManager.registerSource(new SoundCloudSource());
        SoundSourceManager.registerSource(new BandcampSource());
    }
}