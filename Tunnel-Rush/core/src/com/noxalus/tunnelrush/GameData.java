package com.noxalus.tunnelrush;

public class GameData {
	public enum TunnelPatterns {
		NONE, // The tunnel keep to go straight
		DECREASE, // The distance between left and right walls decrease
        INCREASE, // The distance between left and right walls increase
        RIGHT, // The tunnel go to the right direction
        LEFT, // The tunnel go to the left direction
        RANDOM // The left and right walls go in a random direction
	}
	
	public boolean isDemo;
    public boolean godMode;
	public boolean gameOver;
	public int score;
	public int difficulty;
	public float wallDistance;
	public TunnelPatterns tunnelPatterns;
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
        godMode = true;
		gameOver = false;
		score = 0;
		difficulty = 0;
		wallDistance = Config.MaxWallDistance;
        tunnelPatterns = TunnelPatterns.NONE;
		highscore = Config.Settings.getInteger("highscore");
		deathNumber = Config.Settings.getInteger("deathNumber");
		
		cameraRotationAngle = 0f;
		cameraRotationEnabled = false;
		cameraRotationSpeed = 0f;
		cameraRotationType = CameraRotationType.CLOCKWISE;
	}
}
