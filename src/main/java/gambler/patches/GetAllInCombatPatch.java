package gambler.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import gambler.PokerHand;

import java.util.HashSet;
import java.util.UUID;

@SpirePatch(
        clz = GetAllInBattleInstances.class,
        method = "get"
)
public class GetAllInCombatPatch {
    @SpirePostfixPatch
    public static HashSet<AbstractCard> pokerHandCheck(HashSet<AbstractCard> __result, UUID uuid) {
        for (AbstractCard c : PokerHand.cards) {
            if (c.uuid.equals(uuid)) {
                __result.add(c);
            }
        }

        return __result;
    }
}
