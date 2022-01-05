package gambler.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import gambler.actions.generic.ChangeGoldAction;

import static gambler.GamblerMod.makeID;

public class PotionSeller extends BaseRelic {
    public static final String ID = makeID("PotionSeller");
    private static final String IMG = "PotionSeller";

    public PotionSeller()
    {
        super(ID, IMG, RelicTier.COMMON, LandingSound.CLINK);
    }

    public boolean canSpawn() {
        return AbstractDungeon.floorNum < 48 || Settings.isEndless;
    }

    public void sellPotion(AbstractPotion p) {
        if (!(AbstractDungeon.getCurrRoom() instanceof ShopRoom)) {
            this.flash();
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                addToBot(new ChangeGoldAction(70, hb));
            } else {
                AbstractDungeon.player.gainGold(70);

                for(int i = 0; i < 70; ++i) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, hb.cX, hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                }
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PotionSeller();
    }
}