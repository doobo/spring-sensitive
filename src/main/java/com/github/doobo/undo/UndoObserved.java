package com.github.doobo.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 * 被观察者
 */
@Component
public class UndoObserved extends Observable {
    
    private static boolean IS_OBSERVE;

    /*观察者列表*/
    private List<Observer> observerList;

    @PostConstruct
    public void observerRegister() {
        if(observerList != null && !observerList.isEmpty()) {
            observerList.stream().filter(f->f instanceof UndoObserver).forEach(this::addObserver);
            if(observerList.stream().anyMatch(f -> f instanceof UndoObserver)){
                observerList = observerList.stream().filter(f->f instanceof UndoObserver).collect(Collectors.toList());
                IS_OBSERVE = true;
            }
        }
    }

    /**
     * 广播信息
     * @param vo
     */
    public synchronized void sendResult(UndoVO vo){
        this.setChanged();
        this.notifyObservers(vo);
    }

    /**
     * 是否有观察者
     */
    public static boolean isObserver(){
        return IS_OBSERVE;
    }

    /**
     * 获取所有注册的观察者
     */
    public List<Observer> getObserverList() {
        return observerList;
    }

    @Autowired(required = false)
    public void setObserverList(List<Observer> observerList) {
        this.observerList = observerList;
    }
}
