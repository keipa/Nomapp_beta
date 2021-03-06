package com.nomapp.nomapp_beta.CategoriesOfRecipes;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nomapp.nomapp_beta.AvailableRecipes.RecipesRecyclerAdapter;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.RecipePreview.RecipePreviewActivity;

import java.util.ArrayList;

/**
 * Created by antonid on 30.09.2015.
 */
public class RecipesSearchFragment extends Fragment {
    RecyclerView findedRecipesRecycler;

    ArrayList<String> findedRecipesArray;
    ArrayList<String> timeForCooking;
    ArrayList<Integer> numberOfSteps;
    ArrayList<Integer> IDs;
    ArrayList<Integer> numberOfIngs;
    ArrayList<String> measuresForTime;

    RecipesRecyclerAdapter mAdapter;
    Cursor cursor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_recipes, null);

        findedRecipesRecycler = (RecyclerView) v.findViewById(R.id.findedRecipesRecyclerView);

        findedRecipesArray = new ArrayList<>();
        IDs = new ArrayList<>();
        timeForCooking = new ArrayList<>();
        numberOfSteps = new ArrayList<>();
        numberOfIngs = new ArrayList<>();
        measuresForTime = new ArrayList<>();

        return v;
    }

    /*
    * Method for searching
    * items in database
    * by current query
    */
    void search(String enteredText) {
        try {
            findedRecipesArray.clear();
            IDs.clear();
            timeForCooking.clear();
            numberOfSteps.clear();
            numberOfIngs.clear();
            measuresForTime.clear();
        } catch (NullPointerException e) {
            Log.e("Error!", e.getMessage());
        }


        if (enteredText.equals("")) {
            setUpRecyclerView();
            return;
        }

        cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(),
                                Database.getRecipesIsAvailable(), Database.getRecipesNumberOfSteps(),
                                Database.getRecipesTimeForCooking(), Database.getRecipesNumberOfIngredients(),
                                Database.getRecipesMeasureForTime()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String name = cursor.getString(1);
                if (name.toLowerCase().contains(enteredText) || name.contains(enteredText)) {
                    findedRecipesArray.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                    timeForCooking.add(cursor.getString(4));
                    numberOfSteps.add(cursor.getInt(3));
                    numberOfIngs.add(cursor.getInt(5));
                    measuresForTime.add(cursor.getString(6));
                }
            } while (cursor.moveToNext());
        }
        setUpRecyclerView();
    }

    /*
   * Sets up RecyclerView with finded items.
   * Add possibility to select items by OnItemTouchListener
   * interface in Adapter.
    */
    void setUpRecyclerView() {
        RecipesRecyclerAdapter.OnItemTouchListener itemTouchListener = new RecipesRecyclerAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                if (cursor.isClosed()) {
                    cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                            new String[]
                                    {Database.getRecipesId(), Database.getRecipesName(),
                                            Database.getRecipesIsAvailable(), Database.getRecipesNumberOfSteps(),
                                            Database.getRecipesTimeForCooking(), Database.getRecipesNumberOfIngredients(),
                                            Database.getRecipesMeasureForTime()},
                            null, null, null, null
                            , null);
                }

                cursor.moveToPosition(IDs.get(position) - 1);

                Intent intent = new Intent(getActivity(), RecipePreviewActivity.class); //TODO maybe
                intent.putExtra("numberOfRecipe", IDs.get(position));
                intent.putExtra("cooking", cursor.getString(6));
                intent.putExtra("numberOfSteps", numberOfSteps.get(position));
                intent.putExtra("nameOfRecipe", findedRecipesArray.get(position));
                startActivity(intent);
                cursor.close();
            }
        };

        mAdapter = new RecipesRecyclerAdapter(getActivity(), findedRecipesArray, timeForCooking,
                numberOfSteps, numberOfIngs, measuresForTime, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        findedRecipesRecycler.setLayoutManager(layoutManager);
        findedRecipesRecycler.setAdapter(mAdapter);

    }


}
