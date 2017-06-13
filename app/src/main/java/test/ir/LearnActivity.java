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
import android.widget.Toast;

import com.lilosrv.ir.ConsumerIrDevice;
import com.lilosrv.ir.IrException;
import com.lilosrv.ir.IrProtocolEnum;

import java.util.ArrayList;

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
    private IrManager irManager;
    /**
     * 长按发红外码
     * LONG_KEY_RIGHT_CODE 发送右键红外码
     * LONG_KEY_LEFT_CODE  发送左键红外码
     * LONG_KEY_VOLUP_CODE 发送音量+键红外码
     * LEARN_IR_CODE       学习红外码，更新列表
     * INIT_ID_CODE        初始化支持的红外码
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LONG_KEY_RIGHT_CODE:
                    irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, rightcode);
                    sendEmptyMessageDelayed(LONG_KEY_RIGHT_CODE, 100);
                    break;
                case LONG_KEY_LEFT_CODE:
                    irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, leftcode);
                    sendEmptyMessageDelayed(LONG_KEY_LEFT_CODE, 100);
                    break;
                case LONG_KEY_VOLUP_CODE:
                    irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, volup);
                    sendEmptyMessageDelayed(LONG_KEY_VOLUP_CODE, 100);
                    break;
                case LEARN_IR_CODE:
                    IrCoderAdapter adapter = new IrCoderAdapter(learnIrObjects);
                    irRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case INIT_ID_CODE:
                    IrCoderDataAdapter dataAdapter = new IrCoderDataAdapter();
                    irRecyclerView.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_control);
        ButterKnife.bind(this);
        irManager = IrManager.getIrDriver();
        irRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        irRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        Log.i(TAG, "Check whether the device has an infrared emitter = " + irManager.isIrEmitter());
        Log.i(TAG, "Check whether the device has an infrared receiver = " + irManager.isIrReceiver());
//         an array of CarrierFrequencyRange objects representing the ranges that the
//         transmitter can support, or null if there was an error.
        ConsumerIrDevice.CarrierFrequencyRange[] ranges = irManager.getIrReceiverCarrierFrequency();
        for (ConsumerIrDevice.CarrierFrequencyRange range : ranges) {
            Log.i(TAG, "Query the infrared receiver's supported carrier frequencies = " + range.getMaxFrequency() + "  getMinFrequency=" + range.getMinFrequency());
        }
//         an array of CarrierFrequencyRange objects representing the ranges that the
//         transmitter can support, or null if there was an error.
        ConsumerIrDevice.CarrierFrequencyRange[] ranges1 = irManager.getCarrierFrequencies();
        for (ConsumerIrDevice.CarrierFrequencyRange carrierFrequencyRange : ranges1) {
            Log.i(TAG, "Query the infrared transmitter's supported carrier frequencies =" +
                    carrierFrequencyRange.getMaxFrequency() + "  getMinFrequency=" + carrierFrequencyRange.getMinFrequency());
        }
        irManager.addOnReceiveIrListener(new IrReceiveInterface() {
            @Override
            public void onReceive(LearnIrObject learnIrObject) {
                learnIrObjects.add(learnIrObject);
                handler.sendEmptyMessage(LEARN_IR_CODE);
            }

            @Override
            public void onError(IrException e) {
                e.printStackTrace();
                if (e.getErrorCode() == IrException.IRRESULT_RECEIVER_BUSY) {
                    Toast.makeText(getBaseContext(), "正在学习红外码中...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(INIT_ID_CODE);
    }

    @OnClick(R.id.recev)
    public void recway() {
        Log.d(TAG, "Start to learn....");
        irManager.startLearn();
        handler.sendEmptyMessage(LEARN_IR_CODE);
    }

    @OnClick(R.id.irlist)
    public void irList() {
        handler.sendEmptyMessage(INIT_ID_CODE);
    }

    @OnClick(R.id.Button_up)
    public void sendup() {

        /*以下三种发送方式等效*/
        //      irManager.sendPulse(38000, learn);
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, "B37CCA35");
//        irManager.sendMangledCode("f52584de741c1d89e1e1cfc6c05f882f");
//        irManager.sendCode(IrProtocolEnum.IR_Philips_RC6_M6_Long_Gehua, channelUP);
    }

    @OnClick(R.id.Button_down)
    public void senddown() {
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, downcode);
    }

    @OnClick(R.id.Button_left)
    public void sendleft() {
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, leftcode);
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
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, rightcode);
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
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, okcode);
    }

    @OnClick(R.id.boot)
    public void boot() {
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, bootcode);
    }

    @OnClick(R.id.home)
    public void home() {
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, homeCode);
    }

    @OnClick(R.id.menu)
    public void menu() {
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, menuCode);
    }

    @OnClick(R.id.back)
    public void back() {
        irManager.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, backCode);
    }

}
