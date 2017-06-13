package test.ir;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.lilosrv.ir.ConsumerIrDevice;
import com.lilosrv.ir.IrException;
import com.lilosrv.ir.IrProtocolEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 411370845 on 2017/2/21.
 */

public class IrManager {

    private static final String TAG = IrManager.class.getSimpleName();

    /**
     * IR_PULSE  发送 int[]
     * IR_EXPLICIT_CODE 发送 string
     * IR_MANGLED_CODE 发送 解码方式  string
     * IR_LEARN_CODE  学习红外码，学习期间不能发送红外码
     */

    private static final int IR_PULSE = 1;
    private static final int IR_EXPLICIT_CODE = 2;
    private static final int IR_MANGLED_CODE = 3;
    private static final int IR_LEARN_CODE = 4;

    private static ConsumerIrDevice irDevice;
    private static IrManager irDriver;
    private static IrHandler irHandler;
    private boolean irEmitter, irReceiver;
    private ConsumerIrDevice.CarrierFrequencyRange[] irReceiverCarrierFrequency, carrierFrequencies;
    private static int states;


    public static IrManager getIrDriver() {
        if (irDriver == null) {
            synchronized (IrManager.class) {
                if (irDriver == null || irHandler == null) {
                    irDriver = new IrManager();
                }
            }
        }
        return irDriver;
    }

    private IrManager() {
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
        irEmitter = irDevice.hasIrEmitter();
        irReceiver = irDevice.hasIrReceiver();
        irReceiverCarrierFrequency = irDevice.getIrReceiverCarrierFreqency();
        carrierFrequencies = irDevice.getCarrierFrequencies();
    }

    private static class IrHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (states == IR_LEARN_CODE ) {
                for (Map.Entry<IrReceiveInterface, Object> entry : irReceiveConcurrentHashMap.entrySet()) {
                    entry.getKey().onError(new IrException(-7));
                }
                return;
            }
            switch (msg.what) {
                case IR_PULSE:
                    int[] data = (int[]) msg.obj;
                    Log.i(TAG, "freq=" + msg.arg1 + "data.length = " + data.length);
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
                    } catch (IrException e) {
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
                case IR_LEARN_CODE:
                    states = IR_LEARN_CODE;
                    try {
                        irDevice.receive(new ConsumerIrDevice.IrReceiveListener() {
                            @Override
                            public void onIrResult(int[] ints, String s) {

                                Log.d(TAG, "Learned pulse: len=" + ints.length);
                                LearnIrObject irObject = new LearnIrObject();
                                irObject.setLearnMangleCode(s);
                                irObject.setLearn(ints);
                                for (int i = 0; i < ints.length; i++) {
                                    Log.d(TAG, "" + i + "     " + ints[i]);
                                }
                                for (Map.Entry<IrReceiveInterface, Object> entry : irReceiveConcurrentHashMap.entrySet()) {
                                    entry.getKey().onReceive(irObject);
                                }
                                Log.d(TAG, "Learned manglecode: " + s);
                                states = 0;
                            }

                            @Override
                            public void onError(IrException e) {
                                for (Map.Entry<IrReceiveInterface, Object> entry : irReceiveConcurrentHashMap.entrySet()) {
                                    entry.getKey().onError(e);
                                }
                                Log.e(TAG, "", e);
                            }
                        });

                    } catch (IrException e) {
                        Log.e(TAG, "", e);
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

    public void startLearn() {
        irHandler.sendEmptyMessage(IR_LEARN_CODE);
    }

    private static ConcurrentHashMap<IrReceiveInterface, Object> irReceiveConcurrentHashMap =
            new ConcurrentHashMap<>();


    public void addOnReceiveIrListener(IrReceiveInterface listener) {
        if (!irReceiveConcurrentHashMap.containsKey(listener)) {
            irReceiveConcurrentHashMap.put(listener, 1);
        }
    }

    public void removeOnReceiveIrListener(IrReceiveInterface listener) {
        if (irReceiveConcurrentHashMap.containsKey(listener)) {
            irReceiveConcurrentHashMap.remove(listener);
        }
    }

    public boolean isIrReceiver() {
        return irReceiver;
    }

    public boolean isIrEmitter() {
        return irEmitter;
    }

    public ConsumerIrDevice.CarrierFrequencyRange[] getCarrierFrequencies() {
        return carrierFrequencies;
    }

    public ConsumerIrDevice.CarrierFrequencyRange[] getIrReceiverCarrierFrequency() {
        return irReceiverCarrierFrequency;
    }
}
