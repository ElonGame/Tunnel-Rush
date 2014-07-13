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
	public static Texture playerTexture;
	public static Texture title;
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
		playerTexture = loadTexture("data/graphics/sprites/player_animation.png");
		title = loadTexture("data/graphics/pictures/title.png");
		buttons = loadTexture("data/graphics/pictures/buttons.png");
		
		// Texture regions
		play = new TextureRegion(buttons, 0, 0, 320, 184);
		rate = new TextureRegion(buttons, 320, 0, 320, 184);
		achievement = new TextureRegion(buttons, 640, 0, 320, 184);
		leaderboard = new TextureRegion(buttons, 960, 0, 320, 184);
		
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
	
	public static void dispose() {
		
	}
}
