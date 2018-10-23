package widgets;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zohaltech.app.grewords.R;

public class MyToast extends Toast {

    //    public static enum MessageType{
    //        INFORMATION,
    //        WARNING,
    //        ERROR
    //    }
    private AppCompatImageView image;
    private TextView           text;

    private MyToast(Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        image = layout.findViewById(R.id.image);
        text = layout.findViewById(R.id.text);
        setView(layout);
    }

    public static void show(Activity activity, String message, int duration) {
        MyToast myToast = new MyToast(activity);
        myToast.image.setVisibility(View.GONE);
        myToast.text.setText(message);
        myToast.setDuration(duration);
        myToast.show();
    }
}
