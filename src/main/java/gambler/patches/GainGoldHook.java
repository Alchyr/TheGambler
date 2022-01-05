package gambler.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import gambler.powers.interfaces.GoldChangePower;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "gainGold",
        paramtypez = { int.class }
)
public class GainGoldHook {
    @SpireInsertPatch(
            rloc=9
    )
    public static void onGainGold(AbstractPlayer __instance, int amount) {
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            for (AbstractPower p : __instance.powers) {
                if (p instanceof GoldChangePower) {
                    ((GoldChangePower) p).onGoldChange(amount);
                }
            }
        }
    }
}
