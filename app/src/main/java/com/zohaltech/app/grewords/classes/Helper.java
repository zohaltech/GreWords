package com.zohaltech.app.grewords.classes;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.zohaltech.app.grewords.R;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import widgets.MyToast;


public final class Helper {
    
    public static Operator getOperator(Context context) {
        Operator operator = Operator.NO_SIM;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simOperatorName = tm.getSimOperatorName().toUpperCase();
            if (simOperatorName.toUpperCase().compareTo("IR-MCI") == 0 || simOperatorName.compareTo("IR-TCI") == 0) {
                operator = Operator.MCI;
            } else if (simOperatorName.toUpperCase().compareTo("RIGHTEL") == 0) {
                operator = Operator.RIGHTELL;
            } else if (simOperatorName.toUpperCase().compareTo("IRANCELL") == 0) {
                operator = Operator.IRANCELL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return operator;
    }
    
    //public static String getCurrentDateTime() {
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
    //    Date date = new Date();
    //    return dateFormat.format(date);
    //}
    
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    //public static String addDay(int day) {
    //    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    //    Calendar cal = Calendar.getInstance();
    //    cal.add(Calendar.DATE, day);
    //    return dateFormat.format(cal.getTime());
    //
    //}
    
    //public static Date getDateTime(String dateTime) {
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
    //    Date date = null;
    //    try {
    //        date = dateFormat.parse(dateTime);
    //    } catch (ParseException e) {
    //        e.printStackTrace();
    //    }
    //    return date;
    //}
    
    //public static Date getDate(String dateTime) {
    //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    //    Date date = null;
    //    try {
    //        date = dateFormat.parse(dateTime);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return date;
    //}
    
    //public static BigDecimal round(float d, int decimalPlace) {
    //    BigDecimal bd = new BigDecimal(Float.toString(d));
    //    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    //    return bd;
    //}
    
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    
    public static void goToWebsite(Activity activity, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }
    
    //public static void playSound() {
    //    try {
    //        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    //        Ringtone r = RingtoneManager.getRingtone(App.context, notification);
    //        r.play();
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}
    
    //public static void vibrate() {
    //    Vibrator vibrator = (Vibrator) App.context.getSystemService(Context.VIBRATOR_SERVICE);
    //    vibrator.vibrate(500);
    //}
    
    //public static int indexOf(Object o, ArrayList<Object> elementData) {
    //    if (o == null) {
    //        for (int i = 0; i < elementData.size(); i++)
    //            if (elementData.get(i) == null)
    //                return i;
    //    } else {
    //        for (int i = 0; i < elementData.size(); i++)
    //            if (o.equals(elementData.get(i)))
    //                return i;
    //    }
    //    return -1;
    //}
    
    //public static String hashString(String plaintext) {
    //    try {
    //        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
    //        byte[] array = md.digest(plaintext.getBytes());
    //        StringBuilder sb = new StringBuilder();
    //        for (byte anArray : array) {
    //            sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
    //        }
    //        return sb.toString();
    //    } catch (java.security.NoSuchAlgorithmException e) {
    //        e.printStackTrace();
    //    }
    //    return null;
    //}
    
    //public static String inputStreamToString(InputStream inputStream) {
    //    StringBuilder out = new StringBuilder();
    //    try {
    //        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
    //        for (String line = br.readLine(); line != null; line = br.readLine()) {
    //            out.append(line);
    //            out.append("\n");
    //        }
    //        br.close();
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //    return out.toString();
    //}
    
    public static void rateApp(Activity activity) {
        App.preferences.edit().putBoolean("RATED", true).apply();
        Intent intent = new Intent(App.marketPollIntent);
        intent.setData(Uri.parse(App.marketPollUri));
        intent.setPackage(App.marketPackage);
        if (!myStartActivity(activity, intent)) {
            intent.setData(Uri.parse(App.marketWebsiteUri));
            if (!myStartActivity(activity, intent)) {
                MyToast.show(activity, String.format(activity.getString(R.string.could_not_open_market), App.marketName), Toast.LENGTH_SHORT);
            }
        }
    }
    
    private static boolean myStartActivity(Activity activity, Intent intent) {
        try {
            activity.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static void openApk(Context context, File file) {
        try {
            Uri fileUri = Uri.fromFile(file);
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (Exception e) {
            // nothing
        }
    }
    
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddress = InetAddress.getByName("google.com");
            return ipAddress != null && !ipAddress.getHostAddress().equals("");
        } catch (Exception e) {
            return false;
        }
    }
}
