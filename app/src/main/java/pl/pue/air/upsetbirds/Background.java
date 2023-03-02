package pl.pue.air.upsetbirds;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x = 0, y = 0;
    Bitmap background;

    Background (int screenX, int screenY, Resources res) {
//resources decode bitmap from drawable folder

        background = BitmapFactory.decodeResource(res, R.drawable.background);  //change background
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false); //resize to fit our screen

    }

}
