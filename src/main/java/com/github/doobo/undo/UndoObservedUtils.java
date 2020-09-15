package com.github.doobo.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 被观察者
 */
@Component
public class UndoObservedUtils {
    
    private static UndoObserved INSTANCE;

    /**
     * 获取被观察者实例
     */
    public static UndoObserved getInstance(){
        if(INSTANCE == null) {
            throw new IllegalArgumentException("UndoObservedUtils is Undefined");
        }
        return INSTANCE;
    }

    @Autowired
    public void setObserved(UndoObserved observed) {
        INSTANCE = observed;
    }
}
