package pl.pue.air.upsetbirds;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static pl.pue.air.upsetbirds.GameView.screenRatioX;
import static pl.pue.air.upsetbirds.GameView.screenRatioY;

public class Fire {

    int x, y, width, height;
    Bitmap firing;

    //fire, bullet speed in GameView
    Fire(Resources res) {

        firing = BitmapFactory.decodeResource(res, R.drawable.fire);

        width = firing.getWidth();
        height = firing.getHeight();

        width /= 4;    //to resize bullet
        height /= 4;

//same bug as in bird and flight; fixed
        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        firing = Bitmap.createScaledBitmap(firing, width, height, false);

    }

    //checking collision
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}