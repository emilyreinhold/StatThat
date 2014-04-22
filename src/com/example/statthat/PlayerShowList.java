package com.example.statthat;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlayerShowList extends ArrayAdapter<Player>{
	private Context context;
	private List<Player> players;
	
	public PlayerShowList(Context context, List<Player> players){
		super(context, R.layout.player_item, players);
		
		this.players = players;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.player_item, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.team_name);
//		    ImageView imageView = (ImageView) rowView.findViewById(R.id.team_icon);
		    textView.setText(players.get(position).firstName + " " + players.get(position).lastName);

		    return rowView;
	  
	}
}