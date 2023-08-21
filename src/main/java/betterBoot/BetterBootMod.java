package betterBoot;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import betterBoot.relics.CursedCompass;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class BetterBootMod implements EditStringsSubscriber {

    public static final String modID = "betterboot";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public BetterBootMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        BetterBootMod thismod = new BetterBootMod();
    }

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
    };

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        return "eng";
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/" + getLangString() + "/UIStrings.json");
    }
}
