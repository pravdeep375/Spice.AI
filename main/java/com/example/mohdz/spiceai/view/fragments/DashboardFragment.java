package com.example.mohdz.spiceai.view.fragments;

import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.mohdz.spiceai.view.interfaces.DashboardFragmentListener;

import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

/**
 * Created by zeeshan on 2017-10-30.
 */

public class DashboardFragment extends Fragment {


    private ArrayList<String> groceryLists;
    private ArrayList<Recipe> allRecipes;
    private ArrayList<RecipeDetail> recipeDetailList;

    private Button groceryList1, groceryList2, groceryList3;
    private TextView textViewRecipe1, textViewRecipe2, textViewRecipe3;

    private DashboardFragmentListener dashboardFragmentListener;

    public DashboardFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        MainActivity activity = (MainActivity) getActivity();

        recipeDetailList = activity.getRecipesToDisplay();
        groceryLists = activity.getGroceryListsToDisplay();
        allRecipes = activity.getAllRecipes();

        LinearLayout tileLayout = view.findViewById(R.id.tileLayout);
        LinearLayout layout = view.findViewById(R.id.groceryListLayout);

        if(tileLayout.getChildCount() > 0)
            tileLayout.removeAllViews();

        for (int i=0; i<groceryLists.size(); i++){
            Button b = new Button(getContext());
            b.setText(groceryLists.get(i));
            b.setId(i+1);
            b.setHeight(150);
            b.setTextColor(Color.GRAY);
            b.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            b.setPadding(60,40,0,40);
            b.setBackgroundResource(R.drawable.shadow);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dashboardFragmentListener.listButton((Button)v);
                }
            });

            layout.addView(b);
        }

        if(recipeDetailList != null && !recipeDetailList.isEmpty()){
            for(RecipeDetail r:recipeDetailList){
                View tile = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_recipe_tile, null);
                TextView t = tile.findViewById(R.id.recipeTileTitle);

                String uri = "@drawable/r"+r.getRecipeId();
                int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
                if(imageResource!=0){
                    Drawable res = getResources().getDrawable(imageResource);
                    ImageView i = tile.findViewById(R.id.recipeImage);
                    i.setImageDrawable(res);
                }

                t.setText(r.getRecipeName());
                tile.setTag(r.getRecipeId());
                tile.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        handleRecipeTileClicked(v);
                    }
                });
                tileLayout.addView(tile);
            }
        }else{
            View oops = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_not_found, null);
            tileLayout.addView(oops);
        }

        int allRepLen = allRecipes.size();
        String[] allRecipeNames = new String[allRepLen];
        for (int i = 0; i < allRepLen; i++)
            allRecipeNames[i] = allRecipes.get(i).getName();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, allRecipeNames);

        final AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String selected = (String) parent.getItemAtPosition(pos);
                textView.setText("");
                Integer findId = 0;
                for (int i = 0; i < allRecipes.size(); i++)
                    if (selected.equals(allRecipes.get(i).getName())) {
                        findId = allRecipes.get(i).getId();
                        break;
                    }
                dashboardFragmentListener.search(textView, findId);

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DashboardFragmentListener){
            dashboardFragmentListener = (DashboardFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dashboardFragmentListener = null;
    }

    @BindView(R.id.updateInventory)
    public Button updateInventory;

    @OnClick(R.id.updateInventory)
    public void handleUpdateInventoryClicked(){ dashboardFragmentListener.buttonPressed(updateInventory, 0); }
    public void handleRecipeTileClicked(View v){
        dashboardFragmentListener.buttonPressed(v, (int)v.getTag());
    }
}