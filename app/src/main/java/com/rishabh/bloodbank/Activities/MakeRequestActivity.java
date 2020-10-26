package com.rishabh.bloodbank.Activities;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.preference.PreferenceManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.rishabh.bloodbank.R;
import com.rishabh.bloodbank.Utils.EndPoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MakeRequestActivity extends AppCompatActivity {
    EditText message;
    TextView img;
    ImageView image;
    Button post_req;
     Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        AndroidNetworking.initialize(getApplicationContext());
        message =(EditText) findViewById(R.id.message);
        image = (ImageView) findViewById(R.id.image);
        img=findViewById(R.id.img);
        post_req=findViewById(R.id.post_req);

        post_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                   uploadRequest(message.getText().toString());
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permission();
            }
        });

    }
        private void pickImage(){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,101);
        }
            private void permission(){

                if(PermissionChecker.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE)!=PermissionChecker.PERMISSION_GRANTED){

                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE},401);
                }
                else{
                    //If Permission already There
                    pickImage();

                }
            }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 401){

            if(grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                //Permission was granted
                pickImage();
            }
            else{
                // Permission Not Granted
                showMessage(" Permission Declined");
            }
        }
    }

    private void uploadRequest(String message){

            String path = "";

            try {

                    path =getPath(imageUri);
            }catch (URISyntaxException e){
                showMessage(" Wrong Path");
            }

    String number1= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("number1","12345");
        AndroidNetworking.upload(EndPoints.upload_request)
                .addMultipartFile("file",new File(path))
                .addQueryParameter("message" , message)
                .addQueryParameter("number1" , number1)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        long progress = (bytesUploaded / totalBytes) * 100;
                        img.setText(String.valueOf(progress + "%"));
                        img.setOnClickListener(null);
                        Toast.makeText(getApplicationContext(), "Uploading an Image ...", Toast.LENGTH_SHORT).show();

                    }
                })
                 .getAsJSONObject(new JSONObjectRequestListener() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           if(response.getBoolean("success")){
                               showMessage("Successful");
                               MakeRequestActivity.this.finish();
                           }
                           else{
                               showMessage("Message Failed");
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }

                   @Override
                   public void onError(ANError anError) {

                   }
               });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){

            if(data!=null){
               imageUri= data.getData();
               Glide.with(getApplicationContext()).load(imageUri).into(image);
            }


        }
    }

    private  boolean isValid(){

            if(message.getText().toString().isEmpty()){

             showMessage(" Message Should'nt be Empty");
                return false;
            } else if(imageUri ==null){

                showMessage(" Please Upload An Image ");
                return false;
            }
            return true;

    }

    private void showMessage(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("NewApi")
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



}
