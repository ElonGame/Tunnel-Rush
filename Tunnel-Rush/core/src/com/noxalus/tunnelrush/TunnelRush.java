package com.noxalus.tunnelrush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noxalus.tunnelrush.gameservices.ActionResolver;
import com.noxalus.tunnelrush.screens.MainMenuScreen;

public class TunnelRush extends Game {
	public ActionResolver actionResolver;
	public SpriteBatch SpriteBatch;
	
	public TunnelRush(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
	}
	
	@Override
	public void create() {
		SpriteBatch = new SpriteBatch();
		Assets.load();
		
		setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	/** {@link Game#dispose()} only calls {@link Screen#hide()} so you need to override {@link Game#dispose()} in order to call
	 * {@link Screen#dispose()} on each of your screens which still need to dispose of their resources. SuperJumper doesn't
	 * actually have such resources so this is only to complete the example. */
	@Override
	public void dispose () {
		super.dispose();
		SpriteBatch.dispose();
		Assets.dispose();
		getScreen().dispose();
	}
}
