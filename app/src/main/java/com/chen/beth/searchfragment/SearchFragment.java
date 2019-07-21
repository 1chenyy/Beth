package com.chen.beth.searchfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.databinding.FragmentSearchBinding;
import com.chen.beth.models.MinerMark;
import com.google.android.material.textfield.TextInputLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private static final int TYPE_TX = 0;
    private static final int TYPE_ACCOUNT = 1;
    private static final int TYPE_BLOCK = 2;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        binding.setHandle(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.card1.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.search_card_anim));
        binding.card2.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.search_card_anim));
        binding.card3.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.search_card_anim));
        binding.rvHistory.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alpha_in_anim));
        binding.tvHistory.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alpha_in_anim));

    }

    public void onCardClick(View view){
        showEditDialog(view.getId());
    }

    public void showEditDialog(int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_miner_mark_layout,null);
        TextInputLayout textInputLayout = view.findViewById(R.id.edit_layout);
        EditText editText = view.findViewById(R.id.et_miner);
        textInputLayout.setCounterEnabled(true);
        switch (type){
            case R.id.card1:
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_tx));
                break;
            case R.id.card2:
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_account));
                break;
            case R.id.card3:
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_block));
                break;
        }
        builder.setView(view);
        builder.setPositiveButton(R.string.menu_search,null);
        builder.setNegativeButton(BaseUtil.getString(R.string.bt_return),null);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button searchButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        searchButton.setOnClickListener(v->{
            String str = editText.getText().toString();
            String longHex = "\\b0x[0-9a-fA-F]+\\b";
            String shortHex = "\\b[0-9a-fA-F]+\\b";
            switch (type){
                case R.id.card1:
                    if (str.startsWith("0x")){
                        if (str.length()!=66){
                            editText.setError(BaseUtil.getString(R.string.err_tx_length));
                            break;
                        }else if(!str.matches(longHex)){
                            editText.setError(BaseUtil.getString(R.string.error_hash));
                            break;
                        }
                    }else{
                        if (str.length()!=64){
                            editText.setError(BaseUtil.getString(R.string.err_tx_length));
                            break;
                        }else if(!str.matches(shortHex)){
                            editText.setError(BaseUtil.getString(R.string.error_hash));
                            break;
                        }
                    }
                    break;
                case R.id.card2:
                    if (!TextUtils.isDigitsOnly(str)){
                        editText.setError(BaseUtil.getString(R.string.error_block));
                        break;
                    }
                    break;
                case R.id.card3:
                    if (str.startsWith("0x")){
                        if (str.length()!=42){
                            editText.setError(BaseUtil.getString(R.string.error_account_length));
                            break;
                        }else if(!str.matches(longHex)){
                            editText.setError(BaseUtil.getString(R.string.error_hash));
                            break;
                        }
                    }else{
                        if (str.length()!=40){
                            editText.setError(BaseUtil.getString(R.string.error_account_length));
                            break;
                        }else if(!str.matches(shortHex)){
                            editText.setError(BaseUtil.getString(R.string.error_hash));
                            break;
                        }
                    }
                    break;
            }
        });
    }
}
