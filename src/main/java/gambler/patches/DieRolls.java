/*package gambler.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import gambler.relics.FairDie;
import gambler.relics.LoadedDie;
import gambler.rewards.DieReward;
import javassist.CtBehavior;


//Handled through onVictory hook and BaseMod reward save now. Easier than figuring out when a combat happened and when one didn't.

@SpirePatch(
        clz = CombatRewardScreen.class,
        method = "setupItemReward",
        paramtypez = {}
)
public class DieRolls {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void modifyRewards(CombatRewardScreen __instance)
    {
        //The giant if statement used by basegame to determine whether or not there's a card reward (combat)
        if ((AbstractDungeon.getCurrRoom().event == null || AbstractDungeon.getCurrRoom().event != null && !AbstractDungeon.getCurrRoom().event.noCardsInRewards) && !(AbstractDungeon.getCurrRoom() instanceof TreasureRoom) && !(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
            if (AbstractDungeon.player.hasRelic(FairDie.ID)) {
                __instance.rewards.add(0, new DieReward(false));
            }
            if (AbstractDungeon.player.hasRelic(LoadedDie.ID)) {
                __instance.rewards.add(0, new DieReward(true));
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}*/
