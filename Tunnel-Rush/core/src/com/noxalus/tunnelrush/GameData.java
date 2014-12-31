package com.noxalus.tunnelrush;

public class GameData {
	public enum WallDistanceType {
		NONE,
		INCREASE,
		DECREASE
	}
	
	public boolean isDemo;
	public boolean gameOver;
	public int score;
	public int difficulty;
	public float wallDistance;
	public WallDistanceType wallDistanceType;
	public int highscore;
	public int deathNumber;

	// Camera
	public boolean cameraRotationEnabled;
	public float cameraRotationAngle;
	public float cameraRotationSpeed;
	
	public enum CameraRotationType {
		NONE,
		CLOCKWISE,
		COUNTERCLOCKWISE
	}
	
	public CameraRotationType cameraRotationType;
	
	public void initialize()
	{
        isDemo = true;
		gameOver = false;
		score = 0;
		difficulty = 0;
		wallDistance = Config.MaxWallDistance;
		wallDistanceType = WallDistanceType.NONE;
		highscore = Config.Settings.getInteger("highscore");
		deathNumber = Config.Settings.getInteger("deathNumber");
		
		cameraRotationAngle = 0f;
		cameraRotationEnabled = false;
		cameraRotationSpeed = 0f;
		cameraRotationType = CameraRotationType.CLOCKWISE;
	}
}
