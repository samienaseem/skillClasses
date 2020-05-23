package com.learning.skilclasses.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.ProfileActivity;
import com.learning.skilclasses.authentication.SignupActivity;
import com.learning.skilclasses.models.SubCategoriesModel;
import com.learning.skilclasses.preferences.UserSession;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder> {

    private List<SubCategoriesModel> videoList;
    private Context context;
    UserSession userSession;
    boolean updateCourse;
    String category;

    public SubCategoriesAdapter(List<SubCategoriesModel> messageList, Context context, String category, boolean updateCourse) {
        this.videoList = messageList;
        this.context = context;
        userSession = new UserSession(context);
        this.updateCourse = updateCourse;
        this.category = category;
    }

    @NonNull
    @Override
    public SubCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategories_layout, parent, false);
        return new SubCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesAdapter.ViewHolder holder, int position) {
        SubCategoriesModel subCategoriesModel = videoList.get(position);
        try {
            holder.categoryName.setText(subCategoriesModel.getSubcategory());
            holder.categoryPrice.setText("Price \u20B9 " + subCategoriesModel.getPrice());
            holder.card.setOnClickListener(v -> {
                holder.card.setBackgroundColor(context.getColor(R.color.colorPrimary));
                if (!updateCourse) {
                    Intent intent = new Intent(context, SignupActivity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("subcategory", subCategoriesModel.getSubcategory());
                    intent.putExtra("subcategory_price", subCategoriesModel.getPrice());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("subcategory", subCategoriesModel.getSubcategory());
                    intent.putExtra("subcategory_price", subCategoriesModel.getPrice());
                    userSession.setUserCourse(category, subCategoriesModel.getSubcategory(), subCategoriesModel.getPrice());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.subcategory_name)
        TextView categoryName;
        @BindView(R.id.subcategory_price)
        TextView categoryPrice;
        @BindView(R.id.card)
        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
