package vexsQol;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class VexsQOLMod {

    public static final String modID = "vexsqol";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public VexsQOLMod() {
        //BaseMod.subscribe(this);
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static void initialize() {
        VexsQOLMod thismod = new VexsQOLMod();
    }
}
