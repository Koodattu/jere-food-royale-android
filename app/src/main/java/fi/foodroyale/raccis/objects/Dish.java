package fi.foodroyale.raccis.objects;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import fi.foodroyale.raccis.R;

/**
 * Created by Jupe Danger on 21.3.2018.
 */

public class Dish extends AbstractFlexibleItem<Dish.ViewHolder> {
    private String name;
    private String desc;
    private String price;

    public Dish(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Dish(String name, String price, String desc) {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof Dish) {
            Dish inItem = (Dish) inObject;
            return this.name.equals(inItem.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_course;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.title.setText(name);
        holder.price.setText(price);
        holder.props.setText(desc);
    }

    public class ViewHolder extends FlexibleViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.desc) TextView props;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
