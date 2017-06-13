package test.ir;

import com.lilosrv.ir.IrProtocolEnum;

/**
 * Created by 411370845 on 2017/3/23.
 */

public class IrCoderData {
    private IrProtocolEnum anEnum;
    private boolean Config;

    public IrProtocolEnum getAnEnum() {
        return anEnum;
    }

    public void setAnEnum(IrProtocolEnum anEnum) {
        this.anEnum = anEnum;
    }

    public boolean isConfig() {
        return Config;
    }

    public void setConfig(boolean config) {
        Config = config;
    }
}
