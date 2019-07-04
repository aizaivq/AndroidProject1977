package com.via.recordscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by nick on 18-11-2.
 */

public class Utils {
    public static void startSystemPlayer(Context context,String path)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path),"video/mp4");
        context.startActivity(intent);
    }
}
