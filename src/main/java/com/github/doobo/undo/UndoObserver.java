package com.github.doobo.undo;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 观察者处理对象
 */
public abstract class UndoObserver implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"undo".equals(evt.getPropertyName()) || !(evt.getNewValue() instanceof UndoVO)) {
            return;
        }
        UndoVO vo = (UndoVO) evt.getNewValue();
        if(matching(vo)){
            undoValue(vo);
        }
    }

    /**
     * 返回true才执行undoValue
     */
    public boolean matching(UndoVO vo){
        return Boolean.TRUE;
    }

    /**
     * 填充值
     * @param vo
     */
    public abstract void undoValue(UndoVO vo);
}
