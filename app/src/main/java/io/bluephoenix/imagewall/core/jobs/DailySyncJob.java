package io.bluephoenix.imagewall.core.jobs;

import android.os.PowerManager;
import android.support.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.Job;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class DailySyncJob extends DailyJob
{
    DailySyncJob() { schedule(); }

    private void schedule()
    {
        //TODO schedule when the job needs to be done.
    }

    /**
     * This method is invoked from a background thread. You should run your desired task here.
     * This method is thread safe. Each time a job starts, a new instance of your {@link Job}
     * is instantiated and executed. You can identify your {@link Job} with the passed
     * {@code params}.
     * <p>
     * You should call {@link #isCanceled()} frequently for long running jobs and stop your
     * task if necessary.
     * <p>
     * A {@link PowerManager.WakeLock} is acquired for 3 minutes for each {@link Job}.
     * If your task needs more time, then you need to create an extra
     * {@link PowerManager.WakeLock}.
     *
     * @param params The parameters for this concrete job.
     * @return The result of this {@link DailyJob}.
     */
    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params)
    {
        //TODO do the actual job here. Return success for now.
        return DailyJobResult.SUCCESS;
    }

}
