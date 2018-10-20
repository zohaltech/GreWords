package com.zohaltech.app.grewords.classes;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zohaltech.app.grewords.R;

import java.text.MessageFormat;

public final class DialogManager {
    
    public static  String           timeResult;
    private static TimePickerDialog timePickerDialog;
    
    public static Dialog getPopupDialog(
            Context context
            , final String caption
            , final String message
            , String positiveButtonText
            , String negativeButtonText
            , final Runnable onDialogShown
            , final Runnable onPositiveButtonClick
            , final Runnable onNegativeButtonClick) {
        if (onDialogShown != null) {
            onDialogShown.run();
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView txtCaption = dialog.findViewById(R.id.txtCaption);
        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
        Button positiveButton = dialog.findViewById(R.id.positiveButton);
        Button negativeButton = dialog.findViewById(R.id.negativeButton);
        txtCaption.setText(caption);
        txtMessage.setText(message);
        positiveButton.setText(positiveButtonText);
        negativeButton.setText(negativeButtonText);
        
        positiveButton.setOnClickListener(v -> {
            onPositiveButtonClick.run();
            dialog.dismiss();
        });
        
        negativeButton.setOnClickListener(v -> {
            onNegativeButtonClick.run();
            dialog.dismiss();
        });
        return dialog;
    }
    
    public static void showTimePickerDialog(Activity activity, int hour, int minute, final Runnable onPositiveActionClick) {
        timePickerDialog =
                new android.app.TimePickerDialog(activity, (view, hourOfDay, minute1) -> {
                    timeResult = MessageFormat.format("{0}:{1}",
                            String.valueOf(hourOfDay).length() == 1 ? "0" + hourOfDay : hourOfDay,
                            String.valueOf(minute1).length() == 1 ? "0" + minute1 : minute1);
                    onPositiveActionClick.run();
                    timeResult = "";
                    if (timePickerDialog != null && timePickerDialog.isShowing()) {
                        timePickerDialog.dismiss();
                    }
                }, hour, minute, true);
        timePickerDialog.setTitle("");
        timePickerDialog.show();
    }
}
