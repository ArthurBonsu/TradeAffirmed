package com.simcoder.bimbo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class WriteSettingsPermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_settings_permission);

        setTitle("dev2qa.com - Add Write Settings Permission Example");

        // Get the button and add onClickListener.
        final Button writeSettingsPermissionButton = (Button)findViewById(R.id.write_settings_permission_button);
        writeSettingsPermissionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                Context context = getApplicationContext();

                // Check whether has the write settings permission or not.
                boolean settingsCanWrite = Settings.System.canWrite(context);

                if(!settingsCanWrite) {
                    // If do not have write settings permission then open the Can modify system settings panel.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    startActivity(intent);
                }else {
                    // If has permission then show an alert dialog with message.
                    AlertDialog alertDialog = new AlertDialog.Builder(WriteSettingsPermissionActivity.this).create();
                    alertDialog.setMessage("You have system write settings permission now.");
                    alertDialog.show();
                }
            }
        });
    }
}