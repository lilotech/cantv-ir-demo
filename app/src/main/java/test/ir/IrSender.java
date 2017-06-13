package test.ir;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.lilosrv.ir.ConsumerIrDevice;
import com.lilosrv.ir.IrException;
import com.lilosrv.ir.IrProtocolEnum;

/**
 * Created by 411370845 on 2017/2/21.
 */

public class IrSender {

    private static final String TAG = IrSender.class.getSimpleName();

    /**
     * IR_PULSE  发送 int[]
     * IR_EXPLICIT_CODE 发送 string
     * IR_MANGLED_CODE 发送 解码方式  string
     */
    
    private static final int IR_PULSE = 1;
    private static final int IR_EXPLICIT_CODE = 2;
    private static final int IR_MANGLED_CODE = 3;

    private static ConsumerIrDevice irDevice;
    private static IrSender irDriver;
    private static IrHandler irHandler;


    public static IrSender getIrDriver() {
        if (irDriver == null) {
            synchronized (IrSender.class) {
                if (irDriver == null||irHandler==null) {
                    irDriver = new IrSender();
                }
            }
        }
        return irDriver;
    }

    private IrSender() {
        Thread t = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                irHandler = new IrHandler();
                Looper.loop();
            }
        };
        t.setName("ir");
        t.start();
        irDevice = new ConsumerIrDevice();
    }

    private static class IrHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IR_PULSE:
                    int[] data = (int[]) msg.obj;
                    Log.i(TAG, "freq=" + msg.arg1 + "data.length=" + data.length);
                    try {
                        irDevice.sendPulse(msg.arg1, data);
                    } catch (IrException e) {
                        e.printStackTrace();
                    }
                    break;
                case IR_EXPLICIT_CODE:
                    String code = (String) msg.obj;
                    Log.i(TAG, "" + code);
                    try {
                        irDevice.sendMangledCode(code);
                    }catch (IrException e) {
                        e.printStackTrace();
                    }
                    break;
                case IR_MANGLED_CODE:
                    String code2 = (String) msg.obj;
                    IrProtocolEnum prot = IrProtocolEnum.fromOrdinal(msg.arg1);
                    Log.i(TAG, msg.arg1 + "~~~" + code2);
                    if (prot != null) {
                        try {
                            irDevice.sendCode(prot, code2);
                        } catch (IrException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "prot=null");
                    }
                    break;
            }
        }
    }

    public void sendPulse(int freq, int[] data) {
        Message msg = Message.obtain();
        msg.what = IR_PULSE;
        msg.obj = data;
        msg.arg1 = freq;
        Log.i(TAG, (irDriver == null) + "  sss->" + (irHandler == null));
        irHandler.sendMessage(msg);
    }

    public void sendMangledCode(String code) {
        Message msg = Message.obtain();
        msg.what = IR_EXPLICIT_CODE;
        msg.obj = code;
        irHandler.sendMessage(msg);
    }

    public void sendCode(IrProtocolEnum irProtocolEnum, String code) {

        Log.i(TAG, code);
        Message msg = Message.obtain();
        msg.what = IR_MANGLED_CODE;
        msg.obj = code;
        msg.arg1 = irProtocolEnum.ordinal();
        irHandler.sendMessage(msg);
    }

}
