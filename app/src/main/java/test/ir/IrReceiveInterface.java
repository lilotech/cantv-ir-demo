package test.ir;

import com.lilosrv.ir.IrException;

public interface IrReceiveInterface {

    void onReceive(LearnIrObject learnIrObject);

    void onError(IrException e);
}
