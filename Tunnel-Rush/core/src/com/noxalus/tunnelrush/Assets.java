package com.noxalus.tunnelrush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	// Textures
	public static Texture pixelWall;
	public static Texture pixelWalBorder;
	public static Texture pixelWallJoinBorder;
	public static Texture pixelWallBorderBottom;
	public static Texture pixelBackground;
	public static Texture playerTexture;
	public static Texture title;
	public static Texture gameOver;
	public static Texture score;
	public static Texture best;
	public static Texture deaths;
	public static Texture buttons;
	
	// Texture regions
	public static TextureRegion play;
	public static TextureRegion rate;
	public static TextureRegion achievement;
	public static TextureRegion leaderboard;
	
	// Animations
	public static Animation playerAnimation;
	
	// Fonts
	public static BitmapFont font;
	public static BitmapFont digitFont;
	
	// Musics
	public static Music menu;
	public static Music introMusic;
	public static Music loopMusic;
	
	// Sounds
	public static  Sound deathSound;
	public static  Sound selectSound;
	
	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load() {
		// Textures
		pixelWall = loadTexture("data/graphics/pictures/pixel.png");
		pixelWalBorder = loadTexture("data/graphics/pictures/pixel.png");
		pixelWallJoinBorder = loadTexture("data/graphics/pictures/pixel.png");
		pixelWallBorderBottom = loadTexture("data/graphics/pictures/pixel.png");
		pixelBackground = loadTexture("data/graphics/pictures/pixel.png");
		playerTexture = loadTexture("data/graphics/sprites/player_animation.png");
		title = loadTexture("data/graphics/pictures/title.png");
		gameOver = loadTexture("data/graphics/pictures/gameover.png");
		score = loadTexture("data/graphics/pictures/score.png");
		best = loadTexture("data/graphics/pictures/best.png");
		deaths = loadTexture("data/graphics/pictures/deaths.png");
		buttons = loadTexture("data/graphics/pictures/buttons.png");
		
		// Texture regions
		int buttonWidth = buttons.getWidth() / 4;
		play = new TextureRegion(buttons, 0, 0, buttonWidth, buttons.getHeight());
		rate = new TextureRegion(buttons, buttonWidth, 0, buttonWidth, buttons.getHeight());
		achievement = new TextureRegion(buttons, buttonWidth * 2, 0, buttonWidth, buttons.getHeight());
		leaderboard = new TextureRegion(buttons, buttonWidth * 3, 0, buttonWidth, buttons.getHeight());
		
		pixelWall.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Animations
		playerAnimation = new Animation(0.2f, 
				new TextureRegion(playerTexture, 0, 0, 64, 70), 
				new TextureRegion(playerTexture, 64, 0, 64, 70),
				new TextureRegion(playerTexture, 128, 0, 64, 70),
				new TextureRegion(playerTexture, 192, 0, 64, 70));
		
		// Fonts
		font = new BitmapFont(Gdx.files.internal("data/graphics/fonts/classic.fnt"),
				Gdx.files.internal("data/graphics/fonts/classic.png"), true);
		
		digitFont = new BitmapFont(Gdx.files.internal("data/graphics/fonts/digits.fnt"),
				Gdx.files.internal("data/graphics/fonts/digits_0.tga"), true);
		//Assets.digitFont.scale(5);
		
		// Musics
		menu = Gdx.audio.newMusic(Gdx.files.internal("data/audio/bgm/menu.mp3"));
		introMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/bgm/intro.mp3"));
		loopMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/bgm/loop.mp3"));
		
		menu.setLooping(true);
		loopMusic.setLooping(true);
		
		// Sounds
		deathSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/explosion.wav"));
		selectSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/select.wav"));
	}
}
