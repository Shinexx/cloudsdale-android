package org.cloudsdale.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api_models.Message;
import org.cloudsdale.android.models.api_models.User;

import java.util.ArrayList;

public class CloudMessageAdapter extends BaseAdapter {

    private ArrayList<Message> mMessageArray;
    private Activity           mViewRoot;
    private LayoutInflater     mInflater;

    public CloudMessageAdapter(Activity activity, ArrayList<Message> messages) {
        this.mViewRoot = activity;
        this.mInflater = (LayoutInflater) this.mViewRoot
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (messages != null) {
            this.mMessageArray = messages;
        } else {
            clearMessages();
        }
    }

    public void addMessage(Message message) {
        this.mMessageArray.add(message);
        notifyDataSetChanged();
    }

    public void clearMessages() {
        mMessageArray = new ArrayList<Message>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mMessageArray.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mMessageArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = (Message) getItem(position);

        View messageView = convertView;

        if (messageView == null) {
            messageView = mInflater.inflate(R.layout.chat_message_view, null);
        }

        ImageView avatar = (ImageView) messageView
                .findViewById(R.id.message_user_avatar);
        TextView nameText = (TextView) messageView
                .findViewById(R.id.message_name_tag);
        TextView tagText = (TextView) messageView
                .findViewById(R.id.message_role_tag);
        TextView messageBody = (TextView) messageView
                .findViewById(R.id.message_body);

        User user = message.getAuthor();

        UrlImageViewHelper.setUrlDrawable(avatar, user.getAvatar().getNormal(),
                R.drawable.unknown_user, 60000 * 10);
         nameText.setText(message.getAuthor().getName());
         tagText.setText(message.getAuthor().getRole().toString());
         messageBody.setText(message.getContent());

        return messageView;
    }

}
