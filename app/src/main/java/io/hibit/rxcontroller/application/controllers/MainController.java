package io.hibit.rxcontroller.application.controllers;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import io.hibit.rxcontroller.ControllerEvent;
import io.hibit.rxcontroller.RxController;
import io.hibit.rxcontroller.application.R;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;




public class MainController extends RxController {

    public static final String TAG = MainController.class.getSimpleName();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        final View view = inflater.inflate(R.layout.controller_main, container, false);

        disposeObserverExample();

        return view;
    }


    private void disposeObserverExample() {
        Disposable d = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG, value.toString() + " Seconds");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

        /*
         * Dispose observer
         */
        dispose(d);
    }
}
