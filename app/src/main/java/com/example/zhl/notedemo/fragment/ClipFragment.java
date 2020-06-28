package com.example.zhl.notedemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhl.notedemo.R;
import com.example.zhl.notedemo.service.PasteCopyService;
import com.example.zhl.notedemo.utils.binding.ViewBinder;

/**
 * 基类<br>
 * Created by wcy on 2015/11/26.
 */
public  class ClipFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewBinder.bind(this, getView());
        PasteCopyService.startCommand(getActivity());
    }
}
