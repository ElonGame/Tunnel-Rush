package com.noxalus.tunnelrush;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;

public class Config {
	public static int GameWidth = Gdx.graphics.getWidth();
	public static int GameHeight = Gdx.graphics.getHeight();
	public static Random random = new Random(Double.doubleToLongBits(Math.random()));

	// Game logic
	public static final int ScoreToIncreaseDifficulty = 1000;
	
	public static final float InitialWallSpeed = 1000; // 1000
	public static final float WallSpeedStep = 42f; // the answer
	
	public static final int MaxWallNumber = 10;
	public static final int WallStep = 10;
	public static final int WallBorderWidth = 10;
	public static final int MinWallWidth = Config.WallStep * 2;
	public static final int MaxWallWidth = (int)(Config.GameWidth / 1.5f);
	public static final int InitialMaxWallDistance = (int)(Config.GameWidth / 2);
	public static final float WallDistanceChangeStep = 50f;
	public static final int[] MaxWallDistanceIntervals = new int[]
			{
				InitialMaxWallDistance,
				InitialMaxWallDistance / 2,
				InitialMaxWallDistance / 3,
				InitialMaxWallDistance / 4,
				InitialMaxWallDistance / 5,
				InitialMaxWallDistance / 6
			};
	
	public static final int MaxWallDistanceStep = 5;
	public static final int MinWallDistance = 150;
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
		Color.valueOf("ff0000"),
		Color.valueOf("00ff00"),
		Color.valueOf("0000ff"),
		Color.valueOf("00FFFF"),
		Color.valueOf("FF00FF"),
		Color.valueOf("FFFF00"),
		Color.valueOf("000000"),
		Color.valueOf("808080"),
		Color.valueOf("d083fa"),
		Color.valueOf("800000"),
		Color.valueOf("FF8080"),
		Color.valueOf("FF8000"),
		Color.valueOf("FFFFFF"),
			};

	public static Preferences Settings = Gdx.app.getPreferences("settings");
	
	// Debug
	public final static boolean DisableRotation = false;
}
