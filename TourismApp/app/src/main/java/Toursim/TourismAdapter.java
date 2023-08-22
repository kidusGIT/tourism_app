package Toursim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourismapp.DetailActivity;
import com.example.tourismapp.ImageUrl;
import com.example.tourismapp.R;

import java.util.List;

public class TourismAdapter extends RecyclerView.Adapter<TourismAdapter.ViewHolder> {
    private Context context;
    private List<TourismModel> tourismList;
    private static final String imgUrl = ImageUrl.imageUrl;

    public TourismAdapter(Context context) {
        this.context = context;
    }

    public void setTourismList(List<TourismModel> tourismList) {
        this.tourismList = tourismList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.toursim_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(tourismList.get(position).getCover_image().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                    .into(holder.image);
        } else {
            String img = imgUrl + tourismList.get(position).getCover_image();
            Glide.with(context)
                    .asBitmap()
                    .load(img.replace("\\", "/"))
                    .into(holder.image);
//            Log.d("Images", "onBindViewHolder: " + tourismModelList.get(position).getCover_image());
        }

        holder.name.setText(tourismList.get(position).getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name", tourismList.get(position).getName());
                intent.putExtra("image", tourismList.get(position).getCover_image());
                intent.putExtra("desc", tourismList.get(position).getDesc());
                intent.putExtra("id", tourismList.get(position).getId());

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tourismList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tourism_cover_image);
            name = itemView.findViewById(R.id.tourism_name);
            card = itemView.findViewById(R.id.toursim_card);

            image.setClipToOutline(true);

        }
    }
}
