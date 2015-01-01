package com.noxalus.tunnelrush.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;

public class RightWall extends Wall
{
    private LeftWall leftWall;
    private RightWall previousRightWall;
    private RightWall nextRightWall;

    public RightWall(int id, GameData gameData, LeftWall leftWall, RightWall previousRightWall, RightWall nextRightWall)
    {
        super(id, gameData);

        this.leftWall = leftWall;
        this.previousRightWall = previousRightWall;
        this.nextRightWall = nextRightWall;

        setRandomSpriteSize();

        sprite.setPosition(Config.GameWidth - (sprite.getWidth() + Config.WallStep) + outScreenSpace, (id - 3) * Config.WallHeight);

        Initialize();
    }

    public void setPreviousRightWall(RightWall value)
    {
        this.previousRightWall = value;
    }

    public void setNextRightWall(RightWall value)
    {
        this.nextRightWall = value;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if (sprite.getY() >= (Config.GameHeight + Config.WallHeight))
        {
            sprite.setX(Config.GameWidth - (sprite.getWidth() + Config.WallStep) + outScreenSpace);
        }

        border.setPosition(sprite.getX(), sprite.getY());

        joinBorder.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight());

        if (id == Config.MaxWallNumber + 2)
            borderBottom.setPosition(Config.GameWidth - sprite.getWidth() - Config.WallStep + outScreenSpace,
                    sprite.getY() + sprite.getHeight());

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
            float leftWallWidth = leftWall.sprite.getWidth() - outScreenSpace;
            int maxWidth = (int) (Config.GameWidth - leftWallWidth - wallDistance - Config.WallBorderWidth);
            width = nextRightWall.sprite.getWidth() - outScreenSpace;

            if (gameData.tunnelPatterns == GameData.TunnelPatterns.RIGHT || gameData.tunnelPatterns == GameData.TunnelPatterns.INCREASE)
            {
                factor = -1;
                width += Config.WallStep * factor;
            }
            else if (gameData.tunnelPatterns == GameData.TunnelPatterns.LEFT || gameData.tunnelPatterns == GameData.TunnelPatterns.DECREASE)
            {
                factor = 1;
                width += Config.WallStep * factor;
            }

            //width += Config.WallStep * factor /* * Random(1, 5)*/;
            width = MathUtils.clamp(width, Config.WallBorderWidth, maxWidth);

            // Join border
            float difference = 0;
            joinBorder.setX(Config.GameWidth - (width + Config.WallBorderWidth));
            float previousWallWidth = nextRightWall.sprite.getWidth() - outScreenSpace;
            if (previousWallWidth < width)
            {
                difference = previousWallWidth - width;
            }
            else
            {
                difference = previousWallWidth - width + Config.WallStep;
                joinBorder.setX(joinBorder.getX() - difference + Config.WallStep);
            }

            joinBorder.setColor(Color.valueOf("000000"));
            joinBorder.setColor(joinBorder.getColor().r, joinBorder.getColor().g, joinBorder.getColor().b, 1.f);

            joinBorder.setSize(Math.abs(difference), Config.WallBorderWidth);
        }
        else
        {
            if (id > 0)
            {
                width = (previousRightWall.sprite.getWidth() - outScreenSpace);

                if (width <= Config.MinWallWidth)
                    factor = -1;

                //width += Config.WallStep * factor;
            }
            else
            {
                width = Config.GameWidth - (leftWall.sprite.getWidth() - outScreenSpace) - wallDistance;
            }
        }

        return width;
    }
}
