package com.migara.mudan.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by musa on 13.05.2017.
 */

public class Tube {
    public static  final  int TUBE_WITDH = 52;
    private static  final  int FLUCTUATION = 130;
    private static  final  int TUBE_GAP = 100;
    private static  final  int LOWEST_OPENİNG = 120;

    private Texture topTube ,bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Rectangle boundsTop , boundsBot;
    private Random rand;

    public Tube(float x){
        rand = new Random();
        topTube = new Texture("n"+(rand.nextInt(4)+1)+ ".png");
        bottomTube = new Texture("m"+(rand.nextInt(4)+1)+ ".png");

        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENİNG);  // tüp sol alt köşesinden çizilmeye başlanıyor
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop = new Rectangle(posTopTube.x , posTopTube.y , topTube.getWidth()-15, topTube.getHeight());
        boundsBot = new Rectangle(posBotTube.x , posBotTube.y , bottomTube.getWidth()-15, bottomTube.getHeight());

    }

    public void reposition(float x){
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENİNG);
        posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);
    }

    public boolean collides (Rectangle player){
        return player.overlaps(boundsBot) || player.overlaps(boundsTop);
    }

    public void dispose(){
        topTube.dispose();
        bottomTube.dispose();
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }
}
