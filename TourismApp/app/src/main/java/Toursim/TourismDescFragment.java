package Toursim;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourismapp.ImageUrl;
import com.example.tourismapp.R;

public class TourismDescFragment extends Fragment {
    private TextView name, desc;
    private ImageView image;

    private Context context;
    private static final String imgUrl = ImageUrl.imageUrl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tourism_desc, container, false);
        context = getActivity().getApplicationContext();

        name = (TextView) view.findViewById(R.id.tourism_name);
        desc = (TextView) view.findViewById(R.id.tourism_desc);
        image = (ImageView) view.findViewById(R.id.tourism_cover_img_fragment);

        setComponents();
        // Inflate the layout for this fragment
        return view;
    }

    private void setComponents(){
        String nameStr = getActivity().getIntent().getStringExtra("name");
        String descStr = getActivity().getIntent().getStringExtra("desc");
        String imageStr = getActivity().getIntent().getStringExtra("image");
        if(imageStr.equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                    .into(image);
        } else {
            String img = imgUrl + imageStr;
            Glide.with(context)
                    .asBitmap()
                    .load(img.replace("\\", "/"))
                    .into(image);
//            Log.d("Images", "onBindViewHolder: " + tourismModelList.get(position).getCover_image());
        }

        name.setText(nameStr);
        desc.setText(descStr);

    }


}