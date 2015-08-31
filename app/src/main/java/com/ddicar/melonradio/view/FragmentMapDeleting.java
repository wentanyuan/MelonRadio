package com.ddicar.melonradio.view;

import com.ddicar.melonradio.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** 
 * @author wentanyuan 
 *  功能描述：地图页面 
 */  
public class FragmentMapDeleting extends Fragment {  
  
    private View mParent;  
      
    private FragmentActivity mActivity;  
    
    private TextView mText; 
    /** 
     * Create a new instance of DetailsFragment, initialized to show the text at 
     * 'index'. 
     */  
    public static FragmentMapDeleting newInstance(int index) {  
    	FragmentMapDeleting f = new FragmentMapDeleting();  
  
        // Supply index input as an argument.  
        Bundle args = new Bundle();  
        args.putInt("index", index);  
        f.setArguments(args);
  
        return f;  
    }  
  
    public int getShownIndex() {  
        return getArguments().getInt("index", 2);  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.map_new, container, false);  
        return view;  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        mActivity = getActivity();  
        mParent = getView();  
  
        
  
    }  
      
    private void goHelpActivity() {  
        //Intent intent = new Intent(mActivity, HelpActivity.class);  
        //startActivity(intent);  
    }  
  
    @Override  
    public void onHiddenChanged(boolean hidden) {  
        super.onHiddenChanged(hidden);  
    }  
  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
    }  
  
} 
