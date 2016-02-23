package com.newera.wearme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class customize extends AppCompatActivity {

    private android.widget.GridLayout.LayoutParams layoutParams;
    private long backPressedTime = 0;

    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    LinearLayout initialSettings;
    LinearLayout textSettings;
    LinearLayout pictureSettings;
    LinearLayout shapeSettings;
    ImageView imageView;

    //Stuff for camera
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    List<TextView> editText = new ArrayList<TextView>();
    private static final String TEXTVIEW_TAG = "icon bitmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        Intent intent = getIntent();

        //Recieve shirt type in form of string "1" "2" or "3" and assign image
        String shirtType = intent.getStringExtra(tShirtType.EXTRA_MESSAGE);
        imageView = (ImageView) findViewById(R.id.shirt);

        if(Integer.parseInt(shirtType) == 1) {
            imageView.setImageResource(R.drawable.plainwhiteshirt);
        }
        else if(Integer.parseInt(shirtType) == 2) {
            imageView.setImageResource(R.drawable.tanktop);
        }
        else if(Integer.parseInt(shirtType) == 3) {
            imageView.setImageResource(R.drawable.plainwhitecubanstyle);
        }

    }

    //Function to show Text Settings Buttons
    public void showTextSettings(View view) {
        initialSettings = (LinearLayout) findViewById(R.id.initialSettings);
        textSettings = (LinearLayout) findViewById(R.id.textSettings);
        initialSettings.setVisibility(View.GONE);
        textSettings.setVisibility(View.VISIBLE);
    }



    //This method is used for color picking
    public void colorPicker(View view) {
        CharSequence colors[] = new CharSequence[] {"black", "blue", "green", "grey", "orange", "pink", "purple", "red", "white"};
        final List<Integer> colorList = new ArrayList<Integer>();
        colorList.add(R.color.Black); //black
        colorList.add(R.color.Blue); //blue
        colorList.add(R.color.Green); //green
        colorList.add(R.color.Grey); //grey
        colorList.add(R.color.Orange); //orange
        colorList.add(R.color.Pink); //pink
        colorList.add(R.color.Purple);//purple
        colorList.add(R.color.Red);//red
        colorList.add(R.color.White);//white

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Text Color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button colorButton = (Button) findViewById(R.id.color);
                colorButton.setTextColor(getResources().getColor(colorList.get(which)));
                editText.get(editText.size()-1).setTextColor(getResources().getColor(colorList.get(which)));
            }
        });
        builder.show();
    }

    public void format(View view) {

        CharSequence formats[] = new CharSequence[] {"Bold", "Italics", "Bold-Italics"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Text Decoration");
        builder.setItems(formats, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    editText.get(editText.size() - 1).setTypeface(Typeface.DEFAULT_BOLD);
                } else if (which == 1) {
                    editText.get(editText.size() - 1).setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                } else {
                    editText.get(editText.size() - 1).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                }
            }
        });
        builder.show();

    }

    public void addTextBox(View view){
        editText.add(new TextView(this));
        editText.get(editText.size()-1).setHint("Edit Text");
        editText.get(editText.size()-1).setTextSize(20);
        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridEditText);
        gridLayout.addView(editText.get(editText.size() - 1));
        editText.get(editText.size()-1).setTag(TEXTVIEW_TAG);
        editText.get(editText.size()-1).setVisibility(View.VISIBLE);
        editText.get(editText.size() - 1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.get(editText.size() - 1).setCursorVisible(true);
                editText.get(editText.size() - 1).setFocusableInTouchMode(true);
                editText.get(editText.size() - 1).setInputType(InputType.TYPE_CLASS_TEXT);
                editText.get(editText.size() - 1).requestFocus();
            }
        });

        editText.get(editText.size()-1).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(editText.get(editText.size() - 1));

                v.startDrag(dragData, myShadow, null, 0);
                return true;
            }
        });

        editText.get(editText.size()-1).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int x_cord = (int) event.getX();
                int y_cord = (int) event.getY();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (GridLayout.LayoutParams) v.getLayoutParams();
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        String s = "x: " + event.getX() + " y: " + event.getY();
                        Log.v("customize", s + "drag entered");
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if(x_cord < 0 || y_cord < 0) {
                        v.setX(x_cord + editText.get(editText.size() - 1).getLeft());
                        v.setY(y_cord + editText.get(editText.size()-1).getTop()); }
                        else {
                            v.setX(x_cord);
                            v.setY(y_cord);
                        }
                        String s2 = "x: " + (x_cord) + " y: " + (y_cord);
                        Log.v("customize", s2 + "drag exited");
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        String s3 = "x: " + event.getX() + " y: " + event.getY();
                        Log.v("customize", s3 + "drag location");
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    case DragEvent.ACTION_DROP:
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        initialSettings = (LinearLayout) findViewById(R.id.initialSettings);
        textSettings = (LinearLayout) findViewById(R.id.textSettings);
        pictureSettings = (LinearLayout) findViewById(R.id.pictureSettings);
        shapeSettings = (LinearLayout) findViewById(R.id.shapeSettings);

        if(initialSettings.getVisibility() == View.GONE) {
            textSettings.setVisibility(View.GONE);
            pictureSettings.setVisibility(View.GONE);
            shapeSettings.setVisibility(View.GONE);
            initialSettings.setVisibility(View.VISIBLE);
        }
        else {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {    // 2 secs
                backPressedTime = t;
                Toast.makeText(this, "Press back again to leave. You will lose all work.",
                        Toast.LENGTH_SHORT).show();
            } else {    // this guy is serious
                // clean up
                super.onBackPressed();       // bye
            }
        }
    }


    //Switch to Picture Buttons
    public void showPictureSettings(View view) {
        initialSettings = (LinearLayout) findViewById(R.id.initialSettings);
        pictureSettings = (LinearLayout) findViewById(R.id.pictureSettings);
        initialSettings.setVisibility(View.GONE);
        pictureSettings.setVisibility(View.VISIBLE);
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    //Take a picture
    public void takePicture(View view) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // start the image capture Intent
        startActivityForResult(pictureIntent, CAMERA_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView = (ImageView) findViewById(R.id.cameraPicture);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(loadedBitmap, 0, 0, loadedBitmap.getWidth(), loadedBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
            imageView.setVisibility(View.VISIBLE);
        }

    }

    //Choose a picture from gallery
    public void chooseFromGallery(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    //Show Buttons for Shape
    public void showShapeSettings(View view) {
        initialSettings = (LinearLayout) findViewById(R.id.initialSettings);
        shapeSettings = (LinearLayout) findViewById(R.id.shapeSettings);
        initialSettings.setVisibility(View.GONE);
        shapeSettings.setVisibility(View.VISIBLE);
    }


    //Draw a Circle
    public void drawCircle(View view) {

    }

    //Draw a Triangle
    public void drawTriangle(View view) {

    }

    //Draw a Square
    public void drawSquare(View view) {

    }


}
