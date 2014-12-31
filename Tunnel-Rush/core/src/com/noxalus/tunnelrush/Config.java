package com.noxalus.tunnelrush;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Config {
	public static final int GamePerfectWidth = 720;
	public static final int GamePerfectHeight = 1280;
	public static final int PlayerSpriteWidth = 64;
	public static final int PlayerSpriteHeight = 70;
	
	public static int GameWidth = Gdx.graphics.getWidth();
	public static int GameHeight = Gdx.graphics.getHeight();
	public static Random random = new Random(Double.doubleToLongBits(Math.random()));

	public static final float ResolutionRatio = (float)GameWidth / (float)GameHeight;
	public static final float PerfectResolutionRatio = (float)GamePerfectWidth / (float)GamePerfectHeight;
	public static final float Testouille = (1 / PerfectResolutionRatio);
	public static final float GameResolutionRatio = Testouille * ResolutionRatio;
	
	// Game logic
	public static final int ScoreToIncreaseDifficulty = 1000;
	
	public static final float InitialWallSpeed = 1000; // 1000
	public static final float WallSpeedStep = 42f; // the answer
	
	public static final int MaxWallNumber = 10;
	public static final int WallStep = 10;
	public static final int WallBorderWidth = 10;
	public static final int MinWallWidth = Config.WallStep * 2;
	public static final float MinWallDistance = Config.PlayerSpriteWidth * 1.25f;
	public static final float MaxWallDistance = (int)(Config.GameWidth / 2);
	public static final float WallDistanceChangeStep = 50f;
	public static final float[] MaxWallDistanceIntervals = new float[]
			{
				MaxWallDistance,
				MaxWallDistance / 2,
				MaxWallDistance / 3,
				MaxWallDistance / 4,
				MaxWallDistance / 5,
				MaxWallDistance / 6
			};
	
	public static final int MaxWallDistanceStep = 5;
	public static final int MaxWallWidth = (int)(Config.GameWidth - (Config.PlayerSpriteWidth * 1.25f));
	public static final int WallHeight = Config.GameHeight / MaxWallNumber;

	public static final int[] MaxWallSpeedIntervals = new int[]
			{
				2000,
				3000,
				4000,
				5000,
				7500,
				10000
			};

	public static final Color[] WallColors = new Color[]
			{
		Color.valueOf("71c5cf"),
		Color.valueOf("84e28c"),
		Color.valueOf("577e23"),
		Color.valueOf("ddd894"),
		Color.valueOf("bfa340"),
		Color.valueOf("ff0000")
			};
	
	public static final Color[] WallColorsDebug = new Color[]
			{
		Color.valueOf("FF0000"),
		Color.valueOf("FD3F01"),
		Color.valueOf("F8CF01"),
		Color.valueOf("FAF118"),
		Color.valueOf("FF00FF"),
		Color.valueOf("79A005"),
		Color.valueOf("6ACCE5"),
		Color.valueOf("d083fa"),
		Color.valueOf("800000"),
		Color.valueOf("FF8080"),
		Color.valueOf("FF8000"),
		Color.valueOf("0C499C"),
			};
	
	public static Preferences Settings = Gdx.app.getPreferences("settings");
	
	// Debug
	public final static boolean DisableRotation = false;
}
