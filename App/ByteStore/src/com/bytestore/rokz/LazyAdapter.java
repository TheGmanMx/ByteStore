package com.bytestore.rokz;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<String> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private List<String> listaApps;
    
    public LazyAdapter(Activity a, List<String> nomApps, List<String> imgsURL) {
        activity = a;
        listaApps = nomApps;
        data=imgsURL;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.lista_row, null);

        TextView text=(TextView)vi.findViewById(R.id.title);;
        ImageView image=(ImageView)vi.findViewById(R.id.list_image);
        text.setText(listaApps.get(position));
        imageLoader.DisplayImage(data.get(position), image);
        return vi;
    }
}