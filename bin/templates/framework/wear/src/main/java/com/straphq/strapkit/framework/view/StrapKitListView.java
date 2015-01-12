package com.straphq.strapkit.framework.view;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.straphq.strapkit.framework.R;
import com.straphq.strapkit.framework.StrapKitBaseActivity;
import com.straphq.strapkit.strapkit_lib.messaging.StrapKitMessageService;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by martinza on 1/9/15.
 */
public class StrapKitListView extends StrapKitBaseView {

    private static final String TAG = StrapKitListView.class.getSimpleName();

    public WearableItem[] items;
    public String onItemClick;

    @Override
    public void initialize(final StrapKitBaseActivity activity) {

        Adapter adapter = new Adapter(activity, items);

        WearableListView listView = new WearableListView(activity);

        listView.setAdapter(adapter);

        if (onItemClick != null) {
            listView.setClickListener(new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(WearableItem.class, new Serializer()).create();
                    Integer tag = (Integer) viewHolder.itemView.getTag();
                    try {
                        if (onItemClick != null) {
                            MessageItem messageItem = new MessageItem();
                            messageItem.function = onItemClick;
                            Args args = new Args();
                            args.item = items[tag];
                            args.itemIndex = tag;
                            messageItem.args = args;

                            activity.sendMessage("/onItemClick", gson.toJson(messageItem));
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Failed", e);
                    }
                }

                @Override
                public void onTopEmptyRegionClick() {

                }
            });
        }

        ViewGroup viewGroup = (ViewGroup) activity.findViewById(R.id.frame_layout);
        viewGroup.addView(listView);


    }

    class MessageItem implements Serializable {
        public String function;
        public Args args;

    }

    class Args implements Serializable {
        public WearableItem item;
        public Integer itemIndex;
    }

    public static class WearableItem implements Serializable {
        public String title;
        public String subtitle;
        public String data;
    }

    private static final class Adapter extends WearableListView.Adapter {
        private WearableItem[] mDataset;
        private final Context mContext;
        private final LayoutInflater mInflater;

        // Provide a suitable constructor (depends on the kind of dataset)
        public Adapter(Context context, WearableItem[] dataset) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDataset = dataset;
        }

        // Provide a reference to the type of views you're using
        public static class ItemViewHolder extends WearableListView.ViewHolder {
            private TextView textView;
            public ItemViewHolder(View itemView) {
                super(itemView);
                // find the text view within the custom item's layout
                textView = (TextView) itemView.findViewById(R.id.base_text);
            }
        }

        // Create new views for list items
        // (invoked by the WearableListView's layout manager)
        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            // Inflate our custom layout for list items
            return new ItemViewHolder(mInflater.inflate(R.layout.wearable_list_item, null));
        }

        // Replace the contents of a list item
        // Instead of creating new views, the list tries to recycle existing ones
        // (invoked by the WearableListView's layout manager)
        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder,
                                     int position) {
            // retrieve the text view
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            TextView view = itemHolder.textView;
            // replace text contents
            String text = mDataset[position].title + "\n" + mDataset[position].subtitle;
            view.setText(text);
            // replace list item's metadata
            holder.itemView.setTag(position);
        }

        // Return the size of your dataset
        // (invoked by the WearableListView's layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }

    public static class Desirializer implements JsonDeserializer<WearableItem> {

        @Override
        public WearableItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                WearableItem item = new WearableItem();
                JsonObject jobject = (JsonObject) json;
                item.title = jobject.get("title").getAsString();
                item.subtitle = jobject.get("subtitle").getAsString();
                item.data = jobject.get("data").toString();
                return item;
            } catch (Exception e) {
                Log.d(TAG, "yep");
                return null;
            }
        }
    }

    public static class Serializer implements JsonSerializer<WearableItem> {

        @Override
        public JsonElement serialize(WearableItem src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json=new JsonObject();
            json.addProperty("title",src.title);
            json.addProperty("subtitle", src.subtitle);
            JsonParser parser = new JsonParser();
            json.add("data", parser.parse(src.data));
            return json;
        }
    }
}
