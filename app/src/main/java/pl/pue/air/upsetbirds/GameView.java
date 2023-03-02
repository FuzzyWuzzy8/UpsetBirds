package pl.pue.air.upsetbirds;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean ifGamePlaying, checkIfGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;      //for resizing to fit screen resolution, public+static so it can be easily accessed
    private Paint paint;
    private Bird[] birds;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Fire> fires;
    private int sound;
    private Flight flight;
    private GameActivity activity;
    private Background background1, background2;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        //https://developer.android.com/training/data-storage/shared-preferences
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;          //f treated as float
        screenRatioY = 1080f / screenY;          //divide width/height by screen

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources());

        fires = new ArrayList<>();

        background2.x = screenX;
//second background will not be on the screen, it will replace first just after screen ends on the X

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        birds = new Bird[7];

        for (int i = 0;i < 7;i++) {

            Bird bird = new Bird(getResources());
            birds[i] = bird;

        }

        random = new Random();

    }

    @Override
    public void run() {

        while (ifGamePlaying) {

            update ();
            draw ();
            sleep ();

//running until the user is playing; responsible for smooth move of objects
// update position -> draw it on the screen -> sleep (wait a while - set to 17ms)
        }

    }

    private void update () {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

//background will move by 10px to the left after update

        if (background1.x + background1.background.getWidth() < 0) {  //to prevent background from going off the screen (if lesser than 0 = off the screen)
            background1.x = screenX;             //place background after it ends
        }

        if (background2.x + background2.background.getWidth() < 0) {   //to prevent background from going off the screen
            background2.x = screenX;                    //place background after it ends
        }

        if (flight.isGoingUp)                          //moving up and down. to move up we have to hold upper side of left screen
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        if (flight.y < 0)     // if lesser than 0 it won't go off the screen
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Fire> trash = new ArrayList<>();

        for (Fire fire : fires) {

            if (fire.x > screenX)
                trash.add(fire);

            fire.x += 100 * screenRatioX;            //move the bullet by 100

            for (Bird bird : birds) {

                if (Rect.intersects(bird.CollisionFunction(),
                        fire.getCollisionShape())) {

                    score++;
                    bird.x = -500;
                    fire.x = screenX + 500;
                    bird.BirdShooted = true;

                }

            }

        }

        for (Fire fire : trash)
            fires.remove(fire);

        for (Bird bird : birds) {

            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {

                if (!bird.BirdShooted) {
                    checkIfGameOver = true;
                    return;
                }

                int bound = (int) (40 * screenRatioX);  //30 change speed multiplier
                bird.speed = random.nextInt(bound);      // random speed for bird

                if (bird.speed < 20 * screenRatioX) //10
                    bird.speed = (int) (20 * screenRatioX); // 10

                bird.x = screenX;   //placing bird towards end of the screen
                bird.y = random.nextInt(screenY - bird.height);

                bird.BirdShooted = false;
            }

            if (Rect.intersects(bird.CollisionFunction(), flight.getCollisionShape())) {   //if you bump into bird = game over

                checkIfGameOver = true;
                return;
            }

        }

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);      //for score

            if (checkIfGameOver) {
                ifGamePlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);         // shows canvas on the screen
                saveHighestScore();
                waitBeforeExiting ();
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Fire fire : fires)
                canvas.drawBitmap(fire.firing, fire.x, fire.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    //waiting before exit
    private void waitBeforeExiting() {

        try {
            Thread.sleep(3350);  //edit to set sleep time
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //save highest score to the shared preferences
    private void saveHighestScore() {

        if (prefs.getInt("topscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("topscore", score);
            editor.apply();
        }

    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        ifGamePlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            ifGamePlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {              //responsible for flying - go up/down

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;                         //if u take finger off the screen bird will go down
                if (event.getX() > screenX / 2)
                    flight.toFlyBird++;
                break;
        }

        return true;
    }

    public void newBullet() {

        if (!prefs.getBoolean("CheckIfMuted", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);  //play sound

        Fire fire = new Fire(getResources());
        fire.x = flight.x + flight.width;
        fire.y = flight.y + (flight.height / 2);
        fires.add(fire);

    }
}
