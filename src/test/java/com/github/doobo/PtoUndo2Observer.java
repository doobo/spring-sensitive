package com.github.doobo;

import com.github.doobo.undo.UndoObserver;
import com.github.doobo.undo.UndoVO;
import org.springframework.stereotype.Component;

@Component
public class PtoUndo2Observer extends UndoObserver {
    
    @Override
    public boolean matching(UndoVO vo) {
        return "phone".equals(vo.getType()) || "string".equals(vo.getType());
    }

    /**
     * 继承观察者,可填充到方法的入参里面
     * @param vo
     */
    @Override
    public void undoValue(UndoVO vo) {
        vo.undo("......2");
        if("string".equals(vo.getType())){
            vo.undo("............4");
        }
    }
}
