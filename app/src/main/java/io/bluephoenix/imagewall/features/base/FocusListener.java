package io.bluephoenix.imagewall.features.base;

import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class FocusListener implements View.OnFocusChangeListener
{
    private int[] editTextsId;
    private List<TextView> textViews;

    public FocusListener(int[] editTextsId, List<TextView> textViews)
    {
        this.editTextsId = editTextsId;
        this.textViews = textViews;
    }

    @Override
    public void onFocusChange(View view, boolean focus)
    {
        for(int i = 0; i < editTextsId.length; i++)
        {
            if(view.getId() == editTextsId[i])
            {
                textViews.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }
}
