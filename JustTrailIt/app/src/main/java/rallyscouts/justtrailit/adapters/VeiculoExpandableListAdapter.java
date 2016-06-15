package rallyscouts.justtrailit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Veiculo;

/**
 * Created by rjaf on 14/06/16.
 */
public class VeiculoExpandableListAdapter  extends BaseExpandableListAdapter{

    private List<Veiculo> veiculos;
    private Context mContext;


    public VeiculoExpandableListAdapter(Context context, List<Veiculo> lista) {
        veiculos = lista;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return veiculos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return veiculos.get(groupPosition).getCaracteristicas().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return veiculos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return veiculos.get(groupPosition).getCaracteristicas().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_veiculo, null);
        }

        TextView tchassi = (TextView) convertView.findViewById(R.id.textView_chassi);
        TextView tmarca = (TextView) convertView.findViewById(R.id.textView_marca);
        TextView tmodelo = (TextView) convertView.findViewById(R.id.textView_modelo);


        Veiculo vec = veiculos.get(groupPosition);

        if(tchassi != null){ tchassi.setText("CHASSI: " + vec.getChassi()); }
        if(tmarca != null){ tmarca.setText("MARCA: " + vec.getMarca()); }
        if(tmodelo != null){ tmodelo.setText("MODELO: " + vec.getModelo()); }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_veiculo_details, null);
        }

        TextView tcaract = (TextView) convertView.findViewById(R.id.textView_caract);

        if(tcaract != null){ tcaract.setText( veiculos.get(groupPosition).getCaracteristicas().get(childPosition) ); }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
