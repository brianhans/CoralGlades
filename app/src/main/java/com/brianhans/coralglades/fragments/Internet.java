package com.brianhans.coralglades.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.brianhans.coralglades.R;


/**
 * Created by Brian on 12/6/2014.
 */
public class Internet extends Fragment{

    public static final String ITEM_NUMBER = "item_number";
    private WebView webView;
    private View view;
    private FloatingActionButton back;

    public Internet()
    {

    }

    @Override
    public void onDestroy() {
        back.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        back = (FloatingActionButton) getActivity().findViewById(R.id.back);
        back.show();

        view = inflater.inflate(R.layout.internet_fragment, container, false);
        webView = (WebView) view.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        String site = getArguments().getString("site", "none");
        Log.d("onCreateView", "Ran " + site + back.isShown());
        if(site.equals("beep")) {
            webView.loadUrl("http://www.broward.k12.fl.us/casdl/textbooks/index.asp");
        }else if(site.equals("pinnacle"))
        {
            webView.loadUrl("http://gb.browardschools.com/pinnacle/gradebook/");
        }else if(site.equals("vc"))
        {
            webView.loadUrl("http://bcps.browardschools.com/VirtualCounselor/");
        }else if(site.equals("home"))
        {
            webView.loadUrl("https://twitter.com/PrincipalCGHS");
        }else
        {
            webView.loadUrl("https://twitter.com/" + site);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack())
                {
                    webView.goBack();
                }
            }
        });

        return view;

    }


}