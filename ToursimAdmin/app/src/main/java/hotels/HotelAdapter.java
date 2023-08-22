package hotels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toursimadmin.R;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private Context context;
    private List<HotelModel> hotelList;
    private String imgUrl = "http://192.168.43.202:8800/";

    public void setHotelList(List<HotelModel> hotelList) {
        this.hotelList = hotelList;
    }

    public HotelAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateHotelActivity.class);
                intent.putExtra("id", hotelList.get(position).getId());
                context.startActivity(intent);
            }
        });

        if(hotelList.get(position).getImg().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                    .into(holder.image);
        } else {
            String img = imgUrl + hotelList.get(position).getImg();
            Glide.with(context)
                    .asBitmap()
                    .load(img.replace("\\", "/"))
                    .into(holder.image);
//            Log.d("Images", "onBindViewHolder: " + tourismModelList.get(position).getCover_image());
        }

        holder.name.setText(hotelList.get(position).getName());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelCrud hotelCrud = new HotelCrud(context);
                hotelCrud.deleteHotel(hotelList.get(position).getId());
                hotelList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton delete;
        TextView name;
        ConstraintLayout card;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.delete_hotel);
            name = itemView.findViewById(R.id.hotel_name_list);
            card = itemView.findViewById(R.id.hotel_card);
            image = itemView.findViewById(R.id.hotel_image_cover_list);

            image.setClipToOutline(true);
        }
    }
}
