package com.example.android.expnesify;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.MetadataChangeSet;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;

public class exportData extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = "drive-quickstart";
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    private GoogleApiClient mGoogleApiClient;
    private Bitmap mBitmapToSave;

    /**
     * Create a new file and save it to Drive.
     */
    void saveToDrive(final DriveFolder pFldr, final String titl,
                     final String mime, final java.io.File file) {
        if (getGoogleApiClient() != null && pFldr != null && titl != null && mime != null && file != null) try {
            // create content from file
            Drive.DriveApi.newDriveContents(getGoogleApiClient()).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
                    DriveContents cont = driveContentsResult != null && driveContentsResult.getStatus().isSuccess() ?
                            driveContentsResult.getDriveContents() : null;

                    // write file to content, chunk by chunk
                    if (cont != null) try {
                        OutputStream oos = cont.getOutputStream();
                        if (oos != null) try {
                            InputStream is = new FileInputStream(file);
                            byte[] buf = new byte[4096];
                            int c;
                            while ((c = is.read(buf, 0, buf.length)) > 0) {
                                oos.write(buf, 0, c);
                                oos.flush();
                            }
                        }
                        finally { oos.close();}

                        // content's COOL, create metadata
                        MetadataChangeSet meta = new MetadataChangeSet.Builder().setTitle(titl).setMimeType(mime).build();

                        // now create file on GooDrive
                        pFldr.createFile(getGoogleApiClient(), meta, cont).setResultCallback(new ResultCallback<DriveFileResult>() {
                            @Override
                            public void onResult(DriveFileResult driveFileResult) {
                                if (driveFileResult != null && driveFileResult.getStatus().isSuccess()) {
                                    DriveFile dFil = driveFileResult != null && driveFileResult.getStatus().isSuccess() ?
                                            driveFileResult.getDriveFile() : null;
                                    if (dFil != null) {
                                        // BINGO , file uploaded
                                        dFil.getMetadata(getGoogleApiClient()).setResultCallback(new ResultCallback<DriveResource.MetadataResult>() {
                                            @Override
                                            public void onResult(DriveResource.MetadataResult metadataResult) {
                                                if (metadataResult != null && metadataResult.getStatus().isSuccess()) {
                                                    DriveId mDriveId = metadataResult.getMetadata().getDriveId();
                                                }
                                            }
                                        });
                                    }
                                } else { /* report error */     }
                            }
                        });
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
        } catch (Exception e) { e.printStackTrace(); }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
//        if (mBitmapToSave == null) {
//            // This activity has no UI of its own. Just start the camera.
//            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
//                    REQUEST_CODE_CAPTURE_IMAGE);
//            return;
//        }
        saveToDrive(
                Drive.DriveApi.getAppFolder(getGoogleApiClient()),
                "MyDbFile.db",
                "application/x-sqlite3",
                new java.io.File("\\data\\data\\com.example.android.expnesify\\databases\\expense.db")
        );
    }

    private GoogleApiClient getGoogleApiClient() {
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect();
        return mGoogleApiClient;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }
}
