package sk.upjs.druhypokus.bucketList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sk.upjs.druhypokus.R;

public class BListRecyclerAdapter extends RecyclerView.Adapter<BListRecyclerAdapter.ViewHolder> {

    BListViewModel bListViewModel;
    List<BList> bListList;
    boolean hotove;

    public BListRecyclerAdapter(List<BList> bListList, BListViewModel bListViewModel) {
        this.bListList = bListList;
        this.hotove = hotove;
        this.bListViewModel = bListViewModel;
    }

    @NonNull
    @Override
    public BListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bucket_list_item, parent, false);
        return new BListRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BListRecyclerAdapter.ViewHolder holder, int position) {

        if(!bListList.isEmpty()) {
            holder.textView.setText(bListList.get(position).getTask());
            holder.checkBox.setChecked(bListList.get(position).getHotovo());


            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                BList b = bListList.get(position);
                b.setHotovo(isChecked);
                bListList.remove(position);
                bListViewModel.update(b);
            });
        }
    }

    @Override
    public int getItemCount() {
        return bListList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }

    }
}