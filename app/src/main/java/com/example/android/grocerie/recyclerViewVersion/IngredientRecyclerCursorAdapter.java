package com.example.android.grocerie.recyclerViewVersion;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class IngredientRecyclerCursorAdapter extends BaseCursorAdapter<IngredientRecyclerCursorAdapter.IngredientViewHolder> {

    private Context mContext;

    public IngredientRecyclerCursorAdapter() {
        super(null);
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        View formNameView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(formNameView);
    }

    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {

        holder.checkedCheckBox.setOnCheckedChangeListener(null);

        int idIndex = cursor.getColumnIndex(IngredientEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
        int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
        int categoryindex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);

        int idValue = cursor.getInt(idIndex);
        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);
        Integer ingredientChecked = cursor.getInt(checkedColumnIndex);
        int category = cursor.getInt(categoryindex);

        if (TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        if (ingredientChecked == 1) {
            holder.checkedCheckBox.setChecked(true);
        } else {
            holder.checkedCheckBox.setChecked(false);
        }

        switch (category) {
            case IngredientEntry.FRUIT_AND_VEG:
                holder.categoryTextView.setText(R.string.fruit_and_veggie);;
                break;
            case IngredientEntry.MEAT_AND_PROT:
                holder.categoryTextView.setText(R.string.meat_and_prot);;
                break;
            case IngredientEntry.BREAD_AND_GRAIN:
                holder.categoryTextView.setText(R.string.bread_and_grain);;
                break;
            case IngredientEntry.DAIRY:
                holder.categoryTextView.setText(R.string.dairy);;
                break;
            case IngredientEntry.FROZEN:
                holder.categoryTextView.setText(R.string.frozen);;
                break;
            case IngredientEntry.CANNED:
                holder.categoryTextView.setText(R.string.canned);;
                break;
            case IngredientEntry.DRINKS:
                holder.categoryTextView.setText(R.string.drinks);;
                break;
            case IngredientEntry.SNACKS:
                holder.categoryTextView.setText(R.string.snacks);;
                break;
            default:
                holder.categoryTextView.setText(R.string.misc);;
                break;
        }

        Log.e("myTag", "ingredientChecked at row " + idValue + " is equal to " + ingredientChecked);

        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit);


        holder.checkedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                String checkedString;

                if (isChecked) {
                    checkedString = "1";
                } else {
                    checkedString = "0";
                }

                Log.e("myTag", "The checkbox of the current row is " + checkedString);

                ContentValues values = new ContentValues();

                values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkedString);

                mContext.getContentResolver().update(currentIngredientUri, values, null, null);
            }
        });

        holder.ingredientSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Intent intent = new Intent(mContext, IngredientEditor.class);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
                int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
                int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
                int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
                int categoryColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);

                Log.e("myTag", "name column = " + nameColumnIndex);
                Log.e("myTag", "amount column = " + amountColumnIndex);
                Log.e("myTag", "unit column = " + unitColumnIndex);
                Log.e("myTag", "checked column = " + checkedColumnIndex);
                Log.e("myTag", "category column = " + categoryColumnIndex);

                intent.setData(currentIngredientUri);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }


    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView summaryTextView;
        CheckBox checkedCheckBox;
        TextView categoryTextView;
        LinearLayout ingredientSummary;


        IngredientViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
            checkedCheckBox = itemView.findViewById(R.id.ingredient_list_checkBox);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            ingredientSummary = itemView.findViewById(R.id.ingredient_summary);


        }
    }
}