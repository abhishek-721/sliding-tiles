package com.creativeideas.slidingtiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {
    private static final String ARG_CATEGORY_ID = "param1";
    private static final String ARG_CATEGORY_TAG = "param2";

    private int mCategoryId;
    private int mCategoryTag;

    private Adapter adapter;

    private OnFragmentInteractionListener mListener;

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryId Parameter 1.
     * @return A new instance of fragment ImageFragment.
     */
    public static ImageFragment newInstance(int categoryId, int categoryTag) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        args.putInt(ARG_CATEGORY_TAG, categoryTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(ARG_CATEGORY_ID);
            mCategoryTag = getArguments().getInt(ARG_CATEGORY_TAG);
        }

        TypedArray imgArray = getResources().obtainTypedArray(mCategoryId);

        int[] imageArray = new int[imgArray.length()];

        for (int i = 0; i < imgArray.length(); i++) {
            imageArray[i] = imgArray.getResourceId(i, -1);
        }

        imgArray.recycle();

        adapter = new Adapter(imageArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        TextView textView = view.findViewById(R.id.textview);
        textView.setText(mCategoryTag);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
        recyclerView.setAdapter(adapter);

        MaterialButton back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBackClicked(v);
            }
        });

        return view;
    }

    private void onButtonPressed(int imgRes) {
        if (mListener != null) {
            mListener.onImageSelected(imgRes);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        void onImageSelected(int imgRes);

        void onBackClicked(View v);
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private int[] imgArray;

        Adapter(int[] imgArray) {
            this.imgArray = imgArray;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            int height = (parent.getWidth() - 24) / 3;
            itemView.setMinimumHeight(height);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            Glide.with(ImageFragment.this)
                    .load(imgArray[position])
                    .into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed(imgArray[position]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imgArray.length;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.imageView = itemView.findViewById(R.id.imageview);
            }
        }
    }
}
