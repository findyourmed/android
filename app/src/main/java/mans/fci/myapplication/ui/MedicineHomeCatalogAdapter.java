package mans.fci.myapplication.ui;

import android.content.Context;
import android.widget.BaseAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mans.fci.myapplication.LogicModels.CountryInfo;
import mans.fci.myapplication.LogicModels.MedicineInfo;
import mans.fci.myapplication.MainActivity;
import mans.fci.myapplication.R;

//ref : https://abhiandroid.com/ui/listview , https://guides.codepath.com/android/Using-a-BaseAdapter-with-ListView
public class MedicineHomeCatalogAdapter extends BaseAdapter /*implements Filterable*/ {
    Context context;
    List<MedicineInfo> m_MedicinesList;
    LayoutInflater inflter;

    public MedicineHomeCatalogAdapter( Context applicationContext, List<MedicineInfo> MedicinesList) {
        this.context = applicationContext;
        this.m_MedicinesList = MedicinesList;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return m_MedicinesList.size();
    }

    @Override
    public Object getItem(int i) {
        return m_MedicinesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return m_MedicinesList.get(i).Id;
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
        tv_Ingredients.setText(currentItem.active_ingredient);
        tv_Producer.setText(currentItem.producent);
       /* CountryInfo itemCountry =null;
        for (CountryInfo c: MainActivity.m_CountriesList) {
            if(c.country_id == currentItem.country_id)
            {
                itemCountry =c;
                break;
            }
        }*/
//        if(currentItem.m_View_Country!=null)
//            tv_Producer.setText(currentItem.m_View_Country);

        // returns the view for the current row
        return convertView;
    }

//    //https://stackoverflow.com/questions/14365847/how-to-implement-getfilter-with-custom-adapter-that-extends-baseadapter
//    @Override
//    public Filter getFilter()
//    {
//        return new Filter()
//        {
//            List<MedicineInfo> filteredData;
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence)
//            {
//                FilterResults results = new FilterResults();
//
//                //If there's nothing to filter on, return the original data for your list
//                if(charSequence == null || charSequence.length() == 0)
//                {
//                    results.values = m_MedicinesList;
//                    results.count = m_MedicinesList.size();
//                }
//                else
//                {
//                    List<MedicineInfo> filterResultsData = new ArrayList<> ();
//                    String queryText = charSequence.toString();
//                    for(MedicineInfo data : m_MedicinesList)
//                    {
//                        //In this loop, you'll filter through originalData and compare each item to charSequence.
//                        //If you find a match, add it to your new ArrayList
//                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
//                        if(data.IsMatch(queryText))
//                        {
//                            filterResultsData.add(data);
//                        }
//                    }
//
//                    results.values = filterResultsData;
//                    results.count = filterResultsData.size();
//                }
//
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
//            {
//                filteredData = (ArrayList<MedicineInfo>)filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

}
