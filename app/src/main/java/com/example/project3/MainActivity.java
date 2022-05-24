package com.example.project3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    public int NotPrio;
    private FileChooserFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.fragment = (FileChooserFragment) fragmentManager.findFragmentById(R.id.fragment_fileChooser);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.NotificationPriority,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(2);
        MaterialButton button1 = findViewById(R.id.button1);
        MaterialButton button2 = findViewById(R.id.button2);
        button1.setOnClickListener(v -> {
            int pos = spinner.getSelectedItemPosition();
            switch(pos){
                case 0: NotPrio = NotificationCompat.PRIORITY_MAX;
                    break;
                case 1: NotPrio = NotificationCompat.PRIORITY_HIGH;
                    break;
                case 2: NotPrio = NotificationCompat.PRIORITY_DEFAULT;
                    break;
                case 3: NotPrio = NotificationCompat.PRIORITY_LOW;
                    break;
                case 4: NotPrio = NotificationCompat.PRIORITY_MIN;
            }
            String db_path = fragment.getPath();
            System.out.println(db_path);
            if(!db_path.equals("")){
                //InfoToClasses.DB_NAME = "dict2.db";
                InfoToClasses.DB_ABS_PATH = db_path;
            }
            InfoToClasses.NotPrio = NotPrio;
            startService(new Intent(MainActivity.this, BroadcastService.class));
        });
        button2.setOnClickListener(v -> {
            stopService(new Intent(MainActivity.this, BroadcastService.class));
            //showInfo();
        });
    }
    private void showInfo()  {
        String path = this.fragment.getPath();
        Toast.makeText(this, "Path: " + path, Toast.LENGTH_LONG).show();
    }
}