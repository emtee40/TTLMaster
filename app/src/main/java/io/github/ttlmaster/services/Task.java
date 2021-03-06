package io.github.ttlmaster.services;

import android.os.AsyncTask;

public abstract class Task<Parameters,Result> extends AsyncTask<Parameters,Void,Result> {

    private OnResult<Result> callback;


    private Exception exception;

    @SafeVarargs
    @Override
    protected final Result doInBackground(Parameters... params) {
        return action(params[0]);
    }

    @Override
    protected void onPostExecute(Result result) {
        if (callback != null) {
            if (result == null && exception != null) {
                callback.onError(exception);
            } else {
                callback.onResult(result);
            }
        }
    }


    abstract Result action(Parameters p);

    public Result runInForeground(Parameters parameters) {
        Result r = action(parameters);
        onPostExecute(r);
        return r;
    }

    // welcome to java shit-generics world
    @SuppressWarnings("unchecked")
    public void runInBackground(Parameters parameters) {
        execute(parameters);
    }

    public Task<Parameters,Result> attach(OnResult<Result> callback) {
        this.callback = callback;
        return this;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }


    public interface OnResult<Result> {
        void onResult(Result r);
        void onError(Exception e);
    }
}
