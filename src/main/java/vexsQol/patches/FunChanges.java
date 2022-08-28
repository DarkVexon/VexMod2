package vexsQol.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.InkBottle;

import java.nio.charset.StandardCharsets;

public class FunChanges {
    @SpirePatch(
            clz = AbstractRelic.class,
            method = "renderInTopPanel"
    )
    public static class ColorfulInkBottle {
        private static ShaderProgram oldShader;
        private static final ShaderProgram COLORSHIFT_SHADER = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal("vexsqolResources/shaders/inkbottle.frag").readString(String.valueOf(StandardCharsets.UTF_8)));

        public static float SHIFT_AMT = 0.0F;

        public static void Prefix(AbstractRelic __instance, SpriteBatch sb) {
            if (__instance instanceof InkBottle) {
                oldShader = sb.getShader();
                sb.setShader(COLORSHIFT_SHADER);
                COLORSHIFT_SHADER.setUniformf("shift_amt", SHIFT_AMT);
            }
        }

        public static void Postfix(AbstractRelic __instance, SpriteBatch sb) {
            if (__instance instanceof InkBottle) {
                sb.setShader(oldShader);
            }
        }
    }

    @SpirePatch(
            clz = InkBottle.class,
            method = "onUseCard"
    )
    public static class InkBottleShiftColor {
        public static void Prefix(InkBottle __instance, AbstractCard card, UseCardAction action) {
            if (__instance.counter == 9) {
                ColorfulInkBottle.SHIFT_AMT += 0.1F;
            }
        }
    }
}
