package vexsQol.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import vexsQol.VexsQOLMod;
import vexsQol.util.TexLoader;

public class CursedCompass extends CustomRelic {
    public static final String ID = VexsQOLMod.makeID(CursedCompass.class.getSimpleName());

    private static final Texture IMG = TexLoader.getTexture(VexsQOLMod.makeRelicPath("CursedCompass.png"));

    private static final Texture OUTLINE = TexLoader.getTexture(VexsQOLMod.makeRelicPath("CursedCompass_outline.png"));

    public CursedCompass() {
        super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
