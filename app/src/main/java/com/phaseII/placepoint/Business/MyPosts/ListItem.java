package com.phaseII.placepoint.Business.MyPosts;

import android.view.View;

public interface ListItem {
    int getVisibilityPercents(View view);
    void setActive(View newActiveView, int newActiveViewPosition);
    void deactivate(View currentView, int position);
}