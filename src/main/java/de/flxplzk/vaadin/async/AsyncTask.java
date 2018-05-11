package de.flxplzk.vaadin.async;

import de.flxplzk.vaadin.exception.CommonAddOnException;

import java.util.concurrent.ForkJoinPool;

/**
 * This implementation aims to simplify the execution of AsyncTasks on a different thread.
 *
 * @param <P> Type of the params the doInBackground method
 * @param <R> Type of the computed Result
 */
public class AsyncTask<P, R> {
    private boolean isExecuting = false;

    private final BackgroundHandler<P, R> backgroundHandler;
    private PostExecuteHandler<R> postExecuteHandler;

    private PreExecuteHandler preExecuteHandler;
    private ErrorHandler errorHandler;

    public AsyncTask(BackgroundHandler<P, R> backgroundHandler) {
        this.backgroundHandler = backgroundHandler;
    }

    public AsyncTask<P, R> withPreExecutionHandler(PreExecuteHandler preExecuteHandler) {
        this.preExecuteHandler = preExecuteHandler;
        return this;
    }

    public AsyncTask<P, R> withPostExecutionHandler(PostExecuteHandler<R> postExecuteHandler) {
        this.postExecuteHandler = postExecuteHandler;
        return this;
    }

    public AsyncTask<P, R> withErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }
    /**
     * is called to fire an AsyncTask. when finished it will call onPostExecute,
     * for instance to post an AsyncTaskFinishedEvent to the EventBus
     *
     * @param params Type of the params the doInBackground method
     */
    public void execute(P... params) {
        if (!isExecuting) {
            this.isExecuting = true;
            this.onPreExecute();
            ForkJoinPool.commonPool().submit(() -> {
                try {
                    R result = this.backgroundHandler.doInBackground(params);
                    this.onPostExecute(result);
                } catch (RuntimeException cause) {
                    if (this.errorHandler == null) throw cause;
                    this.errorHandler.onError(cause);                }
            });
        } else{
            throw new CommonAddOnException.AsyncTaskException("Task is already executing");
        }
    }

    private void onPostExecute(R result) {
        this.isExecuting = false;
        this.postExecuteHandler.onPostExecute(result);
    }

    private void onPreExecute() {
        if (this.preExecuteHandler == null) return;
        this.preExecuteHandler.preExecute();
    }

    /**
     * handler for pre execution tasks
     */
    @FunctionalInterface
    public interface PreExecuteHandler {
        void preExecute();
    }

    /**
     * handler for background compution
     *
     * @param <P> param type of Async Task
     * @param <R> param type of async task's result
     */
    @FunctionalInterface
    public interface BackgroundHandler<P, R> {
        R doInBackground(P... params);
    }

    /**
     * handler for post execution tasks
     *
     * @param <R> param type of async task's result
     */
    @FunctionalInterface
    public interface PostExecuteHandler<R> {
        void onPostExecute(R result);
    }

    /**
     * error handler
     */
    @FunctionalInterface
    public interface ErrorHandler {
        void onError(RuntimeException e);
    }
}
