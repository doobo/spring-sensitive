package com.github.doobo;

import com.github.doobo.undo.UndoObserver;
import com.github.doobo.undo.UndoVO;
import org.springframework.stereotype.Component;

@Component
public class PtoUndoObserver extends UndoObserver {

    /**
     * 继承观察者,可填充到方法的入参里面
     * @param vo
     */
    @Override
    public boolean undoValue(UndoVO vo) {
        synchronized (this) {
            if (vo.getType().equals("card")) {
                return vo.undo("...1");
            }
            if (vo.getType().equals("phone")) {
                return vo.undo("......2");
            }
            if (vo.getType().equals("reg")) {
                return vo.undo(".........3");
            }
            return false;
        }
    }
}
