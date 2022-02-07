package com.fyts.bluetoothtool.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import com.fyts.bluetoothtool.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message: 工具类
 * Created by  ChenLong.
 * Created by Time on 2018/2/2.
 */

public class ToolUtils {
    /**
     * 正则表达式：验证密码
     */
//    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{8,20}$";
    private static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^1[3|4|5|7|8|9]\\d{9}";

    public static boolean isPhone(String is) {
        return Pattern.matches(REGEX_MOBILE, is);
    }

    /**
     * 正则表达式：验证邮编
     */
    private static final String REGEX_NUM = "^[0-9][0-9]{5}$";
    /**
     * 正则表达式：验证身份证
     * <p>
     * 地区：[1-9]\d{5}
     * 年的前两位：(18|19|([23]\d))            1800-2399
     * 年的后两位：\d{2}
     * 月份：((0[1-9])|(10|11|12))
     * 天数：(([0-2][1-9])|10|20|30|31)          闰年不能禁止29+
     * 三位顺序码：\d{3}
     * 两位顺序码：\d{2}
     * 校验码：[0-9Xx]
     * 十八位：^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$
     * 十五位：^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$
     */
    private static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

    public static boolean isCard(String is) {
        return Pattern.matches(REGEX_ID_CARD, is);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验邮编
     *
     * @param num
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isYouNum(String num) {
        return Pattern.compile(REGEX_NUM).matcher(num).matches();
    }

    /**
     * 纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // 只允许字母、数字和汉字
    public static String stringFilter(String str) {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 纯字母
     *
     * @param fstrData
     * @return
     */
    public static boolean isChar(String fstrData) {
        char c = fstrData.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

    //判断集合
    public static <T> boolean isList(List<T> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    //获取带默认的字符串
    public static String getString(String s, String def) {
        if (!TextUtils.isEmpty(s)) {
            return s;
        }
        return def;
    }

    public static String getString(String s) {
        return getString(s, "");
    }

    //隐藏部分手机号
    public static String showPhoneLen(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s.replace(s.substring(3, 7), "****");
        }
        return getString(s, "");
    }

    // 获取颜色值
    public static int showColor(Context context, int resId) {
        return context.getResources().getColor(resId);
    }

    // 获取背景
    public static Drawable showBackground(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    //图形验证码
    public static void showImgCode(Activity activity, ImageView view) {
        String time = "?time=" + System.currentTimeMillis();
        if (activity != null) {
//            Glide.with(activity).load("路径" + time).skipMemoryCache(true)   //验证码不缓存
//                    .diskCacheStrategy(DiskCacheStrategy.NONE).transform(new GlideCircleTransform(activity, 3)).into(view);
        }
    }

    //关闭软键盘
    public static void closeSoft(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    //打开软键盘
    public static void showSoft(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //消息数量限制
    public static String getToalNum(int num) {
        if (num > 99) {
            return "99+";
        }
        return num + "";
    }

    //改变字体的颜色
    public static SpannableStringBuilder setPosTextColor(String str, int start, int end, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        //str代表要显示的全部字符串
        //３代表从第几个字符开始变颜色，注意第一个字符序号是０．
        //８代表变色到第几个字符．
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 文字匹配变色
     * 附近企业
     */
    public static SpannableStringBuilder nearbyCompanySearchText(Context context, String text, String keyword) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(text);
        //条件 keyword
        Pattern pattern = Pattern.compile(keyword);
        //匹配
        Matcher matcher = pattern.matcher(new SpannableString(text.toLowerCase()));
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String[] split = text.split("\\|");
            //ForegroundColorSpan 需要new 不然也只能是部分变色
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color666666)), 0, split[0].length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_FD485E)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //返回变色处理的结果
        return spannableString;
    }

    //webview 的设置
    public static void settingWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        //设置支持缩放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public static String getDouble(double d) {
        String format = new DecimalFormat("#0.00").format(d);
        return format;
    }

    //字符串转double
    public static double getDoubleValue(String s) {
        double value = 0;
        if (TextUtils.isEmpty(s)) {
            return value;
        }
        try {
            value = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    //字符串转int
    public static int getIntValue(String s) {
        int value = 0;
        if (TextUtils.isEmpty(s)) {
            return value;
        }
        try {
            value = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    //字符串转Long
    public static long getLongValue(String s) {
        long value = 0;
        if (TextUtils.isEmpty(s)) {
            return value;
        }
        try {
            value = Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    //打电话
    public static void callPhone(Activity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        activity.startActivity(intent);
    }

    //处理图片查看
//    public static List<LocalMedia> getPicData(String s) {
//        List<LocalMedia> localMedias = new ArrayList<>();
//        LocalMedia localMedia = new LocalMedia();
//        localMedia.setPath(s);
//        localMedias.add(localMedia);
//        return localMedias;
//    }
    //去掉空格
    public static String getNoKongText(String text) {
        if (!TextUtils.isEmpty(text)) {
            String trim = text.trim();
            String s = trim.replaceAll(" ", "");
            String name1 = s.replaceAll("\\r", "");
            String name2 = name1.replaceAll("\\n", "");
            return name2;
        }
        return "";
    }

    //限制区间
    public static float getRangeValue(float value, float start, float end) {
        if (value <= start) {
            value = start;
        }
        if (value >= end) {
            value = end;
        }
        return value;
    }


    /**
     * 设置textView结尾...后面显示的文字和颜色
     *
     * @param context    上下文
     * @param textView   textview
     * @param minLines   最少的行数
     * @param originText 原文本
     * @param endText    结尾文字
     * @param endColorID 结尾文字颜色id
     * @param isExpand   当前是否是展开状态
     */
    public static void toggleEllipsize(final Context context, final TextView textView, final int minLines, final String originText,
                                       final String endText, final int endColorID, final boolean isExpand) {
        if (TextUtils.isEmpty(originText)) {
            return;
        }
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isExpand) {
                    textView.setText(originText);
                } else {
                    int paddingLeft = textView.getPaddingLeft();
                    int paddingRight = textView.getPaddingRight();
                    TextPaint paint = textView.getPaint();
                    float moreText = textView.getTextSize() * endText.length();
                    float availableTextWidth = (textView.getWidth() - paddingLeft - paddingRight) *
                            minLines - moreText;
                    CharSequence ellipsizeStr = TextUtils.ellipsize(originText, paint,
                            availableTextWidth, TextUtils.TruncateAt.END);
                    if (ellipsizeStr.length() < originText.length()) {
                        CharSequence temp = ellipsizeStr + endText;
                        SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                        ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor
                                        (endColorID)),
                                temp.length() - endText.length(), temp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        textView.setText(ssb);
                    } else {
                        textView.setText(originText);
                    }
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }


    //获取通话记录
    public static void getContentCallLog(Activity activity) {
        Cursor cursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
//                columns 数据库列选择 不选则获取所有列
                null
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        Log.e("=====", "cursor count:" + cursor.getCount());
        int i = 0;
        while (cursor.moveToNext() && i < 20) {
            i++;
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            String phone_account_address = cursor.getString(cursor.getColumnIndex("phone_account_address"));//本机号码可能获取不到（华为、oppo获取不到）
            String phone_account_id = cursor.getString(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));//本机sim卡id，即ICCID
//
            Log.e("=====", "Call log: " + "\n"
                    + "姓名: " + name + "\n"
                    + "通话日期: " + date + "\n"
                    + "电话号码: " + number + "\n"
                    + "通话时长: " + duration + "\n"
                    + "通话类型1.呼入2.呼出3.未接: " + type + "\n"
                    + "本机号码: " + phone_account_address + "\n"
                    + "本机sim卡id: " + phone_account_id + "\n"
            );
        }
    }
}
