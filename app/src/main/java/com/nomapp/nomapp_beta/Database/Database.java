package com.nomapp.nomapp_beta.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by antonid on 12.07.2015.
 */
public class Database {
    private static Database database;

    private SQLiteDatabase ingridients;
    private SQLiteDatabase recipes;
    private SQLiteDatabase categories;

    private static final String INGRIDIENTS_DB_NAME = "IngBase.db";
    private static final String RECIPES_DB_NAME = "RecBase.db";
    private static final String CATEGORIES_DB_NAME = "categories.db";

    private static final String INGREDIENTS_TABLE_NAME = "Ingridients";
    private static final String INGRIDIENT_ID = "_id";
    private static final String INGRIDIENT_NAME = "name";
    private static final String IS_CHECKED = "checked";

    private static final String RECIPES_TABLE_NAME = "Recipes";
    private static final String RECIPES_ID = "_id";
    private static final String RECIPES_NAME = "Name";
    private static final String RECIPES_INGRIDIENTS = "ingridients";
    private static final String RECIPES_HOW_TO_COOK = "howToCook";
    private static final String RECIPES_IS_AVAILABLE = "isAvailable";
    private static final String RECIPES_NUMBER_OF_STEPS = "numberOfSteps";
    private static final String RECIPES_TIME_FOR_COOKING = "timeForCooking";
    private static final String RECIPES_DESCRIPTION = "description";
    private static final String RECIPES_NUMBER_OF_PERSONS = "numberOfPersons";
    private static final String RECIPES_NUMBER_OF_INGRIDIENTS = "numberOfIngridients";



    private static final String CATEGORIES_TABLE_NAME = "categories";
    private static final String CATEGORIES_ID = "_id";
    private static final String CATEGORY_NAME= "name";
    private static final String CATEGORY_INGREDIENTS= "ingredients";


    public static void initDatabase(Context context) {
        if (database == null) {
            database = new Database(context);
        }
    }

    public static Database getDatabase() {
        return database;
    }

    private Database(Context context) {
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(context, INGRIDIENTS_DB_NAME);
        ingridients = dbOpenHelper.openDataBase();

        ExternalDbOpenHelper reciepesOpenHelper = new ExternalDbOpenHelper(context, RECIPES_DB_NAME);
        recipes = reciepesOpenHelper.openDataBase();

        ExternalDbOpenHelper categoriesOpenHelper = new ExternalDbOpenHelper(context, CATEGORIES_DB_NAME);
        categories = categoriesOpenHelper.openDataBase();


    }

    public SQLiteDatabase getIngridients() {
        return ingridients;
    }
    public SQLiteDatabase getRecipes() {
        return recipes;
    }
    public SQLiteDatabase getCategories(){
        return  categories;
    }

    public static String getRecipesId() {
        return RECIPES_ID;
    }
    public static String getRecipesName() {
        return RECIPES_NAME;
    }
    public static String getRecipesIngridients() {
        return RECIPES_INGRIDIENTS;
    }
    public static String getRecipesHowToCook() {
        return RECIPES_HOW_TO_COOK;
    }
    public static String getRecipesIsAvailable() {
        return RECIPES_IS_AVAILABLE;
    }
    public static String getRecipesNumberOfSteps() {
        return RECIPES_NUMBER_OF_STEPS;
    }
    public static String getRecipesTableName() { return RECIPES_TABLE_NAME; }
    public static String getRecipesTimeForCooking() { return RECIPES_TIME_FOR_COOKING; }
    public static String getRecipesDescription() { return RECIPES_DESCRIPTION; }
    public static String getRecipesNumberOfPersons() { return RECIPES_NUMBER_OF_PERSONS; }
    public static String getRecipesNumberOfIngridients() { return RECIPES_NUMBER_OF_INGRIDIENTS; }

    public static String getIngredientsTableName() {return INGREDIENTS_TABLE_NAME; };
    public static String getIngridientId() {
        return INGRIDIENT_ID;
    }
    public static String getIngridientName() {
        return INGRIDIENT_NAME;
    }
    public static String getIngridientIsChecked() {
        return IS_CHECKED;
    }

    public static String getCategoriesId() {
        return CATEGORIES_ID;
    }
    public static String getCategoryName() {
        return CATEGORY_NAME;
    }
    public static String getCategoryIngredients() {
        return CATEGORY_INGREDIENTS;
    }
    public static String getCategoriesTableName() {
        return CATEGORIES_TABLE_NAME;
    }


}