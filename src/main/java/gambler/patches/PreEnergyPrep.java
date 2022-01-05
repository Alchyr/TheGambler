package gambler.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import gambler.relics.LoadedDie;

@SpirePatch(
        clz = EnergyManager.class,
        method = "prep"
)
public class PreEnergyPrep {
    @SpirePrefixPatch
    public static void prePrep(EnergyManager __instance) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof LoadedDie) {
                ((LoadedDie) r).beforeEnergyPrep();
            }
        }
    }
}
