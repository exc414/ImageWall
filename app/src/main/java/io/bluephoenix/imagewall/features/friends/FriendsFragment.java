package io.bluephoenix.imagewall.features.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bluephoenix.imagewall.R;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class FriendsFragment extends Fragment implements IFriendsContract.PublishToView
{
    @BindView(R.id.txtFriendsFragment)
    TextView txtFriendsFragment;

    public FriendsFragment() { }

    public static FriendsFragment newInstance(String text)
    {
        FriendsFragment friendsFragment = new FriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", text);
        friendsFragment.setArguments(bundle);
        return friendsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);

        txtFriendsFragment.setText(getArguments().getString("msg"));

        return view;
    }
}
