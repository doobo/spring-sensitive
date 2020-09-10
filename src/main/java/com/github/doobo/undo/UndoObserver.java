package com.github.doobo.undo;


import java.util.Observable;
import java.util.Observer;

/**
 * 观察者处理对象
 */
public abstract class UndoObserver implements Observer {
    
    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof UndoObserved) || !(arg instanceof UndoVO)){
            return;
        }
        undoValue((UndoVO) arg);
    }

    /**
     * 填充值
     * @param vo
     */
    public abstract boolean undoValue(UndoVO vo);
}
