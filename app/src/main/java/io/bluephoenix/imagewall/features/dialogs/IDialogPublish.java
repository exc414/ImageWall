package io.bluephoenix.imagewall.features.dialogs;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public interface IDialogPublish
{
    //Check that the current password is not invalid/wrong.
    void authSuccess();
    void authFailed();
}
