package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ituition.ituition.R;

import java.util.List;

import model.TuitionItem;

/**
 * Created by mushfiq on 1/20/18.
 */

public class LVAdapter extends ArrayAdapter<TuitionItem> {
    public LVAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public LVAdapter(Context context, int resource, List<TuitionItem> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tuition_card, null);
        }

        TuitionItem p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.mti_name_field);
            TextView tt2 = (TextView) v.findViewById(R.id.mti_sub_field);
            TextView tt3 = (TextView) v.findViewById(R.id.mti_address_field);
            TextView tt4 = (TextView) v.findViewById(R.id.mti_acl_field);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getSubjects());
            }

            if (tt3 != null) {
                tt3.setText(p.getAddress());
            }

            if (tt4 != null) {
                tt4.setText(p.getAclev());
            }
        }

        return v;
    }


}
