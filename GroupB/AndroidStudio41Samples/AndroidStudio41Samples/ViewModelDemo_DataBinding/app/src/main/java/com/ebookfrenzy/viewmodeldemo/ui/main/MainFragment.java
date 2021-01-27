package com.ebookfrenzy.viewmodeldemo.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebookfrenzy.viewmodeldemo.R;
import com.ebookfrenzy.viewmodeldemo.databinding.MainFragmentBinding;
import static com.ebookfrenzy.viewmodeldemo.BR.myViewModel;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    public MainFragmentBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.main_fragment, container, false);

        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setVariable(myViewModel, mViewModel);
    }

}