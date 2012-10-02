package by.chaba.widget.newyear;
/*
 * Copyright (C) 2012 The Android Open Source Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

/**
 * This class designed to display available styles of widget
 * 
 * @author Andrei Churila
 *
 */
public class ThemesActivity extends Activity {    
   public static final String THEME_DATA_TAG = "theme_data";
    
    private ListView mMyThemesListView;
    private ListView mSimpleThemesVistView;
    
    private final Integer[] mMyThemesArray = { R.drawable.theme_wooden_board};
    
    private final Integer[] mSampleThemesArray = { R.color.black_alfa,
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5};
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themes);
        
        initialTabHost();
        initializeMyThemesListView();
        initializeSampleThemesListView();
    }
    
    private void initializeMyThemesListView(){
        // Prepare mMyThemesListView    
        mMyThemesListView = (ListView) findViewById(R.id.actyvityMyThemesListView);
        MySimpleArrayAdapter myThemesListViewAdapter = 
                new MySimpleArrayAdapter(ThemesActivity.this, mMyThemesArray);      
        mMyThemesListView.setAdapter(myThemesListViewAdapter);  
        AdapterView.OnItemClickListener mSelectThemeClickListener = 
                new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View item_clicked, 
                    int position, long id) {                    
                // Change theme
                Intent serviseIntent = new Intent(ThemesActivity.this, WidgetUpdateService.class);
                serviseIntent.putExtra(THEME_DATA_TAG, mMyThemesArray[position]);
                startService(serviseIntent);
                
                Toast.makeText(ThemesActivity.this, getResources().
                        getString(R.string.activity_theme_changed), 
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        mMyThemesListView.setOnItemClickListener(mSelectThemeClickListener);
    }
    
    private void initializeSampleThemesListView(){
        mSimpleThemesVistView = (ListView) findViewById(R.id.actyvitySimpleThemesListView);
        MySimpleArrayAdapter simpleThemesListViewAdapter = 
                new MySimpleArrayAdapter(ThemesActivity.this, mSampleThemesArray);      
        mSimpleThemesVistView.setAdapter(simpleThemesListViewAdapter);
        AdapterView.OnItemClickListener mSelectThemeClickListener = 
                new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View item_clicked, 
                    int position, long id) {
                        
                // Change theme
                Intent serviseIntent = new Intent(ThemesActivity.this,WidgetUpdateService.class);
                serviseIntent.putExtra(THEME_DATA_TAG, mSampleThemesArray[position]);
                startService(serviseIntent);
                    
                Toast.makeText(ThemesActivity.this, 
                        getResources().getString(R.string.activity_theme_changed), 
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        mSimpleThemesVistView.setOnItemClickListener(mSelectThemeClickListener);
    }
    
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }
    
    private void initialTabHost()
    {
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        
        TabSpec tabSpecMyThemses = tabHost.newTabSpec("free_themes");
        tabSpecMyThemses.setIndicator(getResources().getString(R.string.actyvity_my_themes));
        tabSpecMyThemses.setContent(R.id.actyvityMyThemesListView);
        
        TabSpec tabSpecSimleThemes = tabHost.newTabSpec("simpleThemes");
        tabSpecSimleThemes.setIndicator(getResources().
                getString(R.string.actyvity_simple_themes));
        tabSpecSimleThemes.setContent(R.id.actyvitySimpleThemesListView);
        
        tabHost.addTab(tabSpecMyThemses);
        tabHost.addTab(tabSpecSimleThemes);
        tabHost.setCurrentTabByTag("free_themes");
    }
    
    private class MySimpleArrayAdapter extends ArrayAdapter<Integer> {
        private final Context context;
        private final Integer[] values;

        public MySimpleArrayAdapter(Context context, Integer[] values) {
            super(context, R.layout.theme, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.theme, parent, false);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.themeId);                  
            
            imageView.setImageDrawable( getResources().getDrawable(values[position]));          
            return rowView;
        }
    }
}
