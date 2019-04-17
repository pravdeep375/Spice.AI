package com.example.mohdz.spiceai.view.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mohdz.spiceai.R;
import com.example.mohdz.spiceai.model.Recipe;
import com.example.mohdz.spiceai.model.RecipeDetail;
import com.example.mohdz.spiceai.view.MainActivity;
import com.example.mohdz.spiceai.view.interfaces.RecipeFragmentListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zeeshan on 2017-10-30.
 */

public class RecipeFragment extends Fragment {

    private RecipeDetail recipe;
    private TextView recipeTitle;
    private Boolean hideButton;

    private RecipeFragmentListener recipeFragmentListener;
    public RecipeFragment(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);
        MainActivity activity = (MainActivity) getActivity();

        recipe = activity.getRecipeDetails();
        hideButton = activity.hideButton();

        if(hideButton){
            createListButton.setVisibility(View.GONE);
        }

        String uri = "@drawable/r"+recipe.getRecipeId();
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        if(imageResource!=0){
            Drawable res = getResources().getDrawable(imageResource);
            ImageView i = view.findViewById(R.id.myImageView);
            i.setImageDrawable(res);
        }

        recipeTitle = view.findViewById(R.id.recipeTitle);
        recipeTitle.setText(recipe.getRecipeName());

        LinearLayout ll = view.findViewById(R.id.ingredientsLayout);
        for(String s : recipe.getIngredients()){
            TextView tv = new TextView(getContext());
            tv.setText(s);
            tv.setPadding(60, 10, 60, 10);
            ll.addView(tv);
        }

        ll = view.findViewById(R.id.directionsLayout);
        for(String s : recipe.getDirections()){
            TextView tv = new TextView(getContext());
            tv.setText(s);
            tv.setPadding(60, 10, 60, 10);
            ll.addView(tv);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RecipeFragmentListener){
            recipeFragmentListener = (RecipeFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recipeFragmentListener = null;
    }

    @BindView(R.id.create_list_button)
    public Button createListButton;

    @OnClick(R.id.create_list_button)
    public void handleCreateListClicked(){ recipeFragmentListener.createList(createListButton, recipe); }
}
