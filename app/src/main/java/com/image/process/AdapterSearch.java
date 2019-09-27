
package com.image.process;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AdapterSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

    private List<JsonResItem> myItems;
    private ItemListener myListener;
    private List<JsonResItem> filterArrayList;
    private Context mycontext;

    public AdapterSearch(List<JsonResItem> items, Context context) {
        myItems = items;
        this.mycontext = context;
        this.filterArrayList = items;
    }

    public void setListener(ItemListener listener) {
        myListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mycontext).inflate(R.layout.adapter_images, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return filterArrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Holder myHolder = (Holder) holder;
        final JsonResItem model = filterArrayList.get(position);
        //new programming
        final float scale = mycontext.getResources().getDisplayMetrics().density;
        int linearleft =  (int)(Integer.parseInt(model.getLinearMS()) * scale + 0.5f);
        int lineartop =  (int)(Integer.parseInt(model.getLinearMT()) * scale + 0.5f);
        int nameleft =  (int)(Integer.parseInt(model.getNameMS()) * scale + 0.5f);
        int nametop =  (int)(Integer.parseInt(model.getNameMT()) * scale + 0.5f);
        int desigleft =  (int)(Integer.parseInt(model.getDesigMS()) * scale + 0.5f);
        int desigtop =  (int)(Integer.parseInt(model.getDesigMT()) * scale + 0.5f);
        int pichight =  (int)(Integer.parseInt(model.getImageH()) * scale + 0.5f);
        int picwidth =  (int)(Integer.parseInt(model.getImageW()) * scale + 0.5f);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) myHolder.lpic.getLayoutParams();
        layoutParams.setMargins(linearleft,lineartop, 0, 0);
        myHolder.lpic.setLayoutParams(layoutParams);

        ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) myHolder.name.getLayoutParams();
        layoutParams2.setMargins(nameleft,nametop, 0, 0);
        myHolder.name.setLayoutParams(layoutParams2);
        myHolder.name.setTextColor(Color.parseColor(model.getNameFontC()));
        myHolder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,Integer.parseInt(model.getNameFontSize()));
        if(model.getNameFontFam().equalsIgnoreCase("khandbold")){
            Typeface typeface = ResourcesCompat.getFont(mycontext, R.font.khandbold);
            myHolder.name.setTypeface(typeface);
        }else if(model.getNameFontFam().equalsIgnoreCase("BalBharatiDev")){
            Typeface typeface = ResourcesCompat.getFont(mycontext, R.font.balbharatidev);
            myHolder.name.setTypeface(typeface);
        }else if(model.getNameFontFam().equalsIgnoreCase("utsaahb")){
            Typeface typeface = ResourcesCompat.getFont(mycontext, R.font.utsaahb);
            myHolder.name.setTypeface(typeface);
        }

        ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) myHolder.desig.getLayoutParams();
        layoutParams3.setMargins(desigleft,desigtop, 0, 0);
        myHolder.desig.setLayoutParams(layoutParams3);
        myHolder.desig.setTextColor(Color.parseColor(model.getDesigFontC()));
        myHolder.desig.setTextSize(TypedValue.COMPLEX_UNIT_SP,Integer.parseInt(model.getDesigFontSize()));
        if(model.getDesigFontFam().equalsIgnoreCase("khandbold")){
            Typeface typeface = ResourcesCompat.getFont(mycontext, R.font.khandbold);
            myHolder.desig.setTypeface(typeface);
        }else if(model.getDesigFontFam().equalsIgnoreCase("BalBharatiDev")){
            Typeface typeface = ResourcesCompat.getFont(mycontext, R.font.balbharatidev);
            myHolder.desig.setTypeface(typeface);
        }else if(model.getNameFontFam().equalsIgnoreCase("utsaahb")){
            Typeface typeface = ResourcesCompat.getFont(mycontext, R.font.utsaahb);
            myHolder.desig.setTypeface(typeface);
        }

        ViewGroup.LayoutParams params = myHolder.picture.getLayoutParams();
        params.height = pichight;
        params.width = picwidth;
        myHolder.picture.setLayoutParams(params);

        myHolder.name.setText(model.getName());
        myHolder.desig.setText(model.getDesignation());
        myHolder.uid.setText(model.getPhoneNumber());
        Glide.with(mycontext)
                .load(model.getPersonImageUrl())
                .into(myHolder.picture);

        Glide.with(mycontext)
                .load(model.getPostImageUrl())
                .into(myHolder.background);

        myHolder.clickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MainActivity().process(mycontext,model.getId(), myHolder.linearLayout);
            }
        });

        myHolder.uid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new MainActivity().process(mycontext,model.getId(), myHolder.linearLayout);
                if(file != null){
                    if(!model.getPhoneNumber().equalsIgnoreCase("")){
                        new MainActivity().checkWhatsApp(mycontext,model.getPhoneNumber(),file);
                    }else{
                        new MainActivity().sentToWhatsapp(mycontext,file);
                    }
                }
            }
        });

        myHolder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new MainActivity().process(mycontext,model.getId(), myHolder.linearLayout);
                if(file != null){
                    if(!model.getPhoneNumber().equalsIgnoreCase("")){
                        new MainActivity().popup(mycontext,model.getPhoneNumber(),file);
                    }else{
                        new MainActivity().sentToWhatsapp(mycontext,file);
                    }
                }
            }
        });



        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MainActivity().deleteApi(mycontext,model.getId(),model.getBannerDate(),model.getClientCd());
            }
        });

        //ends here


    }

    public interface ItemListener {
        void onItemClick(JsonResItem item);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView uid;
        ImageButton clickimage,whatsapp,gmail,delete;
        //anil bonde
        TextView name,desig;
        ImageView picture,background;
        private LinearLayout linearLayout,lpic;
        //vijay nahata
        TextView vijaynahataname;
        ImageView  vijaynahatapicture, vijaynahatabackground;
        LinearLayout vijaynahatalinlay;

        public Holder(@NonNull View itemView) {
            super(itemView);
            uid = itemView.findViewById(R.id.uid);
            clickimage = itemView.findViewById(R.id.clickimage);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            //anil bonde
            desig = itemView.findViewById(R.id.desig);
            name = itemView.findViewById(R.id.name);
            picture = itemView.findViewById(R.id.picture);
            background = itemView.findViewById(R.id.background);
            linearLayout = itemView.findViewById(R.id.anilbonde);
            lpic = itemView.findViewById(R.id.lpic);
            //gmail = itemView.findViewById(R.id.gmail);
            delete = itemView.findViewById(R.id.delete);
            //vijay nahata
                                           /*vijaynahataname = itemView.findViewById(R.id.vijaynahataname);
                                           vijaynahatapicture = itemView.findViewById(R.id.vijaynahatapicture);
                                           vijaynahatabackground = itemView.findViewById(R.id.vijaynahatabackground);
                                           vijaynahatalinlay = itemView.findViewById(R.id.vijaynahata);*/
        }
    }

    public List<JsonResItem> filterSearchPosition() {
        return filterArrayList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filterArrayList = myItems;
                } else {

                    ArrayList<JsonResItem> filteredList = new ArrayList<>();

                    for (JsonResItem voter : myItems) {

                        if (voter.getName().toLowerCase().contains(charString) ||
                                voter.getName().toUpperCase().contains(charString) ||
                                voter.getPhoneNumber().contains(charString)) {

                            filteredList.add(voter);
                        }
                    }
                    filterArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterArrayList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {
                filterArrayList = (ArrayList<JsonResItem>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public List<JsonResItem> searchFilteredData() {
        return filterArrayList;
    }


}
                                