package com.noxalus.tunnelrush;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noxalus.tunnelrush.gameservices.ActionResolver;
import com.noxalus.tunnelrush.screens.GameScreen;
import com.noxalus.tunnelrush.screens.MainMenuScreen;

public class TunnelRush extends Game {

    public ActionResolver ActionResolver;
    public SpriteBatch SpriteBatch;

    // Screens
    public GameScreen gameScreen;
    public MainMenuScreen mainMenuScreen;

    public TunnelRush(ActionResolver actionResolver) {
        this.ActionResolver = actionResolver;
    }

	@Override
	public void create () {
        SpriteBatch = new SpriteBatch();
        Assets.load();

        gameScreen = new GameScreen(this);
        mainMenuScreen = new MainMenuScreen(this);

        setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
		/*
        Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
        */
	}
}
