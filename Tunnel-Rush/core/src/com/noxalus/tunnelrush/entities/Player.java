package com.noxalus.tunnelrush.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.screens.GameScreen;

public class Player implements DrawableGameEntity
{
	// References
	private GameScreen gameScreen; 
	
	private Sprite sprite;

	private Vector3 projectedPosition;
	
	public boolean isTouchingScreen;
	public Vector3 initialTouchPosition;
	public Vector3 currentTouchPosition;
	public Vector3 initialSpritePosition;
	public Vector3 touchMoveDelta;

	public boolean isAlive;
	
	private Rectangle boundingBox;
	private ShapeRenderer shapeRenderer;
	
	private Sound deathSound;
	
	public Sprite getSprite()
	{
		return sprite;
	}

	public Rectangle getBoundingBox()
	{
		return boundingBox;
	}
	
	public Player(GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
		
		projectedPosition = new Vector3();
		
		boundingBox = new Rectangle();
		shapeRenderer = new ShapeRenderer();
		
		Texture texture = new Texture(Gdx.files.internal("data/graphics/sprites/player.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite = new Sprite(texture);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);

		deathSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/explosion.wav"));
		
		reset();
	}

	public void reset()
	{
		isAlive = true;

		// Touch
		isTouchingScreen = false;
		initialTouchPosition = new Vector3();
		currentTouchPosition = new Vector3();
		initialSpritePosition = new Vector3();
		touchMoveDelta = new Vector3();

		sprite.setPosition(Config.GameWidth/2 - sprite.getWidth()/2, Config.GameHeight - Config.GameHeight/6 - sprite.getHeight()/2);
	}

	@Override
	public void Update(float delta)
	{	
		if(Gdx.input.isTouched()) 
		{
			isTouchingScreen = true;

			if (initialSpritePosition.x == 0 && initialSpritePosition.y == 0)
			{
				initialSpritePosition.set(sprite.getX(), sprite.getY(), 0);
				initialTouchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			}

			currentTouchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			
			touchMoveDelta.set((int)currentTouchPosition.x - (int)initialTouchPosition.x,
							   (int)currentTouchPosition.y - (int)initialTouchPosition.y, 
							   	0);
			
			sprite.setPosition(initialSpritePosition.x + 
							   (touchMoveDelta.x * (float)Math.cos(Math.toRadians(gameScreen.cameraRotationAngle))) + 
							   (touchMoveDelta.y * -(float)Math.sin(Math.toRadians(gameScreen.cameraRotationAngle))),
							   initialSpritePosition.y + 
							   (touchMoveDelta.x * (float)Math.sin(Math.toRadians(gameScreen.cameraRotationAngle))) + 
							   (touchMoveDelta.y * (float)Math.cos(Math.toRadians(gameScreen.cameraRotationAngle))));
		}
		else
		{
			initialSpritePosition.set(0, 0, 0);
			isTouchingScreen = false;
		}

		// Collision detection
		
		// Compute the position of the player according to camera rotation
		projectedPosition.set(sprite.getX(), sprite.getY(), 0);
		gameScreen.camera.project(projectedPosition);
		
		projectedPosition.set(MathUtils.clamp(projectedPosition.x, 0, Config.GameWidth - sprite.getWidth()), 
					 		  MathUtils.clamp(projectedPosition.y, sprite.getHeight(), Config.GameHeight), 
				 		  	  0);

		gameScreen.camera.project(projectedPosition);
		
		sprite.setPosition(projectedPosition.x, projectedPosition.y);
		
		UpdateBoundingBox();
	}

	@Override
	public void Draw(SpriteBatch spriteBatch)
	{
		this.sprite.draw(spriteBatch);
	}
	
	public void DrawBoundingBox()
	{
		shapeRenderer.setProjectionMatrix(gameScreen.camera.combined);
		 
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
		shapeRenderer.end();
	}

	public void Kill()
	{
		deathSound.play();
		
		isAlive = false;
	}
	
	private void UpdateBoundingBox()
	{
		int factor = 3;
		
		boundingBox.set(
				this.sprite.getX() + (this.sprite.getWidth() / factor) / 2, 
				this.sprite.getY() + (this.sprite.getHeight() / factor) / 1.5f, 
				this.sprite.getWidth() - (this.sprite.getWidth() / factor), 
				this.sprite.getHeight() - (this.sprite.getHeight() / factor));
	}
}
