package com.phaseII.placepoint;

import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData;

import java.util.Comparator;

public class AlphabetiComparator implements Comparator<ModelCategoryData> {
    @Override
    public int compare(ModelCategoryData o1, ModelCategoryData o2) {
        return  o1.name.compareTo(o2.name);
    }
}