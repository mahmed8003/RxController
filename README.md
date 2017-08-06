# RxController
This library handles disposing of Observer on different lifecycle events of [Conductor](https://github.com/bluelinelabs/Conductor) Controller.
# Usage
Extend your Controller from RxController
```java
public class MainController extends RxController {
```

Create a Disposable
```java
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
```
Now if you want to dispose Disposable "d" on a specific event - e.g., onDestroyView
```java
this.dispose(d, ControllerEvent.DESTROY_VIEW);
```
or if you want to dispose Disposable "d" on a opposing lifecycle event - e.g., if subscribing during CREATE, it will terminate on DESTROY
```java
this.dispose(d);
```
# Installation
```script
maven { url "https://jitpack.io" }
```
and:
```script
dependencies {
    compile 'com.github.mahmed8003:RxController:0.0.2'
}
```