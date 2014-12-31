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

public class Wall implements DrawableGameEntity
{
	// References
	private GameData gameData;
	
	private int id;
	private float velocity;
	private ArrayList<Wall> leftWalls;
	private ArrayList<Wall> rightWalls;
	private boolean isLeftWall;
	private Rectangle boundingBox;
	private boolean hasReset;
	private int outScreenSpace;
	
	private Sprite sprite;
	private Sprite border;
	private Sprite borderBottom;
	private Sprite joinBorder;

	private int currentFactor;
	
	private boolean increaseWallDistance;
	
	public Wall(int id, boolean isLeftWall, ArrayList<Wall> leftWalls, ArrayList<Wall> rightWalls, GameData gameData)
	{
		// References
		this.gameData = gameData;
		
		this.id = id;
		this.leftWalls = leftWalls;
		this.rightWalls = rightWalls;
		this.isLeftWall = isLeftWall;
		
		this.boundingBox = new Rectangle();
		this.velocity = Config.InitialWallSpeed;
		this.hasReset = false;

		// Test border
		this.joinBorder = new Sprite(Assets.pixelWallJoinBorder);
		joinBorder.setColor(Color.valueOf("FF0000"));

		this.sprite = new Sprite(Assets.pixelWall);
		
		this.outScreenSpace = (Config.GameHeight - Config.GameWidth);

		float width = randomWallWidth();

		sprite.setSize(width + outScreenSpace, Config.WallHeight);

		if (isLeftWall)
			sprite.setPosition(-outScreenSpace, (id - 3) * Config.WallHeight);
		else
			sprite.setPosition(Config.GameWidth - (sprite.getWidth() + Config.WallStep) + outScreenSpace, (id - 3) * Config.WallHeight);

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
			borderBottom.setSize(sprite.getWidth() + Config.WallBorderWidth, Config.WallStep);
			borderBottom.setColor(Color.valueOf("000000"));
		}
		
		currentFactor = 1;
		increaseWallDistance = false;
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

			if (!isLeftWall)
			{
				sprite.setX(Config.GameWidth - (sprite.getWidth() + Config.WallStep) + outScreenSpace);
			}

			if (!gameData.isDemo)
				gameData.score++;
		}

		if (isLeftWall)
		{
			border.setPosition(sprite.getX() + sprite.getWidth() - Config.WallBorderWidth, sprite.getY());

			joinBorder.setY(sprite.getY() + sprite.getHeight());

			if (id == Config.MaxWallNumber + 2)
				borderBottom.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight());
		}
		else
		{
			border.setPosition(sprite.getX(), sprite.getY());
			
			joinBorder.setY(sprite.getY() + sprite.getHeight());
			//joinBorder.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight());
			
			if (id == Config.MaxWallNumber + 2)
				borderBottom.setPosition(Config.GameWidth - sprite.getWidth() - Config.WallStep + outScreenSpace,
						sprite.getY() + sprite.getHeight());
		}

		if (!gameData.isDemo)
			UpdateBoundingBox();
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

	private float randomWallWidth()
	{
		float width = 0;
		
		// -1 => tunnel go to the left, 1 => tunnel go to the right
		int factor = (Config.random.nextFloat() > 0.5f) ? 1 : -1;
		//factor = currentFactor;

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
		
		float wallDistance = gameData.wallDistance;

		if (hasReset)
		{
			if (isLeftWall)
			{
				width = (leftWalls.get((id + 1) % leftWalls.size()).sprite.getWidth() - outScreenSpace);
				int maxWidth = (int) (Config.GameWidth - wallDistance - Config.WallBorderWidth);
				/*
				if (width + wallDistance > Config.GameWidth - Config.WallStep)
					factor = -1;
				*/

                /*
				if (width <= Config.WallBorderWidth * Config.MaxWallNumber)
					tunnel.leftWallDirection = 1;
				else if (width >= maxWidth)
					tunnel.leftWallDirection = -1;
				*/

				width += Config.WallStep * factor /* * tunnel.leftWallDirection * Random(1, 5)*/;
				
				width = MathUtils.clamp(width, 0, maxWidth);
			}
			else
			{
				float leftWallWidth = leftWalls.get(id).sprite.getWidth() - outScreenSpace;
				int maxWidth = (int) (Config.GameWidth - leftWallWidth - wallDistance - Config.WallBorderWidth);
				width = (rightWalls.get((id + 1) % rightWalls.size()).sprite.getWidth() - outScreenSpace);

//				if (width <= Config.MinWallWidth)
//					factor = 1;
				
				//tunnel.rightWallDirection = increaseWallDistance ? 1 : -1;
                width += Config.WallStep * factor /* * tunnel.rightWallDirection * Random(1, 5)*/;
				
				//width = MathUtils.clamp(width, 0, maxWidth);
				//width = maxWidth;
			}
		}
		else
		{
			if (isLeftWall)
			{
				if (id > 0)
				{
					width = (leftWalls.get(id - 1).sprite.getWidth() - outScreenSpace);
					
					if (width + wallDistance > Config.GameWidth - Config.WallStep)
						factor = -1;

					width += Config.WallStep * factor;
				}
				else
				{
					width = Config.random.nextInt(Config.MaxWallWidth);
					width = 0;
				}
			}
			else
			{
				if (id > 0)
				{
					width = (rightWalls.get(id - 1).sprite.getWidth() - outScreenSpace);
					
					if (width <= Config.MinWallWidth)
						factor = -1;

					width += Config.WallStep * factor;
				}
				else
				{
					width = Config.GameWidth - (leftWalls.get(0).sprite.getWidth() - outScreenSpace) - wallDistance;
				}
			}
		}

		//width = MathUtils.clamp(width, Config.MinWallWidth, Config.MaxWallWidth);

		/*
		if (id > 0)
		{
			float difference = 0;
			if (isLeftWall)
			{
				difference = (leftWalls.get((id - 1)).sprite.getWidth() - outScreenSpace) - width;
			}
			else
			{
				difference = (rightWalls.get((id - 1)).sprite.getWidth() - outScreenSpace) - width;
			}

			joinBorder.setSize(Math.abs(difference), Config.WallBorderWidth);
		}
		*/
		
		if (hasReset)
		{
			float difference = 0;
			if (isLeftWall)
			{
				joinBorder.setX(sprite.getX() + outScreenSpace + width - Config.WallBorderWidth);
				float previousWallWidth = leftWalls.get((id + 1) % leftWalls.size()).sprite.getWidth() - outScreenSpace;
				if (previousWallWidth < width)
				{
					difference = previousWallWidth - width;
					joinBorder.setX(joinBorder.getX() + difference + Config.WallStep);
				}
				else
				{
					difference = previousWallWidth - width + Config.WallStep;
				}
			}
			else
			{
				joinBorder.setX(Config.GameWidth - (width + Config.WallBorderWidth));
				float previousWallWidth = rightWalls.get((id + 1) % rightWalls.size()).sprite.getWidth() - outScreenSpace;
				if (previousWallWidth < width)
				{
					difference = previousWallWidth - width;
				}
				else
				{
					difference = previousWallWidth - width + Config.WallStep;
					joinBorder.setX(joinBorder.getX() - difference + Config.WallStep);
				}
			}
			
			joinBorder.setColor(Color.valueOf("000000"));
			joinBorder.setColor(joinBorder.getColor().r, joinBorder.getColor().g, joinBorder.getColor().b, 0.5f);

			joinBorder.setSize(Math.abs(difference), Config.WallBorderWidth);
		}

		return width;
	}

	private void UpdateBoundingBox()
	{
		boundingBox.set(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
	}

	private int Random(int min, int max)
	{
		return (min + Config.random.nextInt(max - min + 1));
	}
}
