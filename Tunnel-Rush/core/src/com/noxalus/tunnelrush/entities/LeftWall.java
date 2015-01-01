package com.noxalus.tunnelrush.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import java.util.ArrayList;

public class LeftWall extends Wall
{
    private LeftWall previousLeftWall;
    private LeftWall nextLeftWall;

    public LeftWall(int id, GameData gameData, LeftWall previousLeftWall, LeftWall nextLeftWall)
    {
        super(id, gameData);

        this.previousLeftWall = previousLeftWall;
        this.nextLeftWall = nextLeftWall;

        setRandomSpriteSize();

        sprite.setPosition(-outScreenSpace, (id - 3) * Config.WallHeight);

        Initialize();
    }

    public void setPreviousLeftWall(LeftWall value)
    {
        this.previousLeftWall = value;
    }

    public void setNextLeftWall(LeftWall value)
    {
        this.nextLeftWall = value;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        border.setPosition(sprite.getX() + sprite.getWidth() - Config.WallBorderWidth, sprite.getY());

        joinBorder.setY(sprite.getY() + sprite.getHeight());

        if (id == Config.MaxWallNumber + 2)
            borderBottom.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight());

        UpdateBoundingBox();
    }

    @Override
    public void Draw(SpriteBatch spriteBatch, float delta)
    {
        super.Draw(spriteBatch, delta);
    }

    @Override
    protected float randomWallWidth()
    {
        float width = 0;

        // -1 => tunnel go to the left, 1 => tunnel go to the right
        int factor = (Config.random.nextFloat() > 0.5f) ? 1 : -1;

        /*
		if (increaseWallDistance)
		{
			if (gameData.wallDistance >= Config.MaxWallDistance)
				increaseWallDistance = false;
			else
				gameData.wallDistance++;
		}
		else
		{
			if (gameData.wallDistance <= Config.MinWallDistance)
				increaseWallDistance = true;
			else
				gameData.wallDistance--;
		}
		*/

        float wallDistance = gameData.wallDistance;

        if (hasReset)
        {
            width = nextLeftWall.sprite.getWidth() - outScreenSpace;
            int maxWidth = (int) (Config.GameWidth - wallDistance - Config.WallBorderWidth);

            if (gameData.tunnelPattern == GameData.TunnelPatternType.RIGHT || gameData.tunnelPattern == GameData.TunnelPatternType.DECREASE) {
                factor = 1;
                width += Config.WallStep * factor;
            } else if (gameData.tunnelPattern == GameData.TunnelPatternType.LEFT || gameData.tunnelPattern == GameData.TunnelPatternType.INCREASE) {
                factor = -1;
                width += Config.WallStep * factor;
            }

            //width += Config.WallStep * factor /* * Random(1, 5)*/;
            width = MathUtils.clamp(width, Config.WallBorderWidth * 2, maxWidth);

            // Join border
            float difference = 0;
            joinBorder.setX(sprite.getX() + outScreenSpace + width - Config.WallBorderWidth);
            float previousWallWidth = nextLeftWall.sprite.getWidth() - outScreenSpace;
            if (previousWallWidth < width)
            {
                difference = previousWallWidth - width;
                joinBorder.setX(joinBorder.getX() + difference + Config.WallStep);
            }
            else
            {
                difference = previousWallWidth - width + Config.WallStep;
            }

            joinBorder.setColor(Color.valueOf("000000"));
            joinBorder.setColor(joinBorder.getColor().r, joinBorder.getColor().g, joinBorder.getColor().b, 1.f);

            joinBorder.setSize(Math.abs(difference), Config.WallBorderWidth);
        }
        else
        {
            if (id > 0)
            {
                width = (previousLeftWall.sprite.getWidth() - outScreenSpace);

                if (width + wallDistance > Config.GameWidth - Config.WallStep)
                    factor = -1;

                //width += Config.WallStep * factor;
            }
            else
            {
                width = MathUtils.clamp(Config.random.nextInt(Config.MaxInitialWallWidth), Config.WallStep, Config.MaxInitialWallWidth);
                width = Config.MaxInitialWallWidth;
            }
        }

        return width;
    }
}
