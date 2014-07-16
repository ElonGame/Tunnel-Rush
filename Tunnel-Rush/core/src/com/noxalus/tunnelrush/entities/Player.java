package com.noxalus.tunnelrush.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.noxalus.tunnelrush.Assets;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.GameData;
import com.noxalus.tunnelrush.screens.GameScreen;

public class Player implements DrawableGameEntity
{
	// References
	private GameScreen gameScreen; 
	private GameData gameData;

	private Vector3 projectedPosition;
	
	public boolean isTouchingScreen;
	public Vector3 initialTouchPosition;
	public Vector3 currentTouchPosition;
	public Vector3 initialSpritePosition;
	public Vector3 touchMoveDelta;

	public boolean isAlive;
	
	private Rectangle boundingBox;
	private ShapeRenderer shapeRenderer;
	
	private Sprite sprite;
	private float animationState;
	
	// Particles
	ParticleEffectPool jetEffectPool;
	Array<PooledEffect> effects = new Array<PooledEffect>();
	

	private FrameBuffer fbo;
	
	public Sprite getSprite()
	{
		return sprite;
	}

	public Rectangle getBoundingBox()
	{
		return boundingBox;
	}
	
	public Player(GameScreen gameScreen, GameData gameData)
	{
		fbo = new FrameBuffer(Format.RGB888, Config.GameWidth, Config.GameWidth, false);

		this.gameScreen = gameScreen;
		this.gameData = gameData;
		
		projectedPosition = new Vector3();
		
		boundingBox = new Rectangle();
		shapeRenderer = new ShapeRenderer();
		
		sprite = new Sprite(Assets.playerAnimation.getKeyFrame(0, true));
		sprite.flip(false, true);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		ParticleEffect jetEffect = new ParticleEffect();
		jetEffect.load(Gdx.files.internal("data/graphics/particles/jet-engine"), Gdx.files.internal("data/graphics/particles"));
		jetEffectPool = new ParticleEffectPool(jetEffect, 1, 2);
		
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
		
		// Create effect:
		PooledEffect effect = jetEffectPool.obtain();
		effect.setPosition(sprite.getX() + (sprite.getWidth() / 2), sprite.getY() + (sprite.getHeight() / 1.5f));
		effects.add(effect);
	}

	@Override
	public void Update(float delta)
	{	
		animationState += delta;
		
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
							   (touchMoveDelta.x * (float)Math.cos(Math.toRadians(gameData.cameraRotationAngle))) + 
							   (touchMoveDelta.y * -(float)Math.sin(Math.toRadians(gameData.cameraRotationAngle))),
							   initialSpritePosition.y + 
							   (touchMoveDelta.x * (float)Math.sin(Math.toRadians(gameData.cameraRotationAngle))) + 
							   (touchMoveDelta.y * (float)Math.cos(Math.toRadians(gameData.cameraRotationAngle))));
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
	public void Draw(SpriteBatch spriteBatch, float delta)
	{
		// Particles
		// Update and draw effects:
		if (isAlive)
		{
//	        fbo.begin();
//	        
////	        fb.enableBlending();
//	        Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
//
//	        Gdx.gl.glClearColor(1, 1, 1, 0);
//	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        
			for (int i = effects.size - 1; i >= 0; i--) {
			    PooledEffect effect = effects.get(i);
			    effect.setPosition(sprite.getX() + (sprite.getWidth() / 2), sprite.getY() + (sprite.getHeight() / 1.5f));
			    effect.draw(spriteBatch, delta);
			    if (effect.isComplete()) {
			        effect.free();
			        effects.removeIndex(i);
			    }
			}
			
//			fbo.end();
		}
		else
		{
			// Reset all effects:
			for (int i = effects.size - 1; i >= 0; i--)
			    effects.get(i).free();
			effects.clear();
		}
		
		sprite.setRegion(Assets.playerAnimation.getKeyFrame(animationState, true));
		sprite.flip(false, true);
//		spriteBatch.draw(Assets.playerAnimation.getKeyFrame(animationState, true),
//				sprite.getX(), sprite.getY() - 25, sprite.getOriginX(), sprite.getOriginY(), 
//				sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
		sprite.draw(spriteBatch);
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
		Assets.deathSound.play();
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
