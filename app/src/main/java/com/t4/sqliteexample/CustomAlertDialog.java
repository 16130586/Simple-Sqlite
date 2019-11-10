package com.t4.sqliteexample;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.t4.sqliteexample.Callback.OnCallBackRecevicerDialog;

public class CustomAlertDialog {

    public static void askForASimpleTextInput(final Context context,final String displayTitle , final OnCallBackRecevicerDialog callback, final int eventId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(displayTitle);

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.emitEvent(eventId , input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
