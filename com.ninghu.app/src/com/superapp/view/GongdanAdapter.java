package com.superapp.view;

import java.util.ArrayList;
import java.util.List;

import com.superapp.info.Gongdan;
import com.superapp.info.GongdanInfo;
import com.superapp.main.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class GongdanAdapter extends BaseAdapter {
	private Context context;
	private List<GongdanInfo> list = new ArrayList<GongdanInfo>();
	
	public GongdanAdapter(Context context,List<GongdanInfo> list2){
		this.context = context;
		this.list = list2;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		GongdanInfo hh = list.get(position);
		H h = null;
		if(view==null){
			h = new H();
			view = LayoutInflater.from(context).inflate(R.layout.xingongdan, parent, false);
			h.pic = (ImageView)view.findViewById(R.id.l1);
			h.name = (TextView)view.findViewById(R.id.name);
			h.time = (TextView)view.findViewById(R.id.time);
			h.lastmsg = (TextView)view.findViewById(R.id.lastmsg);
			
			view.setTag(h);
		}else{
			h = (H)view.getTag();
		}
		
		h.pic.setImageResource(Integer.parseInt(R.drawable.voip_camerachat + ""));
		h.name.setText(hh.getTask_id());
		h.time.setText(hh.getReceive_time());
		h.lastmsg.setText(hh.getLocation());
		
		return view;
	}

	class H{
		ImageView pic;
		TextView name;
		TextView time;
		TextView lastmsg;
	}
}
