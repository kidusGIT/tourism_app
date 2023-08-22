package Tourism;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toursimadmin.MainActivity;
import com.example.toursimadmin.R;

import java.util.List;

public class TourismAdapter extends RecyclerView.Adapter<TourismAdapter.ViewHolder> {
    private Context context;
    private List<TourismModel> tourismModelList;
    private String imgUrl = "http://192.168.43.202:8800/";

    public TourismAdapter(Context context) {
        this.context = context;
    }

    public void setTourismModelList(List<TourismModel> tourismModelList) {
        this.tourismModelList = tourismModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.toursim_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(tourismModelList.get(position).getCover_image().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                    .into(holder.img);
        } else {
            String img = imgUrl + tourismModelList.get(position).getCover_image();
            Glide.with(context)
                    .asBitmap()
                    .load(img.replace("\\", "/"))
                    .into(holder.img);
//            Log.d("Images", "onBindViewHolder: " + tourismModelList.get(position).getCover_image());
        }

        holder.name.setText(tourismModelList.get(position).getName());
        holder.list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", tourismModelList.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TourismCrud tourismCrud = new TourismCrud(context);
                tourismCrud.deleteTourism(tourismModelList.get(position).getId());
                tourismModelList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return tourismModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        ImageButton delete;
        CardView list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.tourism_pic);
            name = itemView.findViewById(R.id.touersim_list_name);
            delete = itemView.findViewById(R.id.delete_toursim_btn);
            list = itemView.findViewById(R.id.tourism_card);

            img.setClipToOutline(true);
            delete.setClipToOutline(true);
        }
    }
}
