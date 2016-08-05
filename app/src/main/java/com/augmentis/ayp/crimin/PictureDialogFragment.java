package com.augmentis.ayp.crimin;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Amita on 7/28/2016.
 */
public class PictureDialogFragment extends DialogFragment{

    private static final String CRIME_PHOTO = "CrimeFragment.CRIME_PHOTO";
    private File photoFile;
    private ImageView imageView;

    public static PictureDialogFragment newInstance(File file){
        PictureDialogFragment pictureDialogFragment = new PictureDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_PHOTO", file);
        pictureDialogFragment.setArguments(args);
        return pictureDialogFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //3.
        photoFile = (File) getArguments().getSerializable("ARG_PHOTO");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_picture_dialog ,null);

        imageView = (ImageView) v.findViewById(R.id.photo_dialog);

        Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
        imageView.setImageBitmap(bitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.app_name);
        builder.setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }
}
