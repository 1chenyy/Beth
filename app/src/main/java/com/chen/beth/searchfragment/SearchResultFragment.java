package com.chen.beth.searchfragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.beth.BaseFragment;
import com.chen.beth.R;
import com.chen.beth.Utils.Const;
import com.chen.beth.databinding.FragmentSearchResultBinding;

import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends BaseFragment {
    private FragmentSearchResultBinding binding;
    private int type;
    private int src;
    private String name;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getInt(Const.ARG_TYPE,-1);
        src = bundle.getInt(Const.ARG_SRC,-1);
        name = bundle.getString(Const.ARG_TRANSITION_NAME,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchResultBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        binding.iv.setImageResource(src);
        binding.iv.setTransitionName(name);
        return binding.getRoot();
    }
    @Subscribe
    public void event(Float f){

    }

}
