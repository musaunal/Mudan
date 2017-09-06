package com.migara.mudan.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.migara.mudan.MudanDemo;
import com.migara.mudan.sprites.Animation;

import sun.awt.image.GifImageDecoder;

/**
 * Created by musa on 13.05.2017.
 */

public class MenuState extends State {
    private Texture background;
    private Texture playBtn;
    private Animation bg;
    private Vector2 bgPos ;
    private boolean bgReverse;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, MudanDemo.WITDH / 2 , MudanDemo.HEIGHT / 2);   // kamerayı zoomlar döndürür orjini değiştirir
        background = new Texture("bgt.png");
        playBtn = new Texture("playbtn.png");
        bg = new Animation(new TextureRegion(background), 8, 0.9f);
        bgPos = new Vector2(0,0);
        bgReverse = true;
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        bg.update(dt);
        if(bgPos.x <= -480 )
            bgReverse = false;
        else if (bgPos.x >=0)
            bgReverse = true;
        if(bgReverse){
            bgPos.x--;
        }else
            bgPos.x++;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg.getFrame(),bgPos.x, bgPos.y);
        sb.draw(playBtn, camera.position.x - playBtn.getWidth()/2, camera.position.y);  //(MudanDemo.WITDH / 2) - (playBtn.getWidth() / 2) , (MudanDemo.HEIGHT / 2) - (playBtn.getHeight() / 2)
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
    }
}
