package com.example.android.droidchef.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Astraeus on 1/22/2018.
 * This is the SERVICE
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this.getApplicationContext());
    }
}
