package sk.upjs.druhypokus.welcome;
//https://www.geeksforgeeks.org/how-to-add-drag-and-drop-feature-in-android-recyclerview/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import sk.upjs.druhypokus.R;
import sk.upjs.druhypokus.milniky.Milestone;

public class WelcomeRecyclerAdapter extends RecyclerView.Adapter<WelcomeRecyclerAdapter.ViewHolder> {

    List<Milestone> milestonesList;

    public WelcomeRecyclerAdapter(List<Milestone> milestonesList) {
        this.milestonesList = milestonesList;
    }

    @NonNull
    @Override
    public WelcomeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.row_item_milestone, parent, false);
        return new WelcomeRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeRecyclerAdapter.ViewHolder holder, int position) {
        
            holder.rowCountTextView.setText(milestonesList.get(position).getZucastneni() + " | " + milestonesList.get(position).getDatum());
            holder.textView.setText(milestonesList.get(position).getTyp());
    }

    @Override
    public int getItemCount() {
        return milestonesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView, rowCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }

    }
}
