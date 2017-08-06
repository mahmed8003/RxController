package io.hibit.rxcontroller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 *
 */
public abstract class RxController extends Controller {

    private final Map<ControllerEvent, CompositeDisposable> disposables = new ConcurrentHashMap<>(5);
    private ControllerEvent lastEvent = ControllerEvent.CREATE;

    public RxController() {
        this(null);
    }

    public RxController(@Nullable Bundle args) {
        super(args);
        this.addLifecycleListener(new RxControllerLifecycleHelper());
    }

    public final void dispose(Disposable disposable, ControllerEvent event) {
        addDisposable(disposable, event);
    }

    public final void dispose(Disposable disposable) {
        final ControllerEvent lastEvent = this.lastEvent;
        ControllerEvent event = null;
        switch (lastEvent) {
            case CREATE:
                event = ControllerEvent.DESTROY;
                break;

            case ATTACH:
                event = ControllerEvent.DETACH;
                break;

            case CREATE_VIEW:
                event = ControllerEvent.DESTROY_VIEW;
                break;

            case DETACH:
                event = ControllerEvent.DESTROY;
                break;

            default:
                throw new UnsupportedOperationException(String.format("Event can't be disposed at eventType : %s", lastEvent.name()));
        }

        this.addDisposable(disposable, event);
    }


    //
    private void addDisposable(Disposable disposable, ControllerEvent event) {
        CompositeDisposable d = getDisposable(event);
        d.add(disposable);
    }

    private void onEvent(ControllerEvent event) {
        this.lastEvent = event;
        this.disposeAndRemove(event);
    }

    private void disposeAndRemove(ControllerEvent event) {
        if (disposables.containsKey(event)) {
            CompositeDisposable disposable = disposables.get(event);
            disposable.dispose();
            disposable.clear();
            disposables.remove(event);
        }
    }


    private CompositeDisposable getDisposable(ControllerEvent event) {
        CompositeDisposable disposable = null;
        if (disposables.containsKey(event)) {
            disposable = disposables.get(event);
        } else {
            disposable = new CompositeDisposable();
            disposables.put(event, disposable);
        }

        return disposable;
    }


    /**
     *
     */
    private class RxControllerLifecycleHelper extends LifecycleListener {

        @Override
        public void preCreateView(@NonNull Controller controller) {
            onEvent(ControllerEvent.CREATE_VIEW);
        }

        @Override
        public void preAttach(@NonNull Controller controller, @NonNull View view) {
            onEvent(ControllerEvent.ATTACH);
        }

        @Override
        public void preDetach(@NonNull Controller controller, @NonNull View view) {
            onEvent(ControllerEvent.DETACH);
        }

        @Override
        public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
            onEvent(ControllerEvent.DESTROY_VIEW);
        }

        @Override
        public void preDestroy(@NonNull Controller controller) {
            onEvent(ControllerEvent.DESTROY);
        }

    }
}
