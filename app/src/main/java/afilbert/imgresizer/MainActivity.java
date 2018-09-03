package afilbert.imgresizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    final Integer GALLERY_ADD_IMAGE = 0;
    final Integer CAMERA_ADD_IMAGE = 1;
    final int IMAGE_MAX_BYTE_SIZE = 65536; //26214400


    private ImageView currentImg;
    private TextView openText;
    private int resScale = 100;
    private int jpegQuality = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentImg = findViewById(R.id.imagePreview);
        //currentImg.setImageResource(0);

        openText = findViewById(R.id.addImageText);

        SeekBar resScaleSlider = (SeekBar)findViewById(R.id.resolutionScaleSlider);
        SeekBar jpegQualitySlider = (SeekBar)findViewById(R.id.jpegQualitySlider);
        resScaleSlider.setMax(99);
        resScaleSlider.setMax(99);

        resScaleSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //
                resScale = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });

        jpegQualitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //
                jpegQuality = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        if (resultCode == RESULT_OK) {
            if (requestCode == this.CAMERA_ADD_IMAGE || requestCode == this.GALLERY_ADD_IMAGE) {
                // get image
                Image image;
                Uri selectedImage = returnedIntent.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                float newWidth = 512;
                float newHeight = (float) bitmap.getHeight() / ((float) bitmap.getWidth() / newWidth);

                Log.i("width", String.valueOf(newWidth));
                Log.i("height", String.valueOf(newHeight));

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) newWidth, (int) newHeight, true);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] bitmapdata = bos.toByteArray();
                long bitmapSize = bitmapdata.length;
                // check img size
                if (bitmapSize < IMAGE_MAX_BYTE_SIZE) {
                    // store
                    image = new Image(resizedBitmap);
                    Log.i("ImageSize1", String.valueOf(bitmapSize));
                    Log.i("ImageSizeMax", String.valueOf(IMAGE_MAX_BYTE_SIZE));
                }
                else {
                    image = new Image(resizedBitmap);
                    int quality = 45;
                    while (quality > 0){
                        bos.reset();
                        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                        bitmapdata = bos.toByteArray();
                        bitmapSize = bitmapdata.length;
                        Log.i("ImageSizeInLoop", String.valueOf(bitmapSize));
                        if (bitmapSize < IMAGE_MAX_BYTE_SIZE){
                            Log.i("ImageSizeInLoop", String.valueOf(bitmapSize));
                            image = new Image(resizedBitmap);
                            break;
                        }
                        quality = quality - 5;
                    }
                }

                currentImg.setImageBitmap(image.getImageBitmap());
                currentImg.setPadding(0,0,0,0);
                openText.setVisibility(View.INVISIBLE);


            }
            else {
                throw new IllegalStateException();
            }
        }
    }

    private int sizeOf(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public void saveButtonClick(View v) {
        Log.d("resScale", String.valueOf(resScale));
        Log.d("jpeqQuality", String.valueOf(jpegQuality));

    }

    public void previewButtonClick(View v) {

    }

    public void openImgClick(View v) {
        String title = "Add Photo";
        String message = "Pick from gallery or take new photo?";
        String positive = "Gallery", neutral = "Cancel", negative = "Camera";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);
            }
        });
        builder.setNeutralButton(neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }


}


