package pl.pue.air.upsetbirds;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static pl.pue.air.upsetbirds.GameView.screenRatioX;
import static pl.pue.air.upsetbirds.GameView.screenRatioY;

public class Flight {

    int toFlyBird = 0;
    boolean isGoingUp = false; //boolean default value is false. Used in GameView
    int x, y, width, height, flyCounter = 0, toFlyBirdCounter = 1;
    Bitmap flight1, flight2, birdfly1, birdfly2, birdfly3, birdfly4, birdfly5, dead;
    private GameView gameView;

    Flight (GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2);

//same bug as in bird and bullet; fixed
        width = flight1.getWidth();
        height = flight1.getHeight();

        width /= 4;      //reduces size of the img flight
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);         //resize bitmap
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        birdfly1 = BitmapFactory.decodeResource(res, R.drawable.birdfly1);
        birdfly2 = BitmapFactory.decodeResource(res, R.drawable.birdfly2);
        birdfly3 = BitmapFactory.decodeResource(res, R.drawable.birdfly3);
        birdfly4 = BitmapFactory.decodeResource(res, R.drawable.birdfly4);
        birdfly5 = BitmapFactory.decodeResource(res, R.drawable.birdfly5);

        birdfly1 = Bitmap.createScaledBitmap(birdfly1, width, height, false);
        birdfly2 = Bitmap.createScaledBitmap(birdfly2, width, height, false);
        birdfly3 = Bitmap.createScaledBitmap(birdfly3, width, height, false);
        birdfly4 = Bitmap.createScaledBitmap(birdfly4, width, height, false);
        birdfly5 = Bitmap.createScaledBitmap(birdfly5, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y = screenY / 2;           //flight centered vertically
        x = (int) (64 * screenRatioX);    //setting margin for different screens

    }

    //bird flying animation
    //moved to switch. Leave code for additional tests 
    /*
    Bitmap getFlight () {

        if (toFlyBird != 0) {

            if (toFlyBirdCounter == 1) {
                toFlyBirdCounter++;
                return birdfly1;
            }

            if (toFlyBirdCounter == 2) {
                toFlyBirdCounter++;
                return birdfly2;
            }

            if (toFlyBirdCounter == 3) {
                toFlyBirdCounter++;
                return birdfly3;
            }

            if (toFlyBirdCounter == 4) {
                toFlyBirdCounter++;
                return birdfly4;
            }

            toFlyBirdCounter = 1;
            toFlyBird--;
            gameView.newBullet();

            return birdfly5;
        }

        if (flyCounter == 0) {
            flyCounter++;
            return flight1;
        }
        flyCounter--;

        return flight2;
    }
    */

    Bitmap getFlight () {

        if (toFlyBird != 0) {

            switch (toFlyBirdCounter) {
                case 1:
                    toFlyBirdCounter++;
                    return birdfly1;
                case 2:
                    toFlyBirdCounter++;
                    return birdfly2;
                case 3:
                    toFlyBirdCounter++;
                    return birdfly3;
                case 4:
                    toFlyBirdCounter++;
                    return birdfly4;
                default:
                    toFlyBirdCounter = 1;
                    toFlyBird--;
                    gameView.newBullet();
                    return birdfly5;
            }

        }

        if (flyCounter == 0) {
            flyCounter++;
            return flight1;
        }
        flyCounter--;

        return flight2;
    }


    // https://developer.android.com/reference/android/graphics/Rect
    //checking collision
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {
        return dead;
    }

}
