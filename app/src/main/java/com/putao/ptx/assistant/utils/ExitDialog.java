package com.putao.ptx.assistant.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class ExitDialog extends Dialog {

    public ExitDialog(final Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = width;
        params.height = height;
        window.setAttributes(params);
        new AdapterFlowView(
                context,
                new Function0<TextView>() {
                    @Override
                    public TextView invoke() {
                        return new TextView
                                (context);
                    }
                },
                new Function2<TextView, Boolean, Unit>() {
                    @Override
                    public Unit invoke(TextView textView, Boolean aBoolean) {
                        return Unit.INSTANCE;
                    }
                },
                new Function0<kotlin.Pair<Integer, Integer>>() {
                    @Override
                    public kotlin.Pair<Integer, Integer> invoke() {
                        return null;
                    }
                },
                new Function1<List<String>, Unit>() {
                    @Override
                    public Unit invoke(List<String> strings) {
                        return null;
                    }
                });
    }
}