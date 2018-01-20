package adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ituition.ituition.Profile;
import com.ituition.ituition.R;
import com.ituition.ituition.TutorProfile;

import java.util.ArrayList;
import java.util.Locale;

import model.Person;

/**
 * Created by tahmid on 12/4/17.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.UserViewHolder> {


    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView userCard;
        TextView personName;
        TextView personSubjects;
        TextView personLocations;
        TextView personBackground;
        RatingBar ratingBar;
        TextView ratingBarValue;
        TextView salary;
        ImageView personPhoto;

        public UserViewHolder(View itemView) {
            super(itemView);
            userCard = (CardView) itemView.findViewById(R.id.person_card_view);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personSubjects = (TextView) itemView.findViewById(R.id.person_subjects);
            personLocations = (TextView) itemView.findViewById(R.id.person_location);
            personBackground = (TextView) itemView.findViewById(R.id.person_background);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            ratingBar = (RatingBar) itemView.findViewById(R.id.pc_rating_bar);
            ratingBarValue = (TextView) itemView.findViewById(R.id.pc_rating_bar_value);
            ratingBar.setStepSize((float) 0.1);
            salary = (TextView) itemView.findViewById(R.id.person_salary);


            userCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), TutorProfile.class);
            String userName = (String) view.getTag();
            intent.putExtra("username", userName);
            view.getContext().startActivity(intent);
        }
    }

    private ArrayList<Person> people;

    public RVAdapter(ArrayList<Person> people) {
        this.people = people;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_card, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.userCard.setTag(people.get(position).getUsername());
        String val = String.format(Locale.ENGLISH, "%.1f", people.get(position).getRating());
        holder.personName.setText(people.get(position).getName());
        holder.personBackground.setText(people.get(position).getAcBd());
        holder.personLocations.setText(people.get(position).getLocations());
        holder.personSubjects.setText(people.get(position).getSubjects());
        holder.ratingBar.setRating((float) people.get(position).getRating());
        holder.salary.setText(people.get(position).getSalary());
        holder.ratingBarValue.setText(val);
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
