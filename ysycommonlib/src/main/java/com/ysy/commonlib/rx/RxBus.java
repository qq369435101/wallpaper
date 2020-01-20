package com.ysy.commonlib.rx;

import com.ysy.commonlib.base.Event;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public final class RxBus {

    private final Subject<Event> mSubject;

    public RxBus() {
        this(PublishSubject.create());
    }

    public RxBus(Subject<Event> subject) {
        this.mSubject = subject;
    }

    public <E extends Event> void post(E event) {
        mSubject.onNext(event);
    }

    public Observable<Event> observe() {
        return mSubject;
    }

    public <E extends Event> Observable<E> observeEvents(Class<E> eventClass) {
        return mSubject.ofType(eventClass);
    }

    private static final class RxBusHolder {
        private static final RxBus RX_BUS = new RxBus();
    }

    public static RxBus getDefault() {
        return RxBusHolder.RX_BUS;
    }

}
