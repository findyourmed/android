
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
public class SimilarMedicineAdapter extends BaseAdapter {
    Context context;
    List<MedicineInfo> m_MedicinesList;
    LayoutInflater inflter;

    public SimilarMedicineAdapter( Context applicationContext, List<MedicineInfo> MedicinesList) {
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
                    inflate(R.layout.row_similar_medicine_item, parent, false);
        }

        // get current item to be displayed
        MedicineInfo currentItem = (MedicineInfo) getItem(position);


        TextView tvTitle =  convertView.findViewById(R.id.tv_similar_row_medicineTitle);
        TextView tv_country = convertView.findViewById(R.id.tv_similar_row_country);



        //sets the text for item name and item description from the current item object
        tvTitle.setText(currentItem.title);
        tv_country.setText(currentItem.m_View_Country);

        // returns the view for the current row
        return convertView;
    }



}
