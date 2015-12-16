package net.kyouko.cloudier.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kyouko.cloudier.R;

/**
 * Class for fragments containing a list of users.
 *
 * @author beta
 */
public class UserListFragment extends Fragment {

    // TODO: Inflate the user list.

    public UserListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

}
