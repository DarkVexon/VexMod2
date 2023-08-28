package vexsQol.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.map.MapGenerator;

public class RelicChanges {
    @SpirePatch(clz = MapGenerator.class, method = "generateDungeon")
    public static class MapWidthOne {
        public static void Prefix(int height, int width, @ByRef int[] pathDensity, com.megacrit.cardcrawl.random.Random rng) {
            pathDensity[0] = 1;
        }
    }


}
