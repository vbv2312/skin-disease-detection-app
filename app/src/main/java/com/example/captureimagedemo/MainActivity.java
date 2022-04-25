package com.example.captureimagedemo;

import static android.graphics.BitmapFactory.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.*;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView imageProfile;
    Button takePhoto, takegallery;
    public static final int CAMERA_ACTION_CODE = 1;
    public static final int SELECT_PICTURE = 200;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageProfile = findViewById(R.id.ivProfile);
        takePhoto = findViewById(R.id.btnPhoto);
        takegallery = findViewById(R.id.btngallery);
        takegallery.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        }));
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_ACTION_CODE);
                }
            }
        });
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap finalPhoto = null, finalpic = null;
        if (requestCode == CAMERA_ACTION_CODE && resultCode == RESULT_OK && data != null) //selecting from camera
        {
            Bundle bundle = data.getExtras();
            finalPhoto = (Bitmap) bundle.get("data");
            imageProfile.setImageBitmap(finalPhoto);
        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) //selecting from gallery
        {
            Uri selectedImageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                finalPhoto = decodeStream(imageStream);
                imageProfile.setImageBitmap(finalPhoto);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
        }

        if (finalPhoto != null) {
            finalpic = Bitmap.createScaledBitmap(finalPhoto, 128, 96, true);
            int w = finalpic.getWidth();
            int h = finalpic.getHeight();
            System.out.println(w+"  image width "+h+"image height");
            int[] pixels = new int[w * h];
            finalpic.getPixels(pixels, 0, w, 0, 0, w, h);

            int[][][] pixelarray = new int[h][w][3];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {

                    pixelarray[i][j][0] = Color.red(pixels[i * w + j]);
                    pixelarray[i][j][1] = Color.green(pixels[i * w + j]);
                    pixelarray[i][j][2] = Color.blue(pixels[i * w + j]);
                }
            }
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    System.out.println(pixelarray[i][j][0]+" red"+pixelarray[i][j][1]+"blue "+pixelarray[i][j][2]+"green\n");
                }
            }


        }
    }
}