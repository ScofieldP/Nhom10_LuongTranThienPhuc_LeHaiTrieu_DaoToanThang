package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.intro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{
    private TextView textTitle;
    private TextView textDes;
    private ImageView imgOnboarding;

    public OnboardingAdapter(List<OnBoardingItem> onBoardingItems) {
        this.onBoardingItems = onBoardingItems;
    }

    private List<OnBoardingItem> onBoardingItems;
    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_onboarding, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onBoardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder{
        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);

            imgOnboarding = itemView.findViewById(R.id.imageOnboarding);

        }
        void setOnboardingData(OnBoardingItem onBoardingItem){

            imgOnboarding.setImageResource(onBoardingItem.getImage());
        }


    }
}
