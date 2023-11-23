package pl.pue.air.upsetbirds;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static pl.pue.air.upsetbirds.GameView.screenRatioX;
import static pl.pue.air.upsetbirds.GameView.screenRatioY;

public class Bird {

    public int speed = 25; //15
    public boolean BirdShooted = true;
    int x = 0, y, width, height, birdCounter = 1;
    Bitmap bird1, bird2, bird3, bird4, bird5, bird6, bird7, bird8;

    Bird (Resources res) {

        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3);
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4);
        bird5 = BitmapFactory.decodeResource(res, R.drawable.bird5);
        bird6 = BitmapFactory.decodeResource(res, R.drawable.bird6);
        bird7 = BitmapFactory.decodeResource(res, R.drawable.bird7);
        bird8 = BitmapFactory.decodeResource(res, R.drawable.bird8);

        width = bird1.getWidth();
        height = bird1.getHeight();

        width /= 3;       //resize birds
        height /= 3;

//bug with the screen resolution - debug -> 0.92 casted into int = 0 * width/height = 0
/* width = *= (int) screenRatioX
 height = *= (int) screenRatioY;
 */

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false);
        bird5 = Bitmap.createScaledBitmap(bird5, width, height, false);
        bird6 = Bitmap.createScaledBitmap(bird6, width, height, false);
        bird7 = Bitmap.createScaledBitmap(bird7, width, height, false);
        bird8 = Bitmap.createScaledBitmap(bird8, width, height, false);

        y = -height;
    }

    /*
				string + cyferka
				C int to string
				buforowanie / double buffering / image casting
     */

    //enemy bird animation
    //moved to switch. Leave code for additional tests. 
	/*
    Bitmap getBird () {

        if (birdCounter == 1) {
            birdCounter++;
            return bird1;
        }

        if (birdCounter == 2) {
            birdCounter++;
            return bird2;
        }

        if (birdCounter == 3) {
            birdCounter++;
            return bird3;
        }

        if (birdCounter == 4) {
            birdCounter++;
            return bird4;
        }

        if (birdCounter == 5) {
            birdCounter++;
            return bird5;
        }

        if (birdCounter == 6) {
            birdCounter++;
            return bird6;
        }

        if (birdCounter == 7) {
            birdCounter++;
            return bird7;
        }

        birdCounter = 1;

        return bird8;
    }
	*/
	
	Bitmap getBird() {
    switch (birdCounter) {
        case 1:
            birdCounter++;
            return bird1;
        case 2:
            birdCounter++;
            return bird2;
        case 3:
            birdCounter++;
            return bird3;
        case 4:
            birdCounter++;
            return bird4;
        case 5:
            birdCounter++;
            return bird5;
        case 6:
            birdCounter++;
            return bird6;
        case 7:
            birdCounter++;
            return bird7;
        default:
            birdCounter = 1;
            return bird8;
    }
}

    //checking collision
    Rect CollisionFunction() {                     //if you bump into bird/fail to shoot bird = game over
        return new Rect(x, y, x + width, y + height);
    }

}
