package cmu.rrg.pi.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import cmu.rrg.pi.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TitleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TitleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TitleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        return view;
    }

//   public void onCreate(Bundle saveInstanceState) {
//       super.onCreate(saveInstanceState);
//   }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_title, null);
//        Button leftButton = (Button) view.findViewById(R.id.leftButton);
//        Button rightButton = (Button) view.findViewById(R.id.rightButton);
//
//        leftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "我是fragment", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
}
