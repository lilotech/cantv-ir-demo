package test.ir;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;

import com.lilosrv.ir.IrProtocolEnum;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    int[] volupData = new int[]{9088, 4437, 651, 1617, 652, 1615, 654, 480, 649, 484
            , 655, 1612, 657, 1611, 648, 486, 654, 1614, 655, 478, 651, 482
            , 647, 1620, 648, 1619, 650, 1618, 651, 1616, 653, 1615, 654, 479
            , 650, 1618, 651, 1616, 652, 1615, 654, 479, 649, 1618, 651, 482
            , 646, 487, 652, 1615, 654, 479, 650, 484, 656, 477, 652, 1615, 653
            , 480, 649, 1618, 651, 1616, 653, 480, 649
    };
    String upcode = "B37CCA35", downcode = "B37CD22D", leftcode = "B37C9966",
            rightcode = "B37CC13E", volup = "B37C9768", okcode = "B37CCE31",
            backcode = "B37CC53A", channelUP = "80102658";

    //    IrDriver irDriver;
    IrSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_control);
        ButterKnife.bind(this);
        sender = IrSender.getIrDriver();
    }

    @OnClick(R.id.sendir)
    public void sendway() {
//        sender.sendCode(IrProtocolEnum.IR_Philips_RC6_M6_Long_Gehua,channelUP);
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, upcode);
    }

    @OnClick(R.id.recev)
    public void recway() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, backcode);
    }

    @OnClick(R.id.Button_up)
    public void sendup() {

        /*以下三种发送方式等效*/
//        sender.sendPulse(38000, volupData);
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, "B37CCA35");
//        sender.sendMangledCode("f52584de741c1d89e1e1cfc6c05f882f");
    }

    @OnClick(R.id.Button_down)
    public void senddown() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, downcode);
    }

    @OnClick(R.id.Button_left)
    public void sendleft() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, leftcode);
    }

    @OnClick(R.id.Button_right)
    public void sendright() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, rightcode);
    }

    @OnClick(R.id.Button_enter)
    public void sendenter() {
        sender.sendCode(IrProtocolEnum.IR_uPD6121G_NEC, okcode);
    }
}
