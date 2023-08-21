package vexsQol.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.DuplicationPower;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
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
            clz = DreamCatcher.class,
            method = "getUpdatedDescription"
    )
    public static class DreamCatcherDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("DreamCatcherDesc"));

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
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("PotionBeltDesc"));

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
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("BootDesc"));

        public static SpireReturn Prefix(Boot __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = ThornsPower.class,
            method = "onAttacked"
    )
    public static class BootNoThorns {
        public static SpireReturn<Integer> Prefix(ThornsPower __instance, DamageInfo info, int damageAmount) {
            if (__instance.owner != AbstractDungeon.player && AbstractDungeon.player.hasRelic(Boot.ID)) {
                AbstractDungeon.player.getRelic(Boot.ID).flash();
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SharpHidePower.class,
            method = "onUseCard"
    )
    public static class BootNoSharpHide {
        public static SpireReturn Prefix(SharpHidePower __instance, AbstractCard c, UseCardAction action) {
            if (__instance.owner != AbstractDungeon.player && AbstractDungeon.player.hasRelic(Boot.ID) && c.type == AbstractCard.CardType.ATTACK) {
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
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("BlueCandleDesc"));

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
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("TinyHouseDesc"));

        public static SpireReturn Prefix(TinyHouse __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = TinyHouse.class,
            method = "onEquip"
    )
    public static class TinyHouseBuff {
        public static SpireReturn Prefix(TinyHouse __instance) {
            ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade())
                    upgradableCards.add(c);
            }
            Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
            if (!upgradableCards.isEmpty())
                if (upgradableCards.size() == 1) {
                    upgradableCards.get(0).upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                } else {
                    upgradableCards.get(0).upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy(), Settings.WIDTH / 3.0F, Settings.HEIGHT / 3.0F));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 3.0F, Settings.HEIGHT / 3.0F));
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(1).makeStatEquivalentCopy(), (Settings.WIDTH / 3.0F * 2f), (Settings.HEIGHT / 3.0F * 2f)));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((Settings.WIDTH / 3.0F * 2f), (Settings.HEIGHT / 3.0F * 2f)));
                }
            AbstractDungeon.player.increaseMaxHp(5, true);
            AbstractDungeon.getCurrRoom().addGoldToRewards(50);
            AbstractDungeon.getCurrRoom().addPotionToRewards(PotionHelper.getRandomPotion(AbstractDungeon.miscRng));
            AbstractDungeon.combatRewardScreen.open(__instance.DESCRIPTIONS[3]);
            (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.0F;
            return SpireReturn.Return(null);
        }
    }


    @SpirePatch(clz = MapGenerator.class, method = "generateDungeon")
    public static class CursedCompassPatch {
        public static void Prefix(int height, int width, @ByRef int[] pathDensity, com.megacrit.cardcrawl.random.Random rng) {
            if (AbstractDungeon.player.hasRelic(CursedCompass.ID))
                pathDensity[0] = 1;
        }
    }

    @SpirePatch(
            clz = NilrysCodex.class,
            method = "getUpdatedDescription"
    )
    public static class NilrysCodexDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("NilrysDesc"));

        public static SpireReturn Prefix(NilrysCodex __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = NilrysCodex.class,
            method = "onPlayerEndTurn"
    )
    public static class NewNilrysEffect {
        public static SpireReturn Prefix(NilrysCodex __instance) {
            if (AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().anyMatch(q -> q.rarity.equals(AbstractCard.CardRarity.RARE))) {
                __instance.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, __instance));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = Cauldron.class,
            method = "getUpdatedDescription"
    )
    public static class CauldronDescription {
        private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CauldronDesc"));

        public static SpireReturn Prefix(Cauldron __instance) {
            return SpireReturn.Return(uiStrings.TEXT[0]);
        }
    }

    @SpirePatch(
            clz = Cauldron.class,
            method = "onEquip"
    )
    public static class CauldronBonusPotionSlot {
        public static void Prefix(Cauldron __instance) {
            AbstractDungeon.player.potionSlots += 1;
            AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - 1));
        }
    }
}
