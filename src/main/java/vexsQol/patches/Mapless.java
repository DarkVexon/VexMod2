package vexsQol.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.vfx.MapCircleEffect;

import java.util.ArrayList;

public class Mapless {
    @SpirePatch(clz = MapGenerator.class, method = "generateDungeon")
    public static class MapWidthOne {
        public static void Prefix(int height, int width, @ByRef int[] pathDensity, com.megacrit.cardcrawl.random.Random rng) {
            pathDensity[0] = 1;
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "updateMapButtonLogic")
    public static class NoMoreButton {
        public static SpireReturn Prefix(TopPanel panel) {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "renderMapIcon")
    public static class DontShowButton {
        public static SpireReturn Prefix(TopPanel panel) {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = DungeonMapScreen.class, method = "open")
    public static class InstantlyEnd {
        private static ReflectionHacks.RMethod soundMethod = null;
        private static final int IMG_WIDTH;
        public static final float OFFSET_X;
        private static final float OFFSET_Y;
        private static final float SPACING_X;

        public static void Postfix(DungeonMapScreen __instance, boolean doScroll) {
            ArrayList<MapRoomNode> nodesList = new ArrayList<>();
            for (ArrayList<MapRoomNode> row : AbstractDungeon.map) {
                for (MapRoomNode node : row) {
                    if (!AbstractDungeon.firstRoomChosen) {
                        if (node.y == 0 && node.hasEdges()) {
                            nodesList.add((node));
                        }
                    } else if (AbstractDungeon.getCurrMapNode().isConnectedTo(node)) {
                        nodesList.add(node);
                    }
                }
            }
            if (!nodesList.isEmpty()) {
                MapRoomNode next = nodesList.get(0);
                if (soundMethod == null)
                    soundMethod = ReflectionHacks.privateMethod(MapRoomNode.class, "playNodeSelectedSound");
                soundMethod.invoke(next);
                AbstractDungeon.topLevelEffects.add(new MapCircleEffect((float) next.x * SPACING_X + OFFSET_X + next.offsetX, (float) next.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + next.offsetY, MathUtils.random(360.0F)));
                AbstractDungeon.getCurrMapNode().taken = true;
                MapEdge connectedEdge = AbstractDungeon.getCurrMapNode().getEdgeConnectedTo(next);
                if (connectedEdge != null) {
                    connectedEdge.markAsTaken();
                }
                AbstractDungeon.nextRoom = next;
                AbstractDungeon.pathX.add(next.x);
                AbstractDungeon.pathY.add(next.y);
                if (!AbstractDungeon.firstRoomChosen) {
                    AbstractDungeon.firstRoomChosen = true;
                }
                if (!AbstractDungeon.isDungeonBeaten) {
                    AbstractDungeon.nextRoomTransitionStart();
                    CardCrawlGame.music.fadeOutTempBGM();
                }
            } else if (AbstractDungeon.getCurrMapNode().y == 14 || (AbstractDungeon.id.equals("TheEnding") && AbstractDungeon.getCurrMapNode().y == 2)) {
                AbstractDungeon.getCurrMapNode().taken = true;
                MapRoomNode node2 = AbstractDungeon.getCurrMapNode();
                for (MapEdge e : node2.getEdges()) {
                    if (e != null) {
                        e.markAsTaken();
                    }
                }
                MapRoomNode node = new MapRoomNode(-1, 15);
                node.room = new MonsterRoomBoss();
                AbstractDungeon.nextRoom = node;
                if (AbstractDungeon.pathY.size() > 1) {
                    AbstractDungeon.pathX.add(AbstractDungeon.pathX.get(AbstractDungeon.pathX.size() - 1));
                    AbstractDungeon.pathY.add((Integer) AbstractDungeon.pathY.get(AbstractDungeon.pathY.size() - 1) + 1);
                } else {
                    AbstractDungeon.pathX.add(1);
                    AbstractDungeon.pathY.add(15);
                }

                AbstractDungeon.nextRoomTransitionStart();
            }
        }

        static {
            IMG_WIDTH = (int) (Settings.xScale * 64.0F);
            OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
            OFFSET_Y = 180.0F * Settings.scale;
            SPACING_X = Settings.isMobile ? (float) IMG_WIDTH * 2.2F : (float) IMG_WIDTH * 2.0F;
        }
    }

    @SpirePatch(clz = DungeonMapScreen.class, method = "render")
    public static class DontShow {
        public static SpireReturn Prefix(DungeonMapScreen __instance, SpriteBatch sb) {
            return SpireReturn.Return(null);
        }
    }
}
