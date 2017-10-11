package devta.scoratechsample.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moxun.tagcloudlib.view.TagsAdapter;

import devta.scoratechsample.R;

/**
 * @author Divyanshu Tayal
 */
public class SkillTagAdapter extends TagsAdapter {

    private String[] tags;

    public SkillTagAdapter(String[] tags) {
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return tags.length;
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(getItem(position));
        tv.setGravity(Gravity.CENTER);
        tv.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_login_button));
        tv.setPadding(8,8,8,8);
        tv.setTextColor(Color.WHITE);
        return tv;
    }

    @Override
    public String getItem(int position) {
        return tags[position];
    }

    @Override
    public int getPopularity(int position) {
        return position%7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {
        view.setBackgroundColor(themeColor);
    }


}
