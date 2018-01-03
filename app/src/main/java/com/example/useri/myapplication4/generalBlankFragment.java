package com.example.useri.myapplication4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.PriorityQueue;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link generalBlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link generalBlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class generalBlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GALLERY_REQUEST=1;
    private ImageButton image;
    private EditText name;
    private EditText price;
    private Button submit;
    private Uri image1=null;
    private StorageReference storage;

    private DatabaseReference database;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;

    public generalBlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment generalBlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static generalBlankFragment newInstance(String param1, String param2) {
        generalBlankFragment fragment = new generalBlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_general_blank, container, false);
          image=(ImageButton)view.findViewById(R.id.img);
          name=(EditText)view.findViewById(R.id.item);
          price=(EditText)view.findViewById(R.id.price);
          submit=(Button)view.findViewById(R.id.post);



          storage= FirebaseStorage.getInstance().getReference();
          database= FirebaseDatabase.getInstance().getReference().child("Posts");

         image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent gallery=new Intent(Intent.ACTION_GET_CONTENT);
                 gallery.setType("image/*");
                 startActivityForResult(gallery,GALLERY_REQUEST);
             }
         });
         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 startPosting();

             }
         });


return view;
    }

    private void startPosting() {

         final String item=name.getText().toString().trim();
         final String cost=price.getText().toString().trim();

         if(!TextUtils.isEmpty(item) && !TextUtils.isEmpty(cost) && image1!=null) {
             Toast.makeText(getActivity().getApplication(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
            StorageReference filepath=storage.child("Posts").child(image1.getLastPathSegment());


             filepath.putFile(image1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadurl=taskSnapshot.getDownloadUrl();
                    DatabaseReference post=database.push();
                    post.child("item").setValue(item);
                    post.child("price").setValue(cost);
                    post.child("image").setValue(downloadurl.toString());


                }
            });
         }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK);
        image1=data.getData();
        image.setImageURI(image1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context,"Home",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
