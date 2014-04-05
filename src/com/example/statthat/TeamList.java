package com.example.statthat;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamList extends ArrayAdapter<Team>{
	private Context context;
	private List<Team> teams;
	
	public TeamList(Context context, List<Team> teams){
		super(context, R.layout.team_list, teams);
		
		this.teams = teams;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.team_list, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.team_name);
//		    ImageView imageView = (ImageView) rowView.findViewById(R.id.team_icon);
		    textView.setText(teams.get(position).name);

		    return rowView;
	  
	}
}
