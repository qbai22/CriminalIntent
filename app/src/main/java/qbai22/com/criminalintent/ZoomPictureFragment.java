package qbai22.com.criminalintent;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ZoomPictureFragment extends DialogFragment {

    private static final String ARG_PATH = "path";
    private ImageView mZoomedImageView;

    public static ZoomPictureFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);

        ZoomPictureFragment fragment = new ZoomPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.zoom_picture_constrain, container, false);
        getDialog().setTitle("Crime scene");
        String path = getArguments().getString(ARG_PATH);

        mZoomedImageView = (ImageView) v.findViewById(R.id.zoomed_picture);
        Bitmap bitmap = PictureUtils.getScaledBitmap(path, getActivity());
        mZoomedImageView.setImageBitmap(bitmap);

        return v;
    }
}
