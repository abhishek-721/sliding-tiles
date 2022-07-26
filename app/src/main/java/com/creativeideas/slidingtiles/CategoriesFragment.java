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

public class CategoriesFragment extends Fragment {

    private Adapter adapter;

    private OnFragmentInteractionListener mListener;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedArray idArray = getResources().obtainTypedArray(R.array.categoryIds);
        TypedArray resArray = getResources().obtainTypedArray(R.array.categoryDrawables);
        TypedArray tagsArray = getResources().obtainTypedArray(R.array.categoryTags);

        int[] id = new int[idArray.length()];
        int[] res = new int[resArray.length()];
        int[] tags = new int[tagsArray.length()];

        for (int i = 0; i < idArray.length(); i++) {
            id[i] = idArray.getResourceId(i, -1);
            res[i] = resArray.getResourceId(i, -1);
            tags[i] = tagsArray.getResourceId(i, -1);
        }

        idArray.recycle();
        resArray.recycle();
        tagsArray.recycle();

        adapter = new Adapter(res, id, tags);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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

    private void onItemPressed(int id, int tag) {
        if (mListener != null) {
            mListener.onCategorySelected(id, tag);
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
        void onCategorySelected(int id, int tag);

        void onBackClicked(View v);
    }

    private class Adapter extends RecyclerView.Adapter<CategoriesFragment.Adapter.ViewHolder> {
        private int[] imgArray;
        private int[] categoryId;
        private int[] categoryTag;

        Adapter(int[] imgArray, int[] categoryId, int[] categoryTag) {
            this.imgArray = imgArray;
            this.categoryId = categoryId;
            this.categoryTag = categoryTag;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item_view, parent, false);
            return new Adapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, final int position) {
            Glide.with(CategoriesFragment.this)
                    .load(imgArray[position])
                    .into(holder.imageView);
            holder.textView.setText(categoryTag[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemPressed(categoryId[position], categoryTag[position]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imgArray.length;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView textView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.imageView = itemView.findViewById(R.id.imageview);
                this.textView = itemView.findViewById(R.id.textview);
            }
        }
    }
}
