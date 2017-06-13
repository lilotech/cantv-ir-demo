package test.ir;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilosrv.ir.IrProtocolEnum;

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
    private IrSender sender;
    private HashMap<IrProtocolEnum, String> codes;

    public IrCoderDataAdapter(List<IrCoderData> items, HashMap<IrProtocolEnum, String> codes) {
        data = items;
        sender = IrSender.getIrDriver();
        this.codes = codes;
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
        mholder.name.setText(position+1 + ":" + data.get(position).getAnEnum().name());
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
}
