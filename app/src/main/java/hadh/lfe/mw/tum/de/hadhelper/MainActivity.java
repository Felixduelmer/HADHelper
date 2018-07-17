package hadh.lfe.mw.tum.de.hadhelper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {


    public ListenerService listenerService;
    private PowerManager.WakeLock mWakeLock;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setupServiceReceiver();
        onStartService();
        getWakeLock();


        // Zeigt Daten über das Handy an
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        Log.e("MyActivity", "manufacturer " + manufacturer
                + " \n model " + model
                + " \n version " + version
                + " \n versionRelease " + versionRelease
        );

    }


    // Starts the Service
    public void onStartService() {
        Intent i = new Intent(this, ListenerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(i);}
            else{startService(i);}
    }
/*
    // Setup the callback for when data is received from the service
    public void setupServiceReceiver() {
        listenerService = new ListenerService(new Handler());
        // This is where we specify what happens when data is received from the service
        listenerService.setReceiver(new MyTestReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    String resultValue = resultData.getString("resultValue");
                    Toast.makeText(MainActivity.this, resultValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void kickOffListenerService(){
        if (listenerService == null){
            Log.d(Tag, "start Silab ListenerService");
            mListenerService = new ListenerService(mSilabGuiHandler, this);
            mListenerService.start();
        }
    }
*/

    // Sorgt dafür das der Bildschirm nicht ausgeschaltet wird

    protected void getWakeLock(){
        try{
            PowerManager powerManger = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManger.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.FULL_WAKE_LOCK, "de.tum.ergonomie.hadh");
            mWakeLock.acquire();
        }catch(Exception e){
            Log.e(TAG,"get wakelock failed:"+ e.getMessage());
        }
    }

    //holt sich die IpAdresse des Handys



    public  String getIpAddress(){//helper
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getIpAddress() failed: " + e.getMessage());
        }
        return "---";
    }


}

