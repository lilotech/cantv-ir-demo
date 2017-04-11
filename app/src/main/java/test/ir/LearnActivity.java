package test.ir;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lilosrv.ir.ConsumerIrDevice;
import com.lilosrv.ir.IrException;
import com.lilosrv.ir.IrProtocolEnum;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class LearnActivity extends AppCompatActivity {
    private static String TAG = LearnActivity.class.getSimpleName();
    @Bind(R.id.sendir)
    Button volupBt;
    @Bind(R.id.recev)
    Button voldownBt;
    @Bind(R.id.Button_up)
    ImageButton buttonup;
    @Bind(R.id.Button_down)
    ImageButton buttondown;
    @Bind(R.id.Button_left)
    ImageButton buttonleft;
    @Bind(R.id.Button_right)
    ImageButton buttonright;
    @Bind(R.id.Button_enter)
    ImageButton buttonenter;
    @Bind(R.id.boot)
    ImageButton btnboot;
    @Bind(R.id.home)
    ImageButton btnhome;
    @Bind(R.id.back)
    ImageButton btnback;
    @Bind(R.id.menu)
    ImageButton btnmenu;
    int[] volupData = new int[]{9088, 4437, 651, 1617, 652, 1615, 654, 480, 649, 484
            , 655, 1612, 657, 1611, 648, 486, 654, 1614, 655, 478, 651, 482
            , 647, 1620, 648, 1619, 650, 1618, 651, 1616, 653, 1615, 654, 479
            , 650, 1618, 651, 1616, 652, 1615, 654, 479, 649, 1618, 651, 482
            , 646, 487, 652, 1615, 654, 479, 650, 484, 656, 477, 652, 1615, 653
            , 480, 649, 1618, 651, 1616, 653, 480, 649
    };
    private String upcode = "B37CCA35", downcode = "B37CD22D", leftcode = "B37C9966",
            rightcode = "B37CC13E", volup = "B37C9768", voldown = "B37CCA35",
            okcode = "B37CCE31", backCode = "B37CC53A", channelUP = "80102658",bootcode="B37CDC23",
            homeCode="B37C8877",menuCode="B37C8976";
    private static final int LONG_KEY_RIGHT_CODE = 1;
    private static final int LONG_KEY_LEFT_CODE = 2;
    private static final int LONG_KEY_VOLUP_CODE = 3;


    private int[] learn = null;
    private String learnMangleCode;

    IrSender sender;
    /**
     * 长按发红外码
     * LONG_KEY_RIGHT_CODE 发送右键红外码
     * LONG_KEY_LEFT_CODE  发送左键红外码
     * LONG_KEY_VOLUP_CODE 发送音量+键红外码
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LONG_KEY_RIGHT_CODE:
                    sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, rightcode);
                    sendEmptyMessageDelayed(LONG_KEY_RIGHT_CODE, 100);
                    break;
                case LONG_KEY_LEFT_CODE:
                    sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, leftcode);
                    sendEmptyMessageDelayed(LONG_KEY_LEFT_CODE, 100);
                    break;
                case LONG_KEY_VOLUP_CODE:
                    sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, volup);
                    sendEmptyMessageDelayed(LONG_KEY_VOLUP_CODE, 100);
                    break;
            }
        }
    };


    ConsumerIrDevice irDevice = new ConsumerIrDevice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_control);
        ButterKnife.bind(this);
        sender = IrSender.getIrDriver();



        Log.i(TAG, "Check whether the device has an infrared emitter = " + irDevice.hasIrEmitter());
        Log.i(TAG, "Check whether the device has an infrared receiver = " + irDevice.hasIrReceiver());
//         an array of CarrierFrequencyRange objects representing the ranges that the
//         transmitter can support, or null if there was an error.
        ConsumerIrDevice.CarrierFrequencyRange[] ranges = irDevice.getIrReceiverCarrierFreqency();
        for (ConsumerIrDevice.CarrierFrequencyRange range : ranges) {
            Log.i(TAG, "Query the infrared receiver's supported carrier frequencies = " + range.getMaxFrequency() + "  getMinFrequency=" + range.getMinFrequency());
        }
//         an array of CarrierFrequencyRange objects representing the ranges that the
//         transmitter can support, or null if there was an error.
        ConsumerIrDevice.CarrierFrequencyRange[] ranges1 = irDevice.getCarrierFrequencies();
        for (ConsumerIrDevice.CarrierFrequencyRange carrierFrequencyRange : ranges1) {
            Log.i(TAG, "Query the infrared transmitter's supported carrier frequencies =" +
                    carrierFrequencyRange.getMaxFrequency() + "  getMinFrequency=" + carrierFrequencyRange.getMinFrequency());
        }

    }

    @OnClick(R.id.sendir)
    public void sendway() {
//        sender.sendCode(IrProtocolEnum.IR_Philips_RC6_M6_Long_Gehua,channelUP);
       // sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, upcode);
       // sender.sendPulse(38000, learn);

        if (learnMangleCode != null)
            sender.sendMangledCode(learnMangleCode);
        else if (learn != null)
            sender.sendPulse(38000, learn);
    }

    @OnLongClick(R.id.sendir)
    public boolean sendir() {
        //handler.sendEmptyMessage(LONG_KEY_VOLUP_CODE);
        return true;
    }


    @OnTouch(R.id.sendir)
    public boolean ontouchsendirButton(View v, MotionEvent event) {
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                break;
            case KeyEvent.ACTION_UP:
                handler.removeMessages(LONG_KEY_VOLUP_CODE);
                break;
        }
        return false;
    }

    @OnClick(R.id.recev)
    public void recway() {
       // sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, backcode);

        Log.d(TAG, "Start to learn....");

        try {
            irDevice.receive(new ConsumerIrDevice.IrReceiveListener() {
                @Override
                public void onIrResult(int[] ints, String s) {

                    Log.d(TAG, "Learned pulse: len=" + ints.length);
                    for (int i = 0; i < ints.length; i++) {
                        Log.d(TAG, "" + i + "     " + ints[i]);
                    }

                    learn = ints;

                    Log.d(TAG, "Learned manglecode: " + s);
                    learnMangleCode = s;
                }

                @Override
                public void onError(IrException e) {
                    Log.e(TAG, "", e);
                }
            });

        }catch (IrException e) {
            Log.e(TAG, "", e);
        }

    }

    @OnClick(R.id.Button_up)
    public void sendup() {

        /*以下三种发送方式等效*/
  //      sender.sendPulse(38000, learn);
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, "B37CCA35");
//        sender.sendMangledCode("f52584de741c1d89e1e1cfc6c05f882f");
//        sender.sendCode(IrProtocolEnum.IR_Philips_RC6_M6_Long_Gehua, channelUP);
    }

    @OnClick(R.id.Button_down)
    public void senddown() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, downcode);
    }

    @OnClick(R.id.Button_left)
    public void sendleft() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, leftcode);
    }

    @OnLongClick(R.id.Button_left)
    public boolean sendLongLeftKey() {
        handler.sendEmptyMessage(LONG_KEY_LEFT_CODE);
        return true;


    }

    @OnTouch(R.id.Button_left)
    public boolean ontouchLeftButton(View v, MotionEvent event) {
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                break;
            case KeyEvent.ACTION_UP:
                handler.removeMessages(LONG_KEY_LEFT_CODE);
                break;
        }
        return false;
    }

    @OnClick(R.id.Button_right)
    public void sendright() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, rightcode);
    }

    @OnLongClick(R.id.Button_right)
    public boolean sendLongRightKey() {
        handler.sendEmptyMessage(LONG_KEY_RIGHT_CODE);
        return true;
    }

    @OnTouch(R.id.Button_right)
    public boolean ontouchRightButton(View v, MotionEvent event) {
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                break;
            case KeyEvent.ACTION_UP:
                handler.removeMessages(LONG_KEY_RIGHT_CODE);
                break;
        }
        return false;
    }

    @OnClick(R.id.Button_enter)
    public void sendenter() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, okcode);
    }

    @OnClick(R.id.boot)
    public void boot(){
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, bootcode);
    }
    @OnClick(R.id.home)
    public void home(){
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, homeCode);
    }
    @OnClick(R.id.menu)
    public void menu(){
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, menuCode);
    }
    @OnClick(R.id.back)
    public void back(){
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, backCode);
    }
}
