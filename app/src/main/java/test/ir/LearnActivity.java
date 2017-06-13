package test.ir;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.lilosrv.ir.ConsumerIrDevice;
import com.lilosrv.ir.IrException;
import com.lilosrv.ir.IrProtocolEnum;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class LearnActivity extends AppCompatActivity {
    private static String TAG = LearnActivity.class.getSimpleName();
    @Bind(R.id.irlist)
    Button irListButton;
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
    @Bind(R.id.learn)
    RecyclerView irRecyclerView;
    int[] volupData = new int[]{9088, 4437, 651, 1617, 652, 1615, 654, 480, 649, 484
            , 655, 1612, 657, 1611, 648, 486, 654, 1614, 655, 478, 651, 482
            , 647, 1620, 648, 1619, 650, 1618, 651, 1616, 653, 1615, 654, 479
            , 650, 1618, 651, 1616, 652, 1615, 654, 479, 649, 1618, 651, 482
            , 646, 487, 652, 1615, 654, 479, 650, 484, 656, 477, 652, 1615, 653
            , 480, 649, 1618, 651, 1616, 653, 480, 649
    };
    private String upcode = "B37CCA35", downcode = "B37CD22D", leftcode = "B37C9966",
            rightcode = "B37CC13E", volup = "B37C9768", voldown = "B37CCA35",
            okcode = "B37CCE31", backCode = "B37CC53A", channelUP = "80102658", bootcode = "B37CDC23",
            homeCode = "B37C8877", menuCode = "B37C8976";
    private static final int LONG_KEY_RIGHT_CODE = 1;
    private static final int LONG_KEY_LEFT_CODE = 2;
    private static final int LONG_KEY_VOLUP_CODE = 3;
    private static final int LEARN_IR_CODE = 4;
    private static final int INIT_ID_CODE = 5;
    private ArrayList<LearnIrObject> learnIrObjects = new ArrayList<>();
    private ArrayList<IrCoderData> irCodes = new ArrayList<>();
    private HashMap<IrProtocolEnum, String> codes = new HashMap<>();
    private IrSender sender;
    /**
     * 长按发红外码
     * LONG_KEY_RIGHT_CODE 发送右键红外码
     * LONG_KEY_LEFT_CODE  发送左键红外码
     * LONG_KEY_VOLUP_CODE 发送音量+键红外码
     * LEARN_IR_CODE       学习红外码，更新列表
     * INIT_ID_CODE        支持的红外码初始化
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
                case LEARN_IR_CODE:
                    IrCoderAdapter adapter = new IrCoderAdapter(learnIrObjects);
                    irRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case INIT_ID_CODE:
                    IrCoderDataAdapter dataAdapter = new IrCoderDataAdapter(irCodes, codes);
                    irRecyclerView.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
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
        irRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        irRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        initIrcodes();
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

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(INIT_ID_CODE);
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
                    LearnIrObject irObject = new LearnIrObject();
                    irObject.setLearnMangleCode(s);
                    irObject.setLearn(ints);
                    for (int i = 0; i < ints.length; i++) {
                        Log.d(TAG, "" + i + "     " + ints[i]);
                    }
                    learnIrObjects.add(irObject);
                    Log.d(TAG, "Learned manglecode: " + s);
                    handler.sendEmptyMessage(LEARN_IR_CODE);
                }

                @Override
                public void onError(IrException e) {
                    Log.e(TAG, "", e);
                }
            });

        } catch (IrException e) {
            Log.e(TAG, "", e);
        }

    }

    @OnClick(R.id.irlist)
    public void irList() {
        handler.sendEmptyMessage(INIT_ID_CODE);
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
    public void boot() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, bootcode);
    }

    @OnClick(R.id.home)
    public void home() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, homeCode);
    }

    @OnClick(R.id.menu)
    public void menu() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, menuCode);
    }

    @OnClick(R.id.back)
    public void back() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, backCode);
    }

    private void initIrcodes() {

        codes.put(IrProtocolEnum.IR_uPD6121G_NEC, "B37CCA35");
        codes.put(IrProtocolEnum.IR_Philips_RC6_M6_Long_Gehua, channelUP);
        codes.put(IrProtocolEnum.IR_DVB_Pan_7051_SAMSUNG, "484E5958562B");
        codes.put(IrProtocolEnum.IR_PROT_PANASONIC, "022080003DBD");
        codes.put(IrProtocolEnum.IR_RCA_56K, "F590A6");
        codes.put(IrProtocolEnum.IR_SAA3010_Philips_RC5, "1610");
        codes.put(IrProtocolEnum.IR_DVB_40BIT, "47104144EF");
        codes.put(IrProtocolEnum.IR_M50560, "221C");
        codes.put(IrProtocolEnum.IR_Topway_HDDVB, "535A4C54B3");
        codes.put(IrProtocolEnum.IR_Thomson_RCT100, "132B");
        codes.put(IrProtocolEnum.IR_Huizhou, "001001");
        codes.put(IrProtocolEnum.IR_Thomson_RCT311, "C1D");
        codes.put(IrProtocolEnum.IR_Konka_KK_Y261, "120B");
        codes.put(IrProtocolEnum.IR_TC9012, "18180BF41");
        codes.put(IrProtocolEnum.IR_TC9012F, "070702FD");
        codes.put(IrProtocolEnum.IR_LC7461M_C13, "01171EEB1CE3");
        codes.put(IrProtocolEnum.IR_LC7464M_Panasonic, "022080003DBD");
        codes.put(IrProtocolEnum.IR_Motorola2, "D46");
        codes.put(IrProtocolEnum.IR_GD_2000, "0A06");
        codes.put(IrProtocolEnum.IR_MN6014A_W_C6D6, "3F01003E");
        codes.put(IrProtocolEnum.IR_uPD6124_D7C8, "15B7");
        codes.put(IrProtocolEnum.IR_SAA3010P, "121A");
        codes.put(IrProtocolEnum.IR_PPM4_32BIT, "10000809");
        codes.put(IrProtocolEnum.IR_Sharp_IX0773CE, "0128001D73");
        codes.put(IrProtocolEnum.IR_Philips_RC6_3_20bits, "1600000C");
        codes.put(IrProtocolEnum.IR_AD6200_HY, "00F8");
        codes.put(IrProtocolEnum.IR_M3004_6C_LAB1, "51E");
        codes.put(IrProtocolEnum.IR_M50462, "6E02");
        codes.put(IrProtocolEnum.IR_LC7461M_C26D16, "012A1F8400FF");
        codes.put(IrProtocolEnum.IR_LC7464M_Panasonic, "022080003DBD");
        codes.put(IrProtocolEnum.IR_LC7461M_C13, "01171EEB1CE3");
        codes.put(IrProtocolEnum.IR_CUSTOM_6BIT, "02");
        codes.put(IrProtocolEnum.IR_ASTRO1_56KHz, "72FF70");  //error
        codes.put(IrProtocolEnum.IR_Philips_RC6_M0, "000C");
        codes.put(IrProtocolEnum.IR_Philips_RC6_M6_Long, "0000000C");
        codes.put(IrProtocolEnum.IR_Motorola, "79770F");
        codes.put(IrProtocolEnum.IR_PHILIPS_RC_MM_24bits, "0A0008");
        codes.put(IrProtocolEnum.IR_PHILIPS_RC_MM_32bits, "26706620");


        IrCoderData irCodeData;
        for (int i = 1; i < IrProtocolEnum.values().length; i++) {
            irCodeData = new IrCoderData();
            boolean config = true;
            switch (i) {
                case 10:
                    config = false;
                    break;
                case 12:
                    config = false;
                    break;
                case 19:
                    config = false;
                    break;
                case 20:
                    config = false;
                    break;
                case 21:
                    config = false;
                    break;
                case 28:
                    config = false;
                    break;
                case 29:
                    config = false;
                    break;
                case 34:
                    config = false;
                    break;
                case 35:
                    config = false;
                    break;
                case 36:
                    config = false;
                    break;
                case 40:
                    config = false;
                    break;
                case 41:
                    config = false;
                    break;
                case 42:
                    config = false;
                    break;
                case 46:
                    config = false;
                    break;
                case 47:
                    config = false;
                    break;
                case 48:
                    config = false;
                    break;
            }
            irCodeData.setAnEnum(IrProtocolEnum.fromOrdinal(i));
            irCodeData.setConfig(config);
            if (config)
                irCodes.add(irCodeData);
        }

    }
}
