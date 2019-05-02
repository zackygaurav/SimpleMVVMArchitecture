package in.itechvalley.topmovies.executors;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors
{
    private static AppExecutors INSTANCE;

    private final Executor diskIo;
    private final Executor networkIo;
    private final Executor mainThreadIo;

    /*
    * Constructor
    * */
    public AppExecutors(Executor diskIo, Executor networkIo, Executor mainThreadIo)
    {
        this.diskIo = diskIo;
        this.networkIo = networkIo;
        this.mainThreadIo = mainThreadIo;
    }

    /*
    * Singleton Approach to return the Instance of this class.
    * */
    public static AppExecutors getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new AppExecutors(
                    Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(3),
                    new MainThreadExecutor()
            );
        }

        return INSTANCE;
    }

    private static class MainThreadExecutor implements Executor
    {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command)
        {
            mainThreadHandler.post(command);
        }
    }

    public Executor getDiskIo()
    {
        return diskIo;
    }

    public Executor getNetworkIo()
    {
        return networkIo;
    }

    public Executor getMainThreadIo()
    {
        return mainThreadIo;
    }
}
