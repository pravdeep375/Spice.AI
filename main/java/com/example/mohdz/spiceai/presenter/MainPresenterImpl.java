package com.example.mohdz.spiceai.presenter;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mohdz.spiceai.model.Ingredient;
import com.example.mohdz.spiceai.model.IngredientListImpl;
import com.example.mohdz.spiceai.model.Recipe;
import com.example.mohdz.spiceai.model.RecipeDetail;
import com.example.mohdz.spiceai.presenter.enums.MainViewState;
import com.example.mohdz.spiceai.presenter.interfaces.MainPresenter;
import com.example.mohdz.spiceai.view.interfaces.MainView;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.net.ConnectException;
import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

/**
 * Created by zeeshan on 2017-10-28.
 */

public class MainPresenterImpl extends MvpBasePresenter<MainView> implements MainPresenter {

    private RequestAPI requestAPI;
    private MainView view;
    private MainViewState viewState;
    private boolean initialized = false;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private ArrayList<RecipeDetail>  recipesToDisplay = new ArrayList<>();
    private IngredientListImpl inventory, list;
    private ArrayList<String> groceryListsToDisplay;
    private RecipeDetail searchedRecipe;
    private ArrayList<IngredientListImpl> groceryLists;
    private ArrayList<Ingredient> allIngredients;
    private ArrayList<Recipe>  allRecipes;

    @Override
    public void attachView(MainView view){
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance){
        view = null;
    }

    @Override
    public void onStart() {
        try {
            initData();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        if(isViewAttached()) {
            getView().switchFragment(MainViewState.DASHBOARD_FRAGMENT);
        }
        if(!initialized){
            view.switchFragment(MainViewState.DASHBOARD_FRAGMENT);
            initialized = true;
        }
        view.loadAllRecipes(allRecipes);
        view.loadGroceryListsToDisplay(groceryListsToDisplay);
        view.loadAllIngredients(allIngredients);
    }

    @Override
    public void toolbarBackButtonPressed() {
        viewState = MainViewState.DASHBOARD_FRAGMENT;
        view.switchFragment(viewState);
        view.loadAllRecipes(allRecipes);
        view.loadRecipesToDisplay(recipesToDisplay);
        view.loadGroceryListsToDisplay(groceryListsToDisplay);
    }

    @Override
    public void updateInventoryButtonPressed() {
        viewState = MainViewState.INGREDIENT_LIST_FRAGMENT;
        view.switchFragment(viewState);
        view.loadIngredientList(inventory);
    }

    @Override
    public void groceryListButtonPressed(String name) {
        for (IngredientListImpl i:groceryLists) {
            if(name == i.getName()){
                view.loadIngredientList(i);
                break;
            }else{
                view.loadIngredientList(inventory);
            }
        }
        viewState = MainViewState.INGREDIENT_LIST_FRAGMENT;
        view.switchFragment(viewState);
    }

    public void recipeTilePressed(){
        viewState = MainViewState.RECIPE_FRAGMENT;
        view.switchFragment(viewState);
        view.loadRecipeDetails(searchedRecipe);
    }

    public void recipeSearched(int id){
        findRecipe(id);
    }

    public void changeRecipeView(){
        view.loadRecipeDetails(searchedRecipe);
        viewState = MainViewState.RECIPE_FRAGMENT;
        view.switchFragment(viewState);
    }

    public void createList(RecipeDetail r) {
        viewState = MainViewState.DASHBOARD_FRAGMENT;
        view.switchFragment(viewState);
        ArrayList<Ingredient> newIngredients = new ArrayList<>();
        for (int i = 0; i < r.getIngredients().size(); i++) {
            newIngredients.add(new Ingredient(r.getIngredients().get(i), r.getIngredient_ids().get(i)));
        }
        addListItem(groceryLists.get(0).getName(),newIngredients);
    }

    public void deleteListItem(String title, ArrayList<Ingredient> items){
        if(title.equals("Inventory")){
            for (Ingredient i : items) {
                inventory.removeItem(i);
            }
            Gson gson = new Gson();
            PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit()
                    .putString("inventory",gson.toJson(inventory)).apply();
            loadRecipesToDisplay();
            view.loadIngredientList(inventory);
        }else{
            for(IngredientListImpl list: groceryLists){
                if(title.equals(list.getName())){
                    for (Ingredient i : items) {
                        list.removeItem(i);
                    }
                    Gson gson = new Gson();
                    PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit()
                            .putString("groceryList",gson.toJson(list)).apply();
                    view.loadIngredientList(list);
                    break;
                }
            }
        }
    }

    public void addListItem(String title, ArrayList<Ingredient> items) {
        if (title.equals("Inventory")) {
            for(Ingredient i : items){
                if(testForDuplicates(i,inventory.getIngredientList())){
                    inventory.add(i);
                }
            }
            Gson gson = new Gson();
            PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit()
                    .putString("inventory",gson.toJson(inventory)).apply();
            loadRecipesToDisplay();
            view.loadIngredientList(inventory);

        }else{
            for (IngredientListImpl list : groceryLists) {
                if (title.equals(list.getName())) {
                    for(Ingredient i : items){
                        if(testForDuplicates(i,list.getIngredientList())){
                            list.add(i);
                        }
                    }
                    Gson gson = new Gson();
                    PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit()
                            .putString("groceryList",gson.toJson(list)).apply();
                    view.loadIngredientList(list);
                    break;
                }
            }
        }
    }
    private boolean testForDuplicates (Ingredient item, ArrayList<Ingredient> list){
        for (int i = 0; i < list.size(); i++) {
            if (item.getId() == list.get(i).getId()) {
                return false;
            }
        }
        return true;
    }

    public void loadRecipesToDisplay(){
        requestAPI = new RequestAPI();
        requestAPI.execute("http://18.221.66.122/api/find_recipes?list="+generateQuery());
        //list=[1403,%201271,%20603,%201667,%201118,%20718,%201865,%20919,%202230,%20235,%20409,%202368,%201271,%20330,%20389,%202285,%20500,%20312]");
    }

    private String generateQuery(){
        ArrayList<Integer> arr = new ArrayList<>();
        for(Ingredient i: inventory.getIngredients()){
            arr.add(new Integer(i.getId()));
        }
        String str = arr.toString().replace(" ", "%20");
        return str;
    }

    //Init data
    private void initData() throws ConnectException {
        inventory = new IngredientListImpl();
        list = new IngredientListImpl();

        String jsonObject = PreferenceManager.
                getDefaultSharedPreferences(view.getContext()).getString("inventory","");

        if (jsonObject.isEmpty()) {
            inventory.setName("Inventory");
            Gson gson = new Gson();
            PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit()
                    .putString("inventory",gson.toJson(inventory)).apply();
        }else{
            try{
                JSONObject obj = new JSONObject(jsonObject);
                inventory.setName(obj.getString("name"));
                JSONArray ing = obj.getJSONArray("ingredients");
                for(int i=0;i<ing.length();i++){
                    String ingObj = ing.getString(i);
                    JSONObject sub_obj = new JSONObject(ingObj);
                    inventory.add(new Ingredient(sub_obj.getString("name"), new Integer(sub_obj.getString("id"))));
                }
            }catch (JSONException e){ }
        }

        jsonObject = PreferenceManager.
                getDefaultSharedPreferences(view.getContext()).getString("groceryList","");

        if (jsonObject.isEmpty()) {
            list.setName("Grocery List");
            Gson gson = new Gson();
            PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit()
                    .putString("groceryList",gson.toJson(list)).apply();
        }else{
            try{
                JSONObject obj = new JSONObject(jsonObject);
                list.setName(obj.getString("name"));
                JSONArray ing = obj.getJSONArray("ingredients");
                for(int i=0;i<ing.length();i++){
                    String ingObj = ing.getString(i);
                    JSONObject sub_obj = new JSONObject(ingObj);
                    list.add(new Ingredient(sub_obj.getString("name"), new Integer(sub_obj.getString("id"))));
                }
            }catch (JSONException e){ }
        }

        jsonObject = PreferenceManager.
                getDefaultSharedPreferences(view.getContext()).getString("recipesToDisplay","");

        if (jsonObject.isEmpty()) {
            loadRecipesToDisplay();
        }else{
            try{
                JSONArray rec = new JSONArray(jsonObject);
                recipesToDisplay = new ArrayList<>();
                for(int i = 0; i < rec.length(); i++){
                    recipesToDisplay.add(new RecipeDetail(
                            toArrayListString(rec.getJSONObject(i).getString("directions")),
                            toArrayListString(rec.getJSONObject(i).getString("description")),
                            toArrayListString(rec.getJSONObject(i).getString("ingredients")),
                            rec.getJSONObject(i).getString("recipeName"),
                            new Integer(rec.getJSONObject(i).getString("recipeId")),
                            toArrayListInteger(rec.getJSONObject(i).getString("ingredient_ids"))));
                }
                view.loadRecipesToDisplay(recipesToDisplay);
            }catch (JSONException e){ }
        }

        /* ITEMS TO ADD IN THE INVENTORY LIST
        //"title":"Mahi-Mahi in Tomato Olive Sauce "
        //"ing":["tea","white bread","orange","onion","olive oil","mahi mahi fillets","olive","white wine","oregano"]
        // "ing_ids":[1403,1271,603,1667,1118,718,1865,919,2230]
        inventory.add(new Ingredient("onion", 1667));
        inventory.add(new Ingredient("olive oil", 1118));
        inventory.add(new Ingredient("tea", 1403));
        inventory.add(new Ingredient("white bread", 1271));
        inventory.add(new Ingredient("orange", 603));
        inventory.add(new Ingredient("mahi mahi fillets", 718));
        inventory.add(new Ingredient("olive", 1865));
        inventory.add(new Ingredient("white wine", 919));
        inventory.add(new Ingredient("oregano", 2230));
        //"title":"Peanut Butter-Banana Muffins "
        //"ing":["peanut butter","brown sugar","flour","banana","germ","baking","egg","vegetable oil","salt","honey","vanilla extract"]
        // "recipe_id":25,"ing_ids":[1209,1325,162,1812,1338,489,1556,2156,903,1004,1529]
        inventory.add(new Ingredient("peanut butter", 1209));
        inventory.add(new Ingredient("brown sugar", 1325));
        inventory.add(new Ingredient("flour", 162));
        inventory.add(new Ingredient("banana", 1812));
        inventory.add(new Ingredient("germ", 1338));
        inventory.add(new Ingredient("baking", 489));
        inventory.add(new Ingredient("egg", 1556));
        inventory.add(new Ingredient("vegetable oil", 2156));
        inventory.add(new Ingredient("salt", 903));
        inventory.add(new Ingredient("honey", 1004));
        inventory.add(new Ingredient("vanilla extract", 1529));
        */

        groceryLists = new ArrayList<>();
        groceryLists.add(list);

        allIngredients = new ArrayList<>();
        try{
            String jsonStr = loadJSONFromAsset("ingredients.json");
            JSONArray ing = new JSONArray(jsonStr);
            for(int i=0;i<ing.length();i++){
                String ingObj = ing.getString(i);
                JSONObject sub_obj = new JSONObject(ingObj);
                allIngredients.add(new Ingredient(sub_obj.getString("ingredient"), new Integer(sub_obj.getString("ingredient_id"))));
            }
        }catch(JSONException e){}

        allRecipes = new ArrayList<>();
        try{
            String jsonStr = loadJSONFromAsset("recipes.json");
            JSONArray rec = new JSONArray(jsonStr);
            for(int i=0;i<rec.length();i++){
                String ingObj = rec.getString(i);
                JSONObject sub_obj = new JSONObject(ingObj);
                allRecipes.add(new Recipe(sub_obj.getString("recipe"), new Integer(sub_obj.getString("recipe_id"))));
            }
        }catch(JSONException e){}


        groceryListsToDisplay = new ArrayList<>();
        for (IngredientListImpl i : groceryLists) {
            groceryListsToDisplay.add(i.getName());
        }
        searchedRecipe = new RecipeDetail();
    }

    private void findRecipe(int id){
        requestAPI = new RequestAPI();
        requestAPI.execute("http://18.221.66.122/api/recipe_detail/"+id);
    }

    private String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = view.getContext().getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private ArrayList<String> toArrayListString(String str){
        str = str.replace("[\"","").replace("\"]","");
        ArrayList<String> array = new ArrayList<>(Arrays.asList(str.split("\",\"")));
        return array;
    }
    private ArrayList<Integer> toArrayListInteger(String str){
        str = str.replace("[","").replace("]","").replace(" ","");
        ArrayList<String> strArray = new ArrayList<>(Arrays.asList(str.split(",")));
        ArrayList<Integer> array = new ArrayList<>();
        for(String i:strArray){
            array.add(Integer.parseInt(i.trim()));
        }
        return array;
    }

    //Deprecated components :(
    private class RequestAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            return getJSON(params[0]);
        }

        @Override
        protected void onPostExecute(String message) {
            try {
                Object json = new JSONTokener(message).nextValue();
                if (json instanceof JSONObject) {
                    //recipe_detail
                    jsonObject = new JSONObject(message);
                    searchedRecipe = new RecipeDetail(
                            toArrayListString(jsonObject.getString("directions")),
                            toArrayListString(jsonObject.getString("desc")),
                            toArrayListString(jsonObject.getString("ing")),
                            jsonObject.getString("title"),
                            new Integer(jsonObject.getString("recipe_id")),
                            toArrayListInteger(jsonObject.getString("ing_ids")));

                    changeRecipeView();
                } else if (json instanceof JSONArray){
                    jsonArray = new JSONArray(message);
                    recipesToDisplay = new ArrayList<>();
                    for(int i = 0; i < jsonArray.length(); i++){
                        recipesToDisplay.add(new RecipeDetail(
                                toArrayListString(jsonArray.getJSONObject(i).getString("directions")),
                                toArrayListString(jsonArray.getJSONObject(i).getString("desc")),
                                toArrayListString(jsonArray.getJSONObject(i).getString("ing")),
                                jsonArray.getJSONObject(i).getString("title"),
                                new Integer(jsonArray.getJSONObject(i).getString("recipe_id")),
                                toArrayListInteger(jsonArray.getJSONObject(i).getString("ing_ids"))));
                    }
                    Gson gson = new Gson();
                    PreferenceManager.getDefaultSharedPreferences(view.getContext()).edit()
                            .putString("recipesToDisplay",gson.toJson(recipesToDisplay)).apply();
                    view.loadRecipesToDisplay(recipesToDisplay);
                }
            } catch (JSONException e) {}
        }

        private String getJSON(String address){
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(address);
            try{
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if(statusCode == 200){
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                }
            }catch(ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return builder.toString();
        }
    }

}
