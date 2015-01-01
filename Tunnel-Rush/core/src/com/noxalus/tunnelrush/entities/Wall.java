package com.noxalus.tunnelrush.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.noxalus.tunnelrush.Assets;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import com.noxalus.tunnelrush.screens.GameScreen;
import com.noxalus.tunnelrush.GameData.TunnelPatternType;

public class Wall implements DrawableGameEntity
{
    protected int id;
    protected boolean hasReset;
    protected Sprite sprite;
    protected int outScreenSpace;
    protected Sprite border;
    protected Sprite borderBottom;
    protected Sprite joinBorder;

    // References
    protected GameData gameData;

	private float velocity;
	private Rectangle boundingBox;

	private int currentFactor;
	
	private boolean increaseWallDistance;
	
	public Wall(int id, GameData gameData)
	{
		// References
		this.gameData = gameData;
		
		this.id = id;
		
		this.boundingBox = new Rectangle();
		this.velocity = Config.InitialWallSpeed;
		this.hasReset = false;

		// Test border
		this.joinBorder = new Sprite(Assets.pixelWallJoinBorder);
		joinBorder.setColor(Color.valueOf("FF0000"));

		this.sprite = new Sprite(Assets.pixelWall);
		this.outScreenSpace = (Config.GameHeight - Config.GameWidth);
	}

    public void Initialize()
    {
        if (!gameData.isDemo)
            sprite.setY(sprite.getY() - (2 * Config.GameHeight));

        sprite.setColor(Config.WallColors[0]);

        if (gameData.isDemo)
            sprite.setColor(Config.WallColorsDebug[(int) Math.round(Math.random() * (Config.WallColorsDebug.length - 1))]);

        border = new Sprite(Assets.pixelWalBorder);
        border.setSize(Config.WallBorderWidth, Config.WallHeight);
        border.setColor(Color.valueOf("000000"));
        //border.setAlpha(0.5f);

        if (id == Config.MaxWallNumber + 2)
        {
            borderBottom = new Sprite(Assets.pixelWallBorderBottom);
            borderBottom.setSize(sprite.getWidth() + Config.WallBorderWidth - Config.WallStep, Config.WallStep);
            borderBottom.setColor(Color.valueOf("000000"));
        }

        currentFactor = 1;
        increaseWallDistance = false;
    }

    protected void setRandomSpriteSize()
    {
        float width = randomWallWidth();
        sprite.setSize(width + outScreenSpace, Config.WallHeight);
    }

	@Override
	public void Update(float delta)
	{
		// Wall speed according to difficulty
		if (!gameData.isDemo && (gameData.difficulty < Config.MaxWallSpeedIntervals.length && velocity < Config.MaxWallSpeedIntervals[gameData.difficulty]))
		{
			velocity += (Config.WallSpeedStep * delta);
		}

		sprite.setY(sprite.getY() + velocity * delta);

		// Reset wall
		if (sprite.getY() >= (Config.GameHeight + Config.WallHeight))
		{
			hasReset = true;

			if (!gameData.isDemo)
				sprite.setColor(Config.WallColors[gameData.difficulty % (Config.WallColors.length - 1)]);

			sprite.setY(sprite.getY() - Config.GameHeight - (Config.WallHeight * 3));

			sprite.setSize(randomWallWidth() + outScreenSpace, Config.WallHeight);

			if (!gameData.isDemo)
				gameData.score++;
		}
	}

	@Override
	public void Draw(SpriteBatch spriteBatch, float delta)
	{
		sprite.draw(spriteBatch);
		border.draw(spriteBatch);

		if (!hasReset && id == Config.MaxWallNumber + 2)
			borderBottom.draw(spriteBatch);

		joinBorder.draw(spriteBatch);
	}

	public boolean Intersects(Rectangle boundingBox)
    {
		return this.boundingBox.overlaps(boundingBox);
	}

	protected float randomWallWidth()
	{
        return randomWallWidth();
	}

	protected void UpdateBoundingBox()
	{
        if (!gameData.isDemo)
            boundingBox.set(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
	}

	private int Random(int min, int max)
	{
		return (min + Config.random.nextInt(max - min + 1));
	}
}
