package mans.fci.myapplication.ui;

import android.content.Context;
import android.widget.BaseAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mans.fci.myapplication.LogicModels.CountryInfo;
import mans.fci.myapplication.LogicModels.MedicineInfo;
import mans.fci.myapplication.MainActivity;
import mans.fci.myapplication.R;

//ref : https://abhiandroid.com/ui/listview , https://guides.codepath.com/android/Using-a-BaseAdapter-with-ListView
public class MedicineHomeCatalogAdapter extends BaseAdapter {
    Context context;
    MedicineInfo[] m_MedicinesList;
    LayoutInflater inflter;

    public MedicineHomeCatalogAdapter( Context applicationContext, MedicineInfo[] MedicinesList) {
        this.context = applicationContext;
        this.m_MedicinesList = MedicinesList;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return m_MedicinesList.length;
    }

    @Override
    public Object getItem(int i) {
        return m_MedicinesList[i];
    }

    @Override
    public long getItemId(int i) {
        return m_MedicinesList[i].Id;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_home_catolog_list_item, parent, false);
        }

        // get current item to be displayed
        MedicineInfo currentItem = (MedicineInfo) getItem(position);


        TextView tvTitle =  convertView.findViewById(R.id.textView_medicineTitle);
        TextView tv_Ingredients =  convertView.findViewById(R.id.textView_active_ingredients);
        TextView tv_Producer =  convertView.findViewById(R.id.textView_producer);



        //sets the text for item name and item description from the current item object
        tvTitle.setText(currentItem.title);
       /* CountryInfo itemCountry =null;
        for (CountryInfo c: MainActivity.m_CountriesList) {
            if(c.country_id == currentItem.country_id)
            {
                itemCountry =c;
                break;
            }
        }*/
        if(currentItem.m_View_Country!=null)
            tv_Producer.setText(currentItem.m_View_Country);

        // returns the view for the current row
        return convertView;
    }



}
