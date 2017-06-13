package test.ir;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilosrv.ir.IrProtocolEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 411370845 on 2016/6/6.
 */

public class IrCoderDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IrCoderData> data;
    private IrManager sender;
    private HashMap<IrProtocolEnum, String> codes;

    public IrCoderDataAdapter() {
        initIrcodes();
        sender = IrManager.getIrDriver();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mholder = (MyViewHolder) holder;
        mholder.itemView.setTag(position);
        mholder.name.setTag(position);
        mholder.name.setText(position + 1 + ":" + data.get(position).getAnEnum().name());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView2)
        public TextView name;

        @OnClick(R.id.textView2)
        void onclick() {
            int postion = (int) name.getTag();
            String code = codes.get(data.get(postion).getAnEnum());
            if (!TextUtils.isEmpty(code)) {
                sender.sendCode(data.get(postion).getAnEnum(), code);
            }

        }

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void initIrcodes() {
        data = new ArrayList<>();
        codes = new HashMap<>();
        codes.put(IrProtocolEnum.IR_uPD6121G_NEC, "B37CCA35");
        codes.put(IrProtocolEnum.IR_Philips_RC6_M6_Long_Gehua, "80102658");
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
                data.add(irCodeData);
        }

    }
}
