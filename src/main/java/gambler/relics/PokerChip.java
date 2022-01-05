package gambler.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import gambler.rewards.DieReward;
import gambler.rewards.LinkedDieReward;

import static gambler.GamblerMod.makeID;

public class PokerChip extends BaseRelic {
    public static final String ID = makeID("PokerChip");
    private static final String IMG = "PokerChip";

    public PokerChip()
    {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public boolean canSpawn() {
        if (AbstractDungeon.floorNum >= 48 && !Settings.isEndless) {
            return false;
        }

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r.relicId.equals(FairDie.ID) || r.relicId.equals(LoadedDie.ID))
                return true;
        }
        return false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PokerChip();
    }

    //The reward cannot easily replace itself, so it hands the responsibility off to the relic.
    private DieReward reward = null;
    private AbstractRoom rewardRoom = null;
    public void prepReward(DieReward dieReward, AbstractRoom rewardRoom) {
        this.reward = dieReward;
        this.rewardRoom = rewardRoom;
    }


    @Override
    public void update() {
        super.update();
        if (reward != null) {
            MapRoomNode n = AbstractDungeon.currMapNode;
            if (n != null) {
                AbstractRoom currentRoom = n.getRoom();

                if (rewardRoom.equals(currentRoom)) { //Didn't magically leave another room in the single frame between rolling die and this update method
                    this.flash();

                    //Generate linked reward, one to claim original result, another to reroll
                    LinkedDieReward replace = new LinkedDieReward(reward);
                    LinkedDieReward reroll = new LinkedDieReward(replace);

                    AbstractDungeon.combatRewardScreen.rewards.add(0, reroll);
                    AbstractDungeon.combatRewardScreen.rewards.add(0, replace);
                    AbstractDungeon.combatRewardScreen.positionRewards();

                }
                //Clear variables regardless
                this.reward = null;
                this.rewardRoom = null;
            }
        }
        else if (rewardRoom != null)
            rewardRoom = null;
    }
}