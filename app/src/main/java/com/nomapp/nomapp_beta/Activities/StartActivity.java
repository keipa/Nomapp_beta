package com.nomapp.nomapp_beta.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.melnykov.fab.FloatingActionButton;
import com.nomapp.nomapp_beta.CardViewAdapter;
import com.nomapp.nomapp_beta.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

//import com.nomapp.nomapp_beta.SwipeableRecyclerViewTouchListener;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TABLE_NAME = "Ingridients";
    private static final String RECIPES_TABLE_NAME = "Recipes";

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    RecyclerView selectedIngridients;
    SwipeableRecyclerViewTouchListener swipeTouchListener;
    Button showAvailableRecipes;
    FloatingActionButton fab;
    ArrayList<String> forSelectedIngridients;
    ArrayList<String> ingridientsForRecipe;
    ArrayList<Integer> IDs;
    ArrayList<ArrayList<Integer>> convertedIngrodientsForRecipe;

    int nubmerOfAvailableRecipes;
    private CardViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        nubmerOfAvailableRecipes = 0;
        selectedIngridients = (RecyclerView) findViewById(R.id.recycler_view);   //    setUpFAB();
        showAvailableRecipes = (Button) findViewById(R.id.showAvailableRecipes);
        showAvailableRecipes.setOnClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setUpNavigationDraver();


    }

    @Override
    protected void onStart() {
        super.onStart();
        showNumberOfAvailableRecipes();
        fillSelectedIngridients();
        setUpList();
        setUpFAB();
        setUpUserSettings();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.showAvailableRecipes:
                Intent intent = new Intent(StartActivity.this, ListOfAvaliableRecipesActivity.class);
                startActivity(intent);
                break;

            default:

                break;
        }
    }

    View.OnClickListener onCircleButtonCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StartActivity.this, CategoriesActivity.class);
            startActivity(intent);
        }
    };

    void setUpUserSettings(){
        setUpLocalization();
    }

    void setUpLocalization() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = settings.getString("language", "");

        TextView fridgeText = (TextView) findViewById(R.id.addFridge);
        TextView dishOfADayText = (TextView) findViewById(R.id.addDishOfADay);
        TextView allRecipesText = (TextView) findViewById(R.id.addAllRecepies);
        TextView settingsText = (TextView) findViewById(R.id.addSettings);
        TextView leaveFeedbackText = (TextView) findViewById(R.id.addReply);
        TextView aboutText = (TextView) findViewById(R.id.addHelp);

        switch (lang){
            case "1":
                fridgeText.setText(getString(R.string.fridge_en));
                dishOfADayText.setText(getString(R.string.dish_of_a_day_en));
                allRecipesText.setText(getString(R.string.all_recipes_en));
                settingsText.setText(getString(R.string.settings_en));
                leaveFeedbackText.setText(getString(R.string.leave_feedback_en));
                aboutText.setText(getString(R.string.about_en));
                break;

            case "2":
                fridgeText.setText(getString(R.string.fridge_ru));
                dishOfADayText.setText(getString(R.string.dish_of_a_day_ru));
                allRecipesText.setText(getString(R.string.all_recipes_ru));
                settingsText.setText(getString(R.string.settings_ru));
                leaveFeedbackText.setText(getString(R.string.leave_feedback_ru));
                aboutText.setText(getString(R.string.about_ru));
                break;

            default:
                break;
        }
        Log.w("MY_TAG", lang);
    }

    void setUpNavigationDraver(){
        mToolbar= (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Click events for Navigation Drawer
        LinearLayout navButton = (LinearLayout) findViewById(R.id.txtFridgeButton);
        navButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, AddIngridientsActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout settingsButton = (LinearLayout) findViewById(R.id.txtSettings);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // close drawer if you want
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(StartActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    void setUpFAB(){
        fab.attachToRecyclerView(selectedIngridients);
        fab.setColorNormal(getResources().getColor(R.color.chosenElement));
        fab.setColorPressed(getResources().getColor(R.color.primary));
        fab.setColorRipple(getResources().getColor(R.color.chosenElement));
        fab.setOnClickListener(onCircleButtonCliclListener);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_animation);
        fab.startAnimation(hyperspaceJumpAnimation);
    }

    void setUpList() {
        CardViewAdapter.OnItemTouchListener itemTouchListener = new CardViewAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Toast.makeText(StartActivity.this, "Tapped " + forSelectedIngridients.get(position), Toast.LENGTH_SHORT).show();
            }
        };

        mAdapter = new CardViewAdapter(forSelectedIngridients, itemTouchListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectedIngridients.setLayoutManager(layoutManager);
        selectedIngridients.setAdapter(mAdapter);
        setSwipeTouchListener();
    }

    private void fillSelectedIngridients() {
        forSelectedIngridients = new ArrayList<>();
        IDs = new ArrayList<>();
        Cursor cursor = Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(2) != 0) {
                    forSelectedIngridients.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showNumberOfAvailableRecipes() {
        nubmerOfAvailableRecipes = 0;
        fillIngridientsForRecipe();
        int size = ingridientsForRecipe.size();
        convertedIngrodientsForRecipe = new ArrayList<ArrayList<Integer>>();
        for (int counter = 0; counter < size; counter++) {
            convertedIngrodientsForRecipe.add(convertIngridientsToArrayList(ingridientsForRecipe.get(counter)));
        }
        checking();
        showAvailableRecipes.setText(nubmerOfAvailableRecipes + " recipes available");
    }

    private void checking() {
        boolean isRecipeAvailable = true;

        Cursor cursor = Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();

        int numberOfRecipes = convertedIngrodientsForRecipe.size();
        for (int currentRecipe = 0; currentRecipe < numberOfRecipes; currentRecipe++) {
            int numberOfIngridientsInRecipe = convertedIngrodientsForRecipe.get(currentRecipe).size();
            for (int ingridientNumber = 0; ingridientNumber < numberOfIngridientsInRecipe; ingridientNumber++) {
                cursor.moveToPosition(convertedIngrodientsForRecipe.get(currentRecipe).get(ingridientNumber) - 1);
                if (cursor.getInt(2) != 1) {
                    isRecipeAvailable = false;
                    break;
                }
            }

            if (isRecipeAvailable == true) {
                Database.getDatabase().getRecipes().execSQL("UPDATE Recipes SET isAvailable=1 WHERE _id=" + (currentRecipe + 1) + ";");
                nubmerOfAvailableRecipes++;
            } else {
                Database.getDatabase().getRecipes().execSQL("UPDATE Recipes SET isAvailable=0 WHERE _id=" + (currentRecipe + 1) + ";");
            }
            isRecipeAvailable = true;
        }
        cursor.close();
    }

    private void fillIngridientsForRecipe() {
        ingridientsForRecipe = new ArrayList<String>();
        Cursor cursor = Database.getDatabase().getRecipes().query(RECIPES_TABLE_NAME,
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngridients(),
                                Database.getRecipesHowToCook(), Database.getRecipesIsAvailable(),
                                Database.getRecipesNumberOfSteps()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(2) != 0) {
                    ingridientsForRecipe.add(cursor.getString(2));
                    Log.w("MY_TAG", ingridientsForRecipe.get(cursor.getPosition()));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private ArrayList<Integer> convertIngridientsToArrayList(String toConvert) {
        ArrayList<Integer> converted = new ArrayList<Integer>();

        int counter = 0;
        int factor = 1;
        int currentIngridient = 0;
        int size = toConvert.length();
        for (counter = 0; counter < size; counter++) {
            while (toConvert.charAt(counter) != ',' && toConvert.charAt(counter) != '.') {//TODO
                currentIngridient += (toConvert.charAt(counter) - '0') * factor;
                factor *= 10;
                counter++;
            }
            factor = 1;
            converted.add(currentIngridient);
            currentIngridient = 0;
        }
        return converted;
    }

    void setSwipeTouchListener(){
        swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(selectedIngridients,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                                    forSelectedIngridients.remove(position);
                                    IDs.remove(position);
                                    showNumberOfAvailableRecipes();
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                                    forSelectedIngridients.remove(position);
                                    IDs.remove(position);
                                    showNumberOfAvailableRecipes();
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        selectedIngridients.addOnItemTouchListener(swipeTouchListener);

    }


}