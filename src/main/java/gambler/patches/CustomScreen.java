package gambler.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import gambler.PokerHand;
import gambler.screen.PokerHandChangeScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class CustomScreen {
    @SpireEnum
    public static AbstractDungeon.CurrentScreen POKER_HAND_CHANGE;

    public static void refreshPokerHand() {
        for (AbstractCard c : PokerHand.cards)
            c.targetDrawScale = PokerHand.SMALL_SCALE;
        PokerHand.updatePositions();
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "update"
    )
    public static class Update {
        @SpireInstrumentPatch
        public static ExprEditor e() {
            return new ExprEditor() {
                boolean first = true;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (first && m.getMethodName().equals("name") && m.getClassName().equals(AbstractDungeon.CurrentScreen.class.getName())) {
                        first = false;
                        m.replace("if (screen == " + CustomScreen.class.getName() + ".POKER_HAND_CHANGE) {" +
                                PokerHandChangeScreen.class.getName() + ".screen.update();" +
                                "}" +
                                "$_ = $proceed($$);");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class Render {
        @SpireInstrumentPatch
        public static ExprEditor e() {
            return new ExprEditor() {
                boolean first = true;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (first && m.getMethodName().equals("name") && m.getClassName().equals(AbstractDungeon.CurrentScreen.class.getName())) {
                        first = false;
                        m.replace("if (screen == " + CustomScreen.class.getName() + ".POKER_HAND_CHANGE) {" +
                                PokerHandChangeScreen.class.getName() + ".screen.render(sb);" +
                                "}" +
                                "$_ = $proceed($$);");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "closeCurrentScreen"
    )
    public static class CloseCurrentScreen {
        @SpireInstrumentPatch
        public static ExprEditor e() {
            return new ExprEditor() {
                boolean first = true;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (first && m.getMethodName().equals("info")) {
                        first = false;
                        m.replace("if (screen == " + CustomScreen.class.getName() + ".POKER_HAND_CHANGE) {" +
                                "genericScreenOverlayReset();" +
                                CustomScreen.class.getName() + ".refreshPokerHand();" +
                                "}" +
                                "else { $_ = $proceed($$); }");
                    }
                }
            };
        }
    }


    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "openPreviousScreen"
    )
    public static class OpenPreviousScreen {
        @SpirePrefixPatch
        public static void h(AbstractDungeon.CurrentScreen s) {
            if (s == POKER_HAND_CHANGE) {
                AbstractDungeon.overlayMenu.hideBlackScreen();
                PokerHandChangeScreen.screen.reopen();
            }
        }
    }
}
