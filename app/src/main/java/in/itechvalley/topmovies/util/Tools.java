package in.itechvalley.topmovies.util;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Tools
{
    public static void showSnackbar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }
}
