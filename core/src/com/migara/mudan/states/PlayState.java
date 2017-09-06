package com.migara.mudan.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.migara.mudan.MudanDemo;
import com.migara.mudan.sprites.Animation;
import com.migara.mudan.sprites.Bird;
import com.migara.mudan.sprites.Particle;
import com.migara.mudan.sprites.Tube;

/**
 * Created by musa on 13.05.2017.
 */

public class PlayState extends State{
    private static final int TUBE_SPACİNG = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture background;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Array<Tube> tubes;
    private Texture fire;
    private Particle particle;
    private Animation bg;
    private int bgPos ;
    private boolean bgReverse;

    private int mer;
    private boolean merE;
    private Sound action;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50,300);
        camera.setToOrtho(false, MudanDemo.WITDH / 2 , MudanDemo.HEIGHT / 2);   // kamerayı zoomlar döndürür orjini değiştirir
        background = new Texture("bgt.png");
        bg = new Animation(new TextureRegion(background), 8,1);
        bgPos = 0;      bgReverse=true;
        ground = new Texture("gr.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth /2 , GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth /2) + ground.getWidth() , GROUND_Y_OFFSET);

        mer =0;
        merE = true;
        action = Gdx.audio.newSound(Gdx.files.internal("0085.ogg"));
        fire = new Texture("blue.png");
        particle = new Particle(new TextureRegion(fire) , 16 , 0.5f ,5);


        tubes = new Array<Tube>();
        for (int i=1; i<=TUBE_COUNT; i++){
            tubes.add(new Tube(i * (TUBE_SPACİNG + Tube.TUBE_WITDH)));
        }
    }

    private void updateGround(){
        if (camera.position.x -(camera.viewportWidth /2 ) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() *2,0);
        if (camera.position.x -(camera.viewportWidth /2 ) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() *2,0);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt);
        particle.update(dt);
        bg.update(dt);
        updateGround();
        camera.position.x = bird.getPosition().x + 40 + mer;

        if (mer <= 100 && merE){
            mer++;
        }else{
            if (merE) action.play();
            mer--;
            merE = false;
        }if(mer <=-100){
            merE = true;
            action.stop();
        }

        if(bgPos <= -480 )
            bgReverse = false;
        else if (bgPos >=0)
            bgReverse = true;
        if(bgReverse){
            bgPos--;
        }else
            bgPos++;

        for (int i=0; i<tubes.size; i++){
            Tube tube = tubes.get(i);
            if (camera.position.x -(camera.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WITDH + TUBE_SPACİNG) * TUBE_COUNT));
            }if(tube.collides(bird.getBounds())){
                action.stop();
                gsm.set(new GameOver(gsm));
            }
        }
        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET){
            action.stop();
            gsm.set(new GameOver(gsm));

        }

        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg.getFrame(), camera.position.x - (camera.viewportWidth / 2) + bgPos, 0);
        sb.draw(bird.getBird(),bird.getPosition().x,bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
            for(int i=0; i<particle.getLife(); i++){
                sb.draw(particle.getFrame(i), tube.getPosTopTube().x + (int)(Math.random()*50) -25, tube.getPosTopTube().y + (int)(Math.random()*50) -25);
                sb.draw(particle.getFrame(i), tube.getPosBotTube().x + (int)(Math.random()*50) -25, tube.getPosBotTube().y + 250 + (int)(Math.random()*50) -25);
            }

        }
        sb.draw(ground,groundPos1.x,groundPos1.y);
        sb.draw(ground,groundPos2.x,groundPos2.y);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
        ground.dispose();

        for(Tube tube : tubes){
            tube.dispose();
        }
    }
}
