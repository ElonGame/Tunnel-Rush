package com.noxalus.tunnelrush.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.screens.GameScreen;

public class Wall implements DrawableGameEntity
{

	// References
	private GameScreen gameScreen;

	private int id;
	public Sprite sprite;
	private Sprite border;
	private Sprite borderBottom;
	private Sprite joinBorder;
	private float velocity;
	private ArrayList<Wall> leftWalls;
	private ArrayList<Wall> rightWalls;
	private boolean isLeftWall;
	private Rectangle boundingBox;
	private boolean hasReset;
	private int outScreenSpace;

	public Wall(GameScreen gameScreen, int id, boolean isLeftWall, ArrayList<Wall> leftWalls, ArrayList<Wall> rightWalls)
	{
		this.gameScreen = gameScreen;
		this.id = id;
		this.leftWalls = leftWalls;
		this.rightWalls = rightWalls;
		this.isLeftWall = isLeftWall;
		
		this.boundingBox = new Rectangle();
		this.velocity = Config.InitialWallSpeed;
		this.hasReset = false;

		Texture texture = new Texture(Gdx.files.internal("data/graphics/pictures/pixel.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Test border
		this.joinBorder = new Sprite(texture);
		joinBorder.setColor(Color.valueOf("FF0000"));

		this.sprite = new Sprite(texture);
		
		this.outScreenSpace = (Config.GameHeight - Config.GameWidth);

		float width = randomWallWidth();

		sprite.setSize(width + outScreenSpace, Config.WallHeight);

		if (isLeftWall)
			sprite.setPosition(-outScreenSpace, (id - 3) * Config.WallHeight - (2 * Config.GameHeight));
		else
			sprite.setPosition(Config.GameWidth - (sprite.getWidth() + Config.WallStep) + outScreenSpace, (id - 3) * Config.WallHeight
					- (2 * Config.GameHeight));

		sprite.setColor(Config.WallColors[0]);
		sprite.setColor(Config.WallColorsDebug[id]);

		border = new Sprite(texture);
		border.setSize(Config.WallBorderWidth, Config.WallHeight);
		border.setColor(Config.WallColorsDebug[id]/*Color.valueOf("000000")*/);

		if (id == Config.MaxWallNumber + 2)
		{
			borderBottom = new Sprite(texture);
			borderBottom.setSize(sprite.getWidth() + Config.WallBorderWidth, Config.WallStep);
			borderBottom.setColor(Color.valueOf("000000"));
		}
	}

	@Override
	public void Update(float delta)
	{
		// Wall speed according to difficulty
		if (gameScreen.difficulty < Config.MaxWallSpeedIntervals.length && velocity < Config.MaxWallSpeedIntervals[gameScreen.difficulty])
		{
			velocity += Config.WallSpeedStep * delta;
		}

		sprite.setY(sprite.getY() + (velocity * delta));

		// Reset wall
		if (sprite.getY() >= (Config.GameHeight + Config.WallHeight))
		{
			hasReset = true;

			//sprite.setColor(Config.WallColors[gameScreen.difficulty % (Config.WallColors.length - 1)]);

			sprite.setY(sprite.getY() - Config.GameHeight - (Config.WallHeight * 3));

			sprite.setSize(randomWallWidth() + outScreenSpace, Config.WallHeight);

			if (!isLeftWall)
			{
				sprite.setX(Config.GameWidth - (sprite.getWidth() + Config.WallStep) + outScreenSpace);
			}

			gameScreen.score++;
		}

		if (isLeftWall)
		{
			border.setPosition(sprite.getX() + sprite.getWidth(), sprite.getY());

			joinBorder.setY(sprite.getY() + sprite.getHeight());

			if (id == Config.MaxWallNumber + 2)
				borderBottom.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight());
		}
		else
		{
			border.setPosition(sprite.getX(), sprite.getY());
			
			joinBorder.setY(sprite.getY() + sprite.getHeight());
			
			if (id == Config.MaxWallNumber + 2)
				borderBottom.setPosition(Config.GameWidth - sprite.getWidth() - Config.WallStep + outScreenSpace,
						sprite.getY() + sprite.getHeight());
		}

		UpdateBoundingBox();
	}

	@Override
	public void Draw(SpriteBatch spriteBatch)
	{
		//sprite.draw(spriteBatch);
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

		float wallDistance = gameScreen.maxWallDistance;

		if (hasReset)
		{
			if (isLeftWall)
			{
				width = (leftWalls.get((id + 1) % leftWalls.size()).sprite.getWidth() - outScreenSpace);

				if (width + wallDistance > Config.GameWidth - Config.WallStep)
					factor = -1;

				width += Config.WallStep * factor/* * Random(1, 10)*/;
			}
			else
			{
				width = (rightWalls.get((id + 1) % rightWalls.size()).sprite.getWidth() - outScreenSpace);

				if (width <= Config.MinWallWidth)
					factor = 1;
				
				width += Config.WallStep * factor * Random(1, 10);
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

		width = MathUtils.clamp(width, Config.MinWallWidth, Config.MaxWallWidth);

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
				joinBorder.setX(sprite.getX() + outScreenSpace + width);
				float previousWallWidth = leftWalls.get((id + 1) % leftWalls.size()).sprite.getWidth() - outScreenSpace;
				if (previousWallWidth < width)
				{
					difference = previousWallWidth - width;
					//joinBorder.setColor(Color.valueOf("ffffff"));
					joinBorder.setX(joinBorder.getX() + difference + Config.WallStep);
				}
				else
				{
					difference = previousWallWidth - width + Config.WallStep;
					//joinBorder.setColor(Color.valueOf("ff0000"));
				}
				
				joinBorder.setColor(Color.valueOf("000000"));
			}
			else
			{
				float x = sprite.getX();
				float result = x + outScreenSpace - width;
				float result2 = sprite.getX() - outScreenSpace + width;
				joinBorder.setX(x);
				float previousWallWidth = rightWalls.get((id + 1) % rightWalls.size()).sprite.getWidth() - outScreenSpace;
				if (previousWallWidth < width)
				{
					difference = previousWallWidth - width;
					joinBorder.setColor(Color.valueOf("ffffff"));
					//joinBorder.setX(joinBorder.getX() + difference + Config.WallStep);
				}
				else
				{
					difference = previousWallWidth - width + Config.WallStep;
					joinBorder.setColor(Color.valueOf("ff0000"));
					//joinBorder.setX(joinBorder.getX() - difference);
				}
				
				joinBorder.setColor(Config.WallColorsDebug[id]);
			}
			
			joinBorder.setSize(Math.abs(difference), Config.WallBorderWidth);
			
			if (!isLeftWall)
				joinBorder.setSize(100, Config.WallBorderWidth);
			
		}

		//joinBorder.setSize(Config.WallStep * 2, Config.WallStep);

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
