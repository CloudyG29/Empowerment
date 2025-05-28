package com.nullandvoid.empowerment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LeaderBoardFragment extends Fragment {

    private LeaderBoardViewModel mViewModel;

    public static LeaderBoardFragment newInstance() {
        return new LeaderBoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Menu.hideProgressBar();
        return inflater.inflate(R.layout.fragment_leader_board, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LeaderBoardViewModel.class);
        // TODO: Use the ViewModel
    }

}