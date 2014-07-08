package com.noxalus.tunnelrush;

import com.badlogic.gdx.Game;
import com.noxalus.tunnelrush.screens.GameScreen;

public class TunnelRush extends Game {
	
	private GameScreen gameScreen;
	
	@Override
	public void create() {
		gameScreen = new GameScreen();
		
		setScreen(gameScreen);
	}
}
