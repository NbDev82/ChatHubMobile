package com.example.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.R;
import com.example.chat.message.MessagesFragment;
import com.example.databinding.ActivityHomeBinding;
import com.example.friend.friendrequest.FriendRequestsFragment;
import com.example.infrastructure.BaseActivity;
import com.example.setting.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends BaseActivity<HomeViewModel, ActivityHomeBinding> {

    private MenuItem prevMenuItem;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected Class<HomeViewModel> getViewModelClass() {
        return HomeViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_messages) {
                    binding.viewPager.setCurrentItem(0);
                    return true;
                }
                if (itemId == R.id.bottom_friends) {
                    binding.viewPager.setCurrentItem(1);
                    return true;
                }
                if (itemId == R.id.bottom_settings) {
                    binding.viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = binding.bottomNavigation.getMenu().getItem(position);
            }
        });

        setupViewPager();
    }

    private void setupViewPager() {
        MessagesFragment messagesFragment = new MessagesFragment();
        FriendRequestsFragment friendRequestsFragment = new FriendRequestsFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(messagesFragment);
        adapter.addFragment(friendRequestsFragment);
        adapter.addFragment(settingsFragment);

        binding.setViewPagerAdapter(adapter);
    }
}