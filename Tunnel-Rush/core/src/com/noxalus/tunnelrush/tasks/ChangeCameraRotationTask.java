package com.noxalus.tunnelrush.tasks;

import com.badlogic.gdx.utils.Timer.Task;
import com.noxalus.tunnelrush.Config;
import com.noxalus.tunnelrush.screens.GameScreen;
import com.noxalus.tunnelrush.screens.GameScreen.CameraRotationType;

public class ChangeCameraRotationTask extends Task
{
	private GameScreen gameScreen;
	
	public ChangeCameraRotationTask(GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
	}
	
	@Override
	public void run() {
		if (!gameScreen.cameraRotationEnabled || Config.random.nextFloat() > 0.5f)
			return;
					
		float randomValue = Config.random.nextFloat();
		CameraRotationType newCameraRotationType;
		
		if (randomValue < 0.33f)
		{
			newCameraRotationType = CameraRotationType.NONE;
		}
		else if (randomValue < 0.66f)
		{
			newCameraRotationType = CameraRotationType.CLOCKWISE;
		}
		else
		{
			newCameraRotationType = CameraRotationType.COUNTERCLOCKWISE;
		}
		
		// Change rotation
		gameScreen.cameraRotationType = newCameraRotationType;
	}	
}
