package vexsQol.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EventChanges {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "isNoteForYourselfAvailable"
    )
    public static class NoMoreNote {
        public static boolean Postfix(boolean __result, AbstractDungeon __instance) {
            return false;
        }
    }
}
