package vexsQol.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.powers.BurstPower;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import javassist.CtBehavior;
import vexsQol.relics.CursedCompass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static vexsQol.VexsQOLMod.makeID;

public class RelicChanges {

    @SpirePatch(
            clz = CursedKey.class,
            method = "setDescription"
    )
    public static class KeyDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CursedKeyDesc"));

        public static SpireReturn Prefix(CursedKey __instance, AbstractPlayer.PlayerClass c) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = AbstractChest.class,
            method = "update"
    )
    public static class YouCantOpenIt {
        public static SpireReturn Prefix(AbstractChest __instance) {
            if (AbstractDungeon.player.hasRelic(CursedKey.ID)) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = GamblingChip.class,
            method = "getUpdatedDescription"
    )
    public static class ChipDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("GamblingChipDesc");

        public static SpireReturn Prefix(GamblingChip __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = GamblingChip.class,
            method = "atTurnStartPostDraw"
    )
    public static class ChipStopDoingThat {
        public static SpireReturn Prefix(GamblingChip __instance) {
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = GamblingChip.class,
            method = "atBattleStart"
    )
    public static class ChipNewEffect {
        public static void Prefix(GamblingChip __instance) {
            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BurstPower(AbstractDungeon.player, 1), 1));
        }
    }

    @SpirePatch(
            clz = DreamCatcher.class,
            method = "getUpdatedDescription"
    )
    public static class DreamCatcherDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DreamCatcherDesc");

        public static SpireReturn Prefix(DreamCatcher __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = CampfireSleepEffect.class,
            method = "update"
    )
    public static class BetterDreams {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"rewardCards"}
        )
        public static void upgradeCards(CampfireSleepEffect __instance, ArrayList<AbstractCard> rewardCards) {
            for (AbstractCard c : rewardCards) {
                if (!c.upgraded) {
                    c.upgrade();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = PotionBelt.class,
            method = "getUpdatedDescription"
    )
    public static class PotionBeltDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("PotionBeltDesc");

        public static SpireReturn Prefix(PotionBelt __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = PotionBelt.class,
            method = "onEquip"
    )
    public static class PotionBeltComplementaryPotion {
        public static void Postfix(PotionBelt __instance) {
            AbstractDungeon.player.obtainPotion(AbstractDungeon.returnRandomPotion());
        }
    }

    @SpirePatch(
            clz = Boot.class,
            method = "getUpdatedDescription"
    )
    public static class BootDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("BootDesc");

        public static SpireReturn Prefix(Boot __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = ThornsPower.class,
            method = "onAttacked"
    )
    public static class BootNoThorns {
        public static SpireReturn Prefix(ThornsPower __instance, DamageInfo info, int damageAmount) {
            if (__instance.owner != AbstractDungeon.player && AbstractDungeon.player.hasRelic(Boot.ID)) {
                AbstractDungeon.player.getRelic(Boot.ID).flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SharpHidePower.class,
            method = "onUseCard"
    )
    public static class BootNoSharpHide {
        public static SpireReturn Prefix(ThornsPower __instance, DamageInfo info, int damageAmount) {
            if (__instance.owner != AbstractDungeon.player && AbstractDungeon.player.hasRelic(Boot.ID)) {
                AbstractDungeon.player.getRelic(Boot.ID).flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = BlueCandle.class,
            method = "getUpdatedDescription"
    )
    public static class BlueCandleDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("BlueCandleDesc");

        public static SpireReturn Prefix(BlueCandle __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = BlueCandle.class,
            method = "onUseCard"
    )
    public static class BlueCandleBuff {
        public static void Postfix(BlueCandle __instance, AbstractCard card, UseCardAction action) {
            if (card.type == AbstractCard.CardType.CURSE) {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
            }
        }
    }

    @SpirePatch(
            clz = TinyHouse.class,
            method = "getUpdatedDescription"
    )
    public static class TinyHouseDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("TinyHouseDesc");

        public static SpireReturn Prefix(TinyHouse __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = TinyHouse.class,
            method = "onEquip"
    )
    public static class TinyHouseBuff {
        public static void Postfix(TinyHouse __instance) {
            ArrayList<AbstractCard> upgradableCards = new ArrayList();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade()) {
                    upgradableCards.add(c);
                }
            }

            Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
            if (!upgradableCards.isEmpty()) {
                upgradableCards.get(0).upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard) upgradableCards.get(0));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard) upgradableCards.get(0)).makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            }
        }
    }


    @SpirePatch(clz = MapGenerator.class, method = "generateDungeon")
    public static class CursedCompassPatch {
        public static void Prefix(int height, int width, @ByRef int[] pathDensity, Random rng) {
            if (AbstractDungeon.player.hasRelic(CursedCompass.ID))
                pathDensity[0] = 1;
        }
    }

}
