package test.ir;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 411370845 on 2016/6/6.
 */

public class IrCoderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LearnIrObject> data;
    private IrSender sender;

    public IrCoderAdapter(List<LearnIrObject> items) {
        data = items;
        sender = IrSender.getIrDriver();
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
        mholder.name.setText(position+":"+data.get(position).getLearnMangleCode());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView2)
        public TextView name;

        @OnClick(R.id.textView2)
        void onclick() {
            int postion = (int) name.getTag();
            LearnIrObject irObject = data.get(postion);
            if (TextUtils.isEmpty(irObject.getLearnMangleCode())) {
                sender.sendPulse(38000, irObject.getLearn());
            } else {
                sender.sendMangledCode(irObject.getLearnMangleCode());

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
