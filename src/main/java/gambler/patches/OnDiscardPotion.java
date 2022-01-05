package gambler.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import gambler.relics.PotionSeller;
import javassist.CtBehavior;

@SpirePatch(
        clz = PotionPopUp.class,
        method = "updateInput"
)
public class OnDiscardPotion {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void onDiscard(PotionPopUp __instance, AbstractPotion ___potion) {
        AbstractRelic r = AbstractDungeon.player.getRelic(PotionSeller.ID);
        if (r instanceof PotionSeller) {
            ((PotionSeller) r).sellPotion(___potion);
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
            return new int[] { LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1] };
        }
    }
}
