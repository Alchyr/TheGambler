package gambler.patches.showdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import gambler.PokerHand;

@SpirePatch(
        clz = InputHelper.class,
        method = "getCardSelectedByHotkey"
)
public class HandHotkey {
    @SpirePrefixPatch
    public static SpireReturn<AbstractCard> pokerHand(CardGroup c) {
        if (PokerHand.showdown != null && c.equals(AbstractDungeon.player.hand) && !AbstractDungeon.isScreenUp && (
                Gdx.input.isKeyJustPressed(Input.Keys.Q)
                )) {
            return SpireReturn.Return(PokerHand.showdown);
        }
        return SpireReturn.Continue();
    }
}
