package com.superapp.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.superapp.info.Gongdan;
import com.superapp.info.GongdanInfo;
import com.superapp.main.R;


public class WeiWanChengAdapter extends BaseAdapter {
	private Context context;
	private List<GongdanInfo> list = new ArrayList<GongdanInfo>();
	
	public  WeiWanChengAdapter(Context context,List<GongdanInfo> list){
		this.context = context;
		this.list = list;
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
			view = LayoutInflater.from(context).inflate(R.layout.weiwancheng, parent, false);
			h.pic = (ImageView)view.findViewById(R.id.wwcp);
			h.name = (TextView)view.findViewById(R.id.wname);
			h.time = (TextView)view.findViewById(R.id.wtime);
			h.lastmsg = (TextView)view.findViewById(R.id.wdetail);
			
			view.setTag(h);
		}else{
			h = (H)view.getTag();
		}
		
		h.pic.setImageResource(Integer.parseInt(R.drawable.find_more_friend_photograph_icon + ""));
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
