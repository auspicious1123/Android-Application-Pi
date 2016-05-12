package cmu.rrg.pi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmu.rrg.pi.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomFragmentProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BottomFragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomFragmentProfile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_fragment_profile, container, false);
        return view;
    }
}
