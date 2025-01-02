package com.example.bustracking.ui.notifications;


import androidx.fragment.app.Fragment;

import com.example.bustracking.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}