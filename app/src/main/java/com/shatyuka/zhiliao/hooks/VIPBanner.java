package com.shatyuka.zhiliao.hooks;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shatyuka.zhiliao.Helper;
import com.shatyuka.zhiliao.R;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class VIPBanner implements IHook {
    static Class<?> VipEntranceView;

    @Override
    public String getName() {
        return "隐藏会员卡片";
    }

    @Override
    public void init(ClassLoader classLoader) throws Throwable {
        VipEntranceView = classLoader.loadClass("com.zhihu.android.app.ui.fragment.more.more.widget.VipEntranceView");
    }

    @Override
    public void hook() throws Throwable {
        if (Helper.prefs.getBoolean("switch_mainswitch", false) && Helper.prefs.getBoolean("switch_vipbanner", false)) {
            XposedHelpers.findAndHookMethod(VipEntranceView, "a", Context.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) {
                    XmlResourceParser layout_vipentranceview = Helper.modRes.getLayout(R.layout.layout_vipentranceview);
                    LayoutInflater.from((Context) param.args[0]).inflate(layout_vipentranceview, (ViewGroup) param.thisObject);
                    return null;
                }
            });
            for (Method method : VipEntranceView.getMethods()) {
                if (method.getName().equals("setData")) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(null));
                    break;
                }
            }
            XposedHelpers.findAndHookMethod(VipEntranceView, "onClick", View.class, XC_MethodReplacement.returnConstant(null));
            XposedHelpers.findAndHookMethod(VipEntranceView, "resetStyle", XC_MethodReplacement.returnConstant(null));
        }
    }
}
