package com.github.doobo.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 被观察者
 */
@Component
public class UndoObserved {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    private static boolean IS_OBSERVE;

    /*观察者列表*/
    private List<PropertyChangeListener> observerList;

    @PostConstruct
    public void observerRegister() {
        if(observerList != null && !observerList.isEmpty()) {
            observerList.stream().filter(f->f instanceof UndoObserver).forEach(this::addPropertyChangeListener);
            if(observerList.stream().anyMatch(f -> f instanceof UndoObserver)){
                observerList = observerList.stream().filter(f->f instanceof UndoObserver).collect(Collectors.toList());
                IS_OBSERVE = true;
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /**
     * 广播信息
     * @param vo
     */
    public synchronized void sendResult(UndoVO vo){
        support.firePropertyChange("undo", null, vo);
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
    public List<PropertyChangeListener> getObserverList() {
        return observerList;
    }

    @Autowired(required = false)
    public void setObserverList(List<PropertyChangeListener> observerList) {
        this.observerList = observerList;
    }
}
