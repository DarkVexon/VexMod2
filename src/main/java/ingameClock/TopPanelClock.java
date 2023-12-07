package ingameClock;

import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import theFishing.FishingMod;

public class TopPanelClock extends TopPanelItem {

    private static final float tipYpos = Settings.HEIGHT - (120.0f * Settings.scale);

    public static final String ID = ClockMod.makeID("TopPanelClock");

    public static final Texture ICON = TexLoader;
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public TopPanelClock() {
        super(ICON, ID);
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void render(SpriteBatch sb) {
        render(sb, Color.WHITE);
        if (getHitbox().hovered) {
            TipHelper.renderGenericTip(getHitbox().x, tipYpos, uiStrings.TEXT[0], uiStrings.TEXT[1] + FishingMod.activeBoard.getDescription());
        }
    }

    @Override
    protected void onClick() {

    }
}
