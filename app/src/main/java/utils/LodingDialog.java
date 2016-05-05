package utils;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.jlu.edu.csmla.R;

/**
 * Created by zhengheming on 2016/4/1.
 */
public class LodingDialog extends Dialog {
    public Context context;
    public LodingDialog(Context context) {
        super(context, R.style.DialogStyle);
        this.context=context;
    }

    private LodingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void create() {
        super.create();
        setContentView(R.layout.loding);
        ImageView image = (ImageView) findViewById(R.id.image);

        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        image.startAnimation(operatingAnim);
    }
}
