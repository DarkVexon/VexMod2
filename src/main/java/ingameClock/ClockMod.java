package ingameClock;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ClockMod implements EditStringsSubscriber, PostInitializeSubscriber {

    public static final String modID = "ingameclock";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public ClockMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        ClockMod thismod = new ClockMod();
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

    @Override
    public void receivePostInitialize() {
        BaseMod.addTopPanelItem(new TopPanelClock());
    }
}
