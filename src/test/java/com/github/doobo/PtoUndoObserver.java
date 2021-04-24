package com.github.doobo;

import com.github.doobo.undo.UndoObserver;
import com.github.doobo.undo.UndoVO;
import org.springframework.stereotype.Component;

@Component
public class PtoUndoObserver extends UndoObserver {

    /**
     * 返回True才执行undoValue
     */
    @Override
    public boolean matching(UndoVO vo) {
        return "card".equals(vo.getType()) || "reg".equals(vo.getType());
    }

    /**
     * 如果是基本类型的入参，并且参数为空，无内存地址，不替换内容
     * 继承观察者,可填充到方法的入参里面
     */
    @Override
    public void undoValue(UndoVO vo) {
        if (vo.getType().equals("card")) {
            vo.undo("...1");
        }
        if (vo.getType().equals("phone")) {
            vo.undo("......2");
        }
        if (vo.getType().equals("reg")) {
            vo.undo('.');
        }
        if(vo.getType().equals("string")){
            vo.undo("............4");
        }
        if(vo.getType().equals("obj")){
            vo.undo(new SingleObj().setAuthor("............5"));
        }
    }
}
