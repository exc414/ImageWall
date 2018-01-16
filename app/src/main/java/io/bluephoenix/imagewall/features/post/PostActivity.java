package io.bluephoenix.imagewall.features.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import butterknife.BindView;
import io.bluephoenix.imagewall.BuildConfig;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.common.PresenterDef;
import io.bluephoenix.imagewall.features.base.BaseActivity;
import io.bluephoenix.imagewall.util.Constant;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class PostActivity extends BaseActivity implements IPostContract.PublishToView
{
    //Result codes
    public static final int REQUEST_GALLERY = 1000;
    public static final int REQUEST_TAKE_PHOTO = 1001;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgDisplayPhoto)
    ImageView imgDisplayPhoto;

    @BindView(R.id.constraintPostLayout)
    ConstraintLayout constraintLayout;

    private PostPresenter postPresenter;
    private String selectedPhotoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setToolbarBackButton(toolbar, getString(R.string.post_activity_name));

        postPresenter = attachPresenter(PostPresenter.class, PresenterDef.POST);
        postPresenter.attachView(this);
        Log.d(Constant.TAG, "onCreate : " + getClass().getName());
    }

    /**
     * Create the menu.
     *
     * @param menu A menu to inflate.
     * @return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }

    /**
     * If the back button on the UI (not the key on the phone itself) is pressed
     * return to previous activity.
     *
     * @param item A menu item which was clicked.
     * @return A menu item which was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.addImageFromCamera) { selectPhotoFromCamera(); }
        else if(id == R.id.addImageFromGallery) { selectPhotoFromGallery(); }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieves the result from the activity that has ended.
     *
     * @param requestCode An int with which the ending activity was called with.
     * @param resultCode  An int that says whether the action preform was successful.
     * @param data        An intent with the retrieve data.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_TAKE_PHOTO)
            {
                //Null here is passed because instead of getting the image information from
                //a content provider input stream, the info is retrieved from the path set
                //in the createImageFile() method.
                BitmapFactory.Options bmOptions = setBMOptions(null);
                addPhotoToImageView(null, bmOptions);
            }
            else if(requestCode == REQUEST_GALLERY)
            {
                try
                {
                    //Need to input streams as you can only read it once. The next time
                    //a read is performed the cursor will already be at the end and thus
                    //return nothing. Using mark() and reset() did not work properly.
                    InputStream is1 = getContentResolver().openInputStream(data.getData());
                    InputStream is2 = getContentResolver().openInputStream(data.getData());

                    BitmapFactory.Options bmOptions = setBMOptions(is1);
                    addPhotoToImageView(is2, bmOptions);
                }
                catch(FileNotFoundException | NullPointerException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Take a photo using an intent and the user's default camera application.
     * Requires Camera and Storage (Read/Write) permissions.
     */
    public void selectPhotoFromCamera()
    {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Ensure that there's a camera activity to handle the intent
        if(takePhotoIntent.resolveActivity(getPackageManager()) != null)
        {
            //Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch(IOException ex)
            {
                //Show message letting the user know that permissions must be granted
                Snackbar.make(constraintLayout,
                        getString(R.string.request_permission_snack_bar),
                        Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorPrimaryLight))
                        .setAction("GRANT", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                //Opens the settings for the application.
                                Intent settings = new Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + getPackageName()));
                                start(settings);
                            }
                        }).show();

                Log.e(Constant.TAG, "The app does not have permissions to create a file.");
                ex.printStackTrace();
            }

            //Continue only if the File was successfully created.
            if(photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                start(takePhotoIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Allow the user to select a photo from the gallery (or any other application
     * that can handle the intent).
     * Requires Storage(Read/Write) permission.
     */
    public void selectPhotoFromGallery()
    {
        //TODO check permission for storage here or else it will crash.
        //Intent action type
        String intentType = "image/*";

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType(intentType);

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(intentType);

        Intent chooserIntent = Intent.createChooser(getIntent, "Select an image : ");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { pickIntent });

        start(chooserIntent, REQUEST_GALLERY);
    }

    /**
     * Adds the retrieve photo to an image view.
     *
     * @param imageStream Provides the image information.
     * @param bmOptions   A bitmap options object containing the proper scaling for the photo.
     */
    private void addPhotoToImageView(InputStream imageStream, BitmapFactory.Options bmOptions)
    {
        //Make the image view visible first.
        imgDisplayPhoto.setVisibility(View.VISIBLE);
        Bitmap bitmap;

        if(imageStream != null)
        {
            bitmap = BitmapFactory.decodeStream(imageStream, null, bmOptions);
        }
        else
        {
            bitmap = BitmapFactory.decodeFile(selectedPhotoPath, bmOptions);
        }

        RoundedBitmapDrawable roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(10);
        imgDisplayPhoto.setImageDrawable(roundedBitmapDrawable);

        //The photo is set horizontal, therefore we must rotate 90 degrees.
        //imgDisplayPhoto.setRotation(90);
    }


    /**
     * Create the file for the image that the user will take.
     * Note this part is needed because if done with an intent and no file
     * creation. The picture in the preview of the user's camera will be
     * downgraded (meaning it looks awful) as the phone is using a thumbnail
     * instead of the full picture. Thus we must save the picture and that
     * will be used in the preview.
     *
     * @return A file object which contains the photo's name and path.
     * @throws IOException when the file cannot be created.
     */
    private File createImageFile() throws IOException
    {
        //Create an image file name
        long timeStamp = (Calendar.getInstance().getTimeInMillis() / 1000);
        String imageFileName = "ImageWall_" + timeStamp + "_";

        //Create an image path
        File storageDir = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);

        //If the storage dir does not exist make it.
        if(!storageDir.exists())
        {
            //If it cannot be made throw an exception.
            if(!storageDir.mkdirs()) { throw new IOException(); }
        }

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        selectedPhotoPath = image.getAbsolutePath();

        return image;
    }

    /**
     * Scale the image retrieve either from the camera or the gallery.
     *
     * @param imageStream Provides the image information.
     * @return a bitmap options object containing the proper scaling for the photo.
     */
    private BitmapFactory.Options setBMOptions(InputStream imageStream)
    {
        //Get the dimensions of the View
        int targetW = imgDisplayPhoto.getWidth();
        int targetH = imgDisplayPhoto.getHeight();

        int photoW;
        int photoH;

        //Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        if(imageStream != null)
        {
            Bitmap tempBitmap = BitmapFactory.decodeStream(imageStream);
            photoW = tempBitmap.getWidth();
            photoH = tempBitmap.getHeight();

            bmOptions.outWidth = tempBitmap.getWidth();
            bmOptions.outHeight = tempBitmap.getHeight();
        }
        else
        {
            BitmapFactory.decodeFile(selectedPhotoPath, bmOptions);
            photoW = bmOptions.outWidth;
            photoH = bmOptions.outHeight;
        }

        //Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        //Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return bmOptions;
    }
}