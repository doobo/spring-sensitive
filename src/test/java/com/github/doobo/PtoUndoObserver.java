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
    public void undoValue(UndoVO vo) {
        synchronized (this) {
            if (vo.getType().equals("card")) {
                vo.undo("...1");
            }
            if (vo.getType().equals("phone")) {
                vo.undo("......2");
            }
            if (vo.getType().equals("reg")) {
                vo.undo(".........3");
            }
            if(vo.getType().equals("string")){
                vo.undo(4);
            }
        }
    }
}
