package io.bluephoenix.imagewall.features.favourite;

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
public class FavouriteFragment extends Fragment implements IFavouriteContract.PublishToView
{
    @BindView(R.id.txtFavouriteFragment)
    TextView txtFavouriteFragment;

    public FavouriteFragment() { }

    public static FavouriteFragment newInstance(String text)
    {
        FavouriteFragment favouriteFragment = new FavouriteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", text);
        favouriteFragment.setArguments(bundle);

        return favouriteFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);

        txtFavouriteFragment.setText(getArguments().getString("msg"));

        return view;
    }
}
