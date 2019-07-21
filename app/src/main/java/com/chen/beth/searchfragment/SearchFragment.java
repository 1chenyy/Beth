package com.chen.beth.searchfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chen.beth.BaseFragment;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.databinding.FragmentSearchBinding;
import com.chen.beth.models.MinerMark;
import com.google.android.material.textfield.TextInputLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {
    private FragmentSearchBinding binding;


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
//        binding.cardTx.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.search_card_anim));
//        binding.cardAccount.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.search_card_anim));
//        binding.cardBlock.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.search_card_anim));
        binding.rvHistory.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alpha_in_anim));
        binding.tvHistory.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alpha_in_anim));

    }

    public void onCardClick(View view){
//        if (view.getId()==R.id.card_account){
//            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
//                    .addSharedElement(view.findViewById(R.id.iv_account),getString(R.string.shared_element_search_account))
//                    .build();
//            Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_searchResultFragment,
//                    null,null,extras);
//            return;
//        }
        showEditDialog(view.getId(),view);
    }

    public void showEditDialog(int type,View nav){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_miner_mark_layout,null);
        TextInputLayout textInputLayout = view.findViewById(R.id.edit_layout);
        EditText editText = view.findViewById(R.id.et_miner);
        textInputLayout.setCounterEnabled(true);
        switch (type){
            case R.id.card_tx:
                builder.setTitle(R.string.search_transaction);
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_tx));
                break;
            case R.id.card_account:
                builder.setTitle(R.string.search_account);
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_account));
                break;
            case R.id.card_block:
                builder.setTitle(R.string.search_block);
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_block));
                break;
        }

        builder.setView(view);
        builder.setPositiveButton(R.string.menu_search,null);
        builder.setNegativeButton(R.string.bt_return,null);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button searchButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        searchButton.setOnClickListener(v->{
            String str = editText.getText().toString();
            String longHex = "\\b0x[0-9a-fA-F]+\\b";
            String shortHex = "\\b[0-9a-fA-F]+\\b";
            switch (type){
                case R.id.card_tx:
                    if (str.startsWith("0x")){
                        if (str.length()!=66){
                            editText.setError(BaseUtil.getString(R.string.err_tx_length));
                            break;
                        }else if(!str.matches(longHex)){
                            editText.setError(BaseUtil.getString(R.string.error_hash));
                            break;
                        }
                        dialog.dismiss();

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
                case R.id.card_account:
                    if (!TextUtils.isDigitsOnly(str)){
                        editText.setError(BaseUtil.getString(R.string.error_block));
                        break;
                    }
                    dialog.dismiss();
                    jump(Const.TYPE_ACCOUNT,R.id.iv_account,nav,R.drawable.ic_account);
                    break;
                case R.id.card_block:
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

    public void jump(int type,int id,View view,int src){
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ARG_TYPE,type);
        ImageView sharedView = view.findViewById(id);
        bundle.putString(Const.ARG_TRANSITION_NAME,sharedView.getTransitionName());
        bundle.putSerializable(Const.ARG_SRC,src);
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(sharedView,sharedView.getTransitionName()).build();
        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_searchResultFragment,
                bundle,null,extras);
    }

    @Subscribe
    public void event(Float f){

    }
}
