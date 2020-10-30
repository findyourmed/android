

package mans.fci.myapplication.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mans.fci.myapplication.R;

//BASED ON https://abhiandroid.com/ui/custom-spinner-examples.html
public class FormsSpinnerAdapter extends BaseAdapter {
    Context context;
    int flags[];
    String[] formsNames;
    LayoutInflater inflter;

    public FormsSpinnerAdapter(Context applicationContext, int[] flags, String[] forms) {
        this.context = applicationContext;
        this.flags = flags;
        this.formsNames = forms;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_form_spinner_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(flags[i]);
        names.setText(formsNames[i]);
        return view;
    }
}