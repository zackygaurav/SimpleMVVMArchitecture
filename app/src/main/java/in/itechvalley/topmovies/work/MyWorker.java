package in.itechvalley.topmovies.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker
{
    /*
    * Constructor
    * */
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Data inputData = getInputData();
        String message = inputData.getString("test_key");

        WorkerUtils.makeStatusNotification(message, getApplicationContext());

        return Result.success();
    }
}
