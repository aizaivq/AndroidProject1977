package com.via.opencv.judge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by nick on 18-3-13.
 */

public class JudgeUtil {
    private static final String TAG = JudgeUtil.class.getSimpleName();

    public static Bitmap bitmap2Grey(Bitmap input, int g) {
        int w = input.getWidth();
        int h = input.getHeight();
        int alpha = 0xFF << 24;
        int[] ps = new int[w * h];

        input.getPixels(ps, 0, w, 0, 0, w, h);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int grey = ps[w * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                if (red > g || green > g || blue > g) {
                    red = 255;
                    green = 255;
                    blue = 255;
                }


                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                ps[w * i + j] = grey;
            }
        }
        Bitmap ret = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        ret.setPixels(ps, 0, w, 0, 0, w, h);
        return ret;

    }

    public static Bitmap bitmap2Grey(String path, int g) {
        Bitmap input = BitmapFactory.decodeFile(path);
        return bitmap2Grey(input, g);
    }

    public static Bitmap createJudgeBitmap(int w, int h, int splitWidth) {
        //   int[] ps = new int[w * h];
        Bitmap ret = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        boolean isSplit = false;
        boolean lastSplit = isSplit;
        int bitmapSplitWidth = 0;
        for (int y = 0; y < h; y++) {
            // Log.d(TAG,"y: " + y);
            for (int x = 0; x < w; x++) {
                //     Log.d(TAG,"x: " + x);
                if (isSplit) {
                    if (bitmapSplitWidth >= splitWidth) {
                        bitmapSplitWidth = 0;
                        isSplit = false;
                    }
                    ret.setPixel(x, y, Color.BLACK);
                } else {
                    if (bitmapSplitWidth >= splitWidth) {
                        bitmapSplitWidth = 0;
                        isSplit = true;
                    }
                    ret.setPixel(x, y, Color.WHITE);
                }
                bitmapSplitWidth++;

            }
            bitmapSplitWidth = 0;
            if (y % splitWidth == 0) {
              //  Log.d(TAG, "split change y: " + y);
                isSplit = !lastSplit;
            }
            else
            {
                isSplit = lastSplit;
            }
            lastSplit = isSplit;
        }

        return ret;


    }


}
