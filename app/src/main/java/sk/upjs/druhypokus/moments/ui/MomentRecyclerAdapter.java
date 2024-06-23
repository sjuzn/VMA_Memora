package sk.upjs.druhypokus.moments.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import sk.upjs.druhypokus.R;
import sk.upjs.druhypokus.moments.Entity.Moment;

public class MomentRecyclerAdapter extends RecyclerView.Adapter<MomentRecyclerAdapter.ViewHolder> {

    private List<Moment> momentList;
    private final Context context;

    public static Moment m;
    public static Context c;


    public MomentRecyclerAdapter(List<Moment> momentList, Context context) {
        this.momentList = momentList;
        sortMomentsByDateDescending(this.momentList);
        this.context = context;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateMoments(List<Moment> newMoments){
        momentList = newMoments;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MomentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_moment, parent, false);
        return new MomentRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MomentRecyclerAdapter.ViewHolder holder, int position) {
        c = context;
        if (!momentList.isEmpty()) {
            m = momentList.get(position);

            holder.nazovTextView.setText(momentList.get(position).getNazov());

            LocalDate datumObj = LocalDate.parse(momentList.get(position).getDatum(), DateTimeFormatter.ISO_DATE);
            Locale locale = context.getResources().getConfiguration().locale;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM \n yyyy", locale);
            String novyFormat = datumObj.format(formatter);

            holder.datumTextView.setText(novyFormat);
        }
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView datumTextView;
        TextView nazovTextView;
        ImageView imageViewMoment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            datumTextView = itemView.findViewById(R.id.datumTextView);
            nazovTextView = itemView.findViewById(R.id.nazovTextView);
            imageViewMoment = itemView.findViewById(R.id.imageViewMoment);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(c, ShowMomentActivity.class);
            intent.putExtra("moment", m);
            c.startActivity(intent);
        }
    }

    public static void sortMomentsByDateDescending(List<Moment> momentList) {

        Comparator<Moment> comparator = (moment1, moment2) -> {
            LocalDate date1 = LocalDate.parse(moment1.getDatum(), DateTimeFormatter.ISO_DATE);
            LocalDate date2 = LocalDate.parse(moment2.getDatum(), DateTimeFormatter.ISO_DATE);

            return date2.compareTo(date1);
        };

        momentList.sort(comparator);
    }
}
