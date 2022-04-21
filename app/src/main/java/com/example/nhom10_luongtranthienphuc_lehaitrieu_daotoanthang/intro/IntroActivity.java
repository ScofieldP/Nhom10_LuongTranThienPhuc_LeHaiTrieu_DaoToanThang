package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.window.SplashScreen;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.MainActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.SplashActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private MaterialButton buttonOnboardingAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        layoutOnboardingIndicators = findViewById(R.id.layoutonBoardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnBoardingAction);
        setupOnboardingItems();
        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);



        ViewPager2 onboardingViewPager = findViewById(R.id.onBoardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);
        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), SplashActivity.class );
            startActivity(mainActivity);
            finish();


        }
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onboardingViewPager.getCurrentItem() +1 < onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem()+1);
                }else{
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    savePrefsData();
                    finish();
                }
            }
        });
    }
    private void setupOnboardingItems(){
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();
        OnBoardingItem itemPayOnline = new OnBoardingItem();

        itemPayOnline.setImage(R.drawable.intro);

        OnBoardingItem itemOntheWay = new OnBoardingItem();

        itemOntheWay.setImage(R.drawable.intro1);

        OnBoardingItem itemEatTogether = new OnBoardingItem();

        itemEatTogether.setImage(R.drawable.intro2);

        onBoardingItems.add(itemPayOnline);
        onBoardingItems.add(itemOntheWay);
        onBoardingItems.add(itemEatTogether);

        onboardingAdapter = new OnboardingAdapter(onBoardingItems);

    }
    private void setupOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i = 0; i<indicators.length;i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }

    }
    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i<childCount;i++){
            ImageView imageView =(ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active)
                );
            }else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive)
                );
            }
            if(index == onboardingAdapter.getItemCount()-1){
                buttonOnboardingAction.setText("Start");

            }
            else{
                buttonOnboardingAction.setText("Next");
            }
        }
    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }


}