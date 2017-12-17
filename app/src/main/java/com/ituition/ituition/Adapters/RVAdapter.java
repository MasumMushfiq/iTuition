package com.ituition.ituition.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ituition.ituition.R;

import java.util.ArrayList;

import Model.Person;

/**
 * Created by tahmid on 12/4/17.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.UserViewHolder> {


    public static class UserViewHolder extends RecyclerView.ViewHolder{
        CardView userCard;
        TextView personName;
        TextView personSubjects;
        TextView personLocations;
        TextView personBackground;
        ImageView personPhoto;

        public UserViewHolder(View itemView) {
            super(itemView);
            userCard = (CardView)itemView.findViewById(R.id.person_card_view);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personSubjects = (TextView)itemView.findViewById(R.id.person_subjects);
            personLocations = (TextView)itemView.findViewById(R.id.person_location);
            personBackground = (TextView)itemView.findViewById(R.id.person_background);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    ArrayList<Person> people;

    public RVAdapter(ArrayList<Person> users) {
        this.people = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_person_card, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.personName.setText(people.get(position).getName());
        holder.personBackground.setText(people.get(position).getAcBd());
        holder.personLocations.setText(people.get(position).getLocations());
        holder.personSubjects.setText(people.get(position).getSubjects());
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
