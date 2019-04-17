package com.example.mohdz.spiceai.view.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohdz.spiceai.R;
import com.example.mohdz.spiceai.model.Ingredient;
import com.example.mohdz.spiceai.model.IngredientListImpl;
import com.example.mohdz.spiceai.view.MainActivity;
import com.example.mohdz.spiceai.view.interfaces.IngredientListFragmentListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zeeshan on 2017-10-30.
 */

public class IngredientListFragment extends Fragment {

    private IngredientListImpl ingredientList;
    private ArrayList<Ingredient> checkedItems;
    private IngredientListFragmentListener ingredientListFragmentListener;
    private ArrayList<Ingredient> allIngredients;
    private ArrayList<Ingredient> newItems;

    public IngredientListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        ButterKnife.bind(this, view);

        MainActivity activity = (MainActivity) getActivity();

        ingredientList = activity.getIngredientList();
        TextView title = view.findViewById(R.id.ingredientListTitle);
        title.setText(ingredientList.getName());

        checkedItems = new ArrayList<>();
        newItems = new ArrayList<>();

        LinearLayout layout = view.findViewById(R.id.vlayout);
        for (Ingredient i : ingredientList.getIngredients()){
            CheckBox cb = new CheckBox(getContext());
            cb.setText(i.getName());
            cb.setPadding(10, 10, 10, 10);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        for (Ingredient i : ingredientList.getIngredients()){
                            if(i.getName().equals(compoundButton.getText().toString())){
                                checkedItems.add(i);
                                break;
                            }
                        }
                    }else{
                        for (Ingredient i : checkedItems){
                            if(i.getName().equals(compoundButton.getText().toString())){
                                checkedItems.remove(i);
                                break;
                            }
                        }
                    }
                }
            });
            layout.addView(cb);
        }
        allIngredients = activity.getAllIngredients();

        int ingLen = allIngredients.size();
        String[] allIngredientNames = new String[ingLen];
        for (int i = 0; i < ingLen; i++)
            allIngredientNames[i] = allIngredients.get(i).getName();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, allIngredientNames);

        final AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.AddItemAutoCompleteTextView);
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String selected = (String) parent.getItemAtPosition(pos);
                textView.setText("");
                for (int i = 0; i < allIngredients.size(); i++)
                    if (selected.equals(allIngredients.get(i).getName())) {
                        newItems.add(new Ingredient(allIngredients.get(i).getName(),allIngredients.get(i).getId()));
                        break;
                    }
                ingredientListFragmentListener.updateList(AddItemAutoCompleteTextView, ingredientList.getName(), newItems);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(IngredientListFragment.this).attach(IngredientListFragment.this).commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof IngredientListFragmentListener){
            ingredientListFragmentListener = (IngredientListFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ingredientListFragmentListener = null;
    }

    @BindView(R.id.AddItemAutoCompleteTextView)
    public AutoCompleteTextView AddItemAutoCompleteTextView;

    @BindView(R.id.deleteButton)
    public Button deleteButton;

    @OnClick(R.id.deleteButton)
    public void handleDeleteButtonClicked(){
        ingredientListFragmentListener.updateList(deleteButton, ingredientList.getName(), checkedItems);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
