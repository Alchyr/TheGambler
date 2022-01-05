package gambler.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MathHelper {
    public static float dist(float a, float b)
    {
        return Math.abs(a-b);
    }
    public static float dist(float x1, float x2, float y1, float y2)
    {
        return (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static float angle(float x1, float x2, float y1, float y2)
    {
        return MathUtils.atan2(y2 - y1, x2 - x1) * 180.0f / MathUtils.PI;
    }

    //rotate vector p around cx, cy by angle in degrees
    public static Vector2 rotatePoint(Vector2 p, float cx, float cy, float angle)
    {
        p.x -= cx;
        p.y -= cy;

        rotatePoint(p, angle);

        p.x += cx;
        p.y += cy;

        return p;
    }

    //rotate vector p around 0, 0 by angle in degrees
    public static Vector2 rotatePoint(Vector2 p, float angle)
    {
        float s = MathUtils.sin(MathUtils.degreesToRadians * angle);
        float c = MathUtils.cos(MathUtils.degreesToRadians * angle);

        p.x = p.x * c - p.y * s;
        p.y = p.x * s + p.y * c;

        return p;
    }
}
