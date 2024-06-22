package sk.upjs.druhypokus.bucketList;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import sk.upjs.druhypokus.R;
import sk.upjs.druhypokus.moments.ui.AddMomentActivity;

public class BListRecyclerAdapter extends RecyclerView.Adapter<BListRecyclerAdapter.ViewHolder> {

    Context context;

    BListViewModel bListViewModel;
    List<BList> bListList;


    public BListRecyclerAdapter(List<BList> bListList, BListViewModel bListViewModel, Context context) {
        this.bListList = bListList;
        this.bListViewModel = bListViewModel;
        this.context = context;
    }

    @NonNull
    @Override
    public BListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_bucket_list, parent, false);
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

                //ked bol zaskrtnuty = splnili sme =  opytame sa ci chceme vytvorit sspomienku
                if(isChecked){
                    new AlertDialog.Builder(context)
                            .setTitle("Create Memory")
                            .setMessage("Do you want to create a memory for this task?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, AddMomentActivity.class);
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
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