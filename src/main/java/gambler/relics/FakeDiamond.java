package gambler.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static gambler.GamblerMod.makeID;

public class FakeDiamond extends BaseRelic implements ClickableRelic {
    public static final String ID = makeID("FakeDiamond");
    private static final String IMG = "FakeDiamond";

    private AbstractRoom lastRoom = null;

    private boolean activated = false;

    public FakeDiamond()
    {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);

        setCounter(200);
    }

    public boolean canSpawn() {
        return AbstractDungeon.floorNum < 48 || Settings.isEndless;
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);

        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FakeDiamond();
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (lastRoom instanceof ShopRoom) {
            this.flash();

            setCounter(counter + 50);
        }
        lastRoom = room;
    }

    @Override
    public void update() {
        super.update();
        if (lastRoom == null) {
            MapRoomNode n = AbstractDungeon.currMapNode;
            if (n != null) {
                lastRoom = n.room;
            }
        }
    }

    @Override
    public void onRightClick() {
        if (lastRoom instanceof ShopRoom && !activated) {
            activated = true;
            AbstractDungeon.effectList.add(new AbstractGameEffect() {
                @Override
                public void update() {
                    AbstractDungeon.player.loseRelic(ID);
                    this.isDone = true;
                }

                @Override
                public void render(SpriteBatch spriteBatch) {
                }

                @Override
                public void dispose() {
                    FakeDiamond.this.activated = false;
                }
            });
            AbstractDungeon.player.gainGold(this.counter);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + counter + DESCRIPTIONS[1];
    }
}