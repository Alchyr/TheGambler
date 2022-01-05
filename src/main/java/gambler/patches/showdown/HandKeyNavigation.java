package gambler.patches.showdown;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gambler.PokerHand;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "updateFullKeyboardCardSelection"
)
public class HandKeyNavigation {
    //using handsize index for poker hand card
    @SpireInsertPatch(
            rloc = 23
    )
    public static SpireReturn<Boolean> noSelectedLeft(AbstractPlayer __instance, @ByRef int[] ___keyboardCardIndex) {
        if (PokerHand.showdown != null) {
            ___keyboardCardIndex[0] = -1;
            return SpireReturn.Return(false);
        }
        return SpireReturn.Continue();
    }

    @SpireInsertPatch(
            rloc = 39
    )
    public static SpireReturn<Boolean> movingAround(AbstractPlayer __instance, @ByRef int[] ___keyboardCardIndex) {
        if (PokerHand.showdown != null) {
            if (___keyboardCardIndex[0] == -1 || ___keyboardCardIndex[0] == __instance.hand.size()) {
                ___keyboardCardIndex[0] = -1;


                if (!AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.topPanel.potionUi.targetMode) {
                    if (PokerHand.showdown != __instance.hoveredCard && Math.abs(PokerHand.showdown.current_x - PokerHand.showdown.target_x) < 400.0F * Settings.scale) {
                        __instance.toHover = PokerHand.showdown;
                        if (Settings.isControllerMode && AbstractDungeon.actionManager.turnHasEnded) {
                            __instance.toHover = null;
                        }

                        if (!__instance.inspectMode) {
                            Gdx.input.setCursorPosition((int)PokerHand.showdown.hb.cX, (int)PokerHand.showdown.hb.cY);
                        }
                        return SpireReturn.Return(true);
                    }
                }


                return SpireReturn.Return(false);
            }
            else if (___keyboardCardIndex[0] < -1) {
                ___keyboardCardIndex[0] = -1;
            }
        }
        return SpireReturn.Continue();
    }
}
