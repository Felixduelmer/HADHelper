package hadh.lfe.mw.tum.de.hadhelper;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ListenerService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private static final int SERVICE_ID = 4;
    private static final String TAG = "MyService";
    Thread listenerthread;
    Boolean isConnected = false;

    // Key for the string that's delivered in the action's intent.
    private static final String KEY_TEXT_REPLY = "Shutdown Service";

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                try {

                    int port = 58453;

                    // Create a socket to listen on the port.
                    DatagramSocket dsocket = new DatagramSocket(port);

                    // Create a buffer to read datagrams into. If a
                    // packet is larger than this buffer, the
                    // excess will simply be discarded!
                    byte[] buffer = new byte[2048];

                    // Create a packet to receive data into the buffer
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    if (dsocket.isBound()) {

                        // Now loop forever, waiting to receive packets and printing them.
                        while (true) {
                            // Wait to receive a datagram
                            dsocket.receive(packet);

                            // Convert the contents to a string, and display them
                            String message = new String(buffer, 0, packet.getLength());
                            System.out.println(packet.getAddress().getHostName() + ": "
                                    + message);

                            // Reset the length of the packet before reusing it.
                            packet.setLength(buffer.length);
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }finally {
                Log.d(TAG, "Klappt nicht");

            }
        }
    }
    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        HandlerThread thread = new HandlerThread("ListenerService", Process.THREAD_PRIORITY_FOREGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        createNotificationChannel();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) { // Hat vielleicht keinen Intent wenn der Service beendet und neu gestartet wurde (STICKY_SERVICE)
            Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

            // For each start request, send a message to start a job and deliver the
            // start ID so we know which request we're stopping when we finish the job
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);

        }


        createNotificationChannel(); //ist ab android Oreo Vorraussetzung, dass Notifications angezeigt werden (für nähere Infos: https://developer.android.com/training/notify-user/build-notification)

        // Create an explicit intent for an Activity in your app
        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent stoppingintent = new Intent(this, ListenerService.class);
        stoppingintent.


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Erster_Channel")
                .setSmallIcon(R.drawable.ic_directions_car_green_24dp)
                .setContentTitle("Service Running")
                .setContentText("Background Service for UDP")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_background, KEY_TEXT_REPLY, null)
                .setAutoCancel(false);
        final Notification notification = mBuilder.build();



        startForeground(SERVICE_ID, notification);

        return Service.START_STICKY; //Sorgt dafür, dass der Service immer wieder neu gestartet wird und nicht zerstört werden kann
    }

    //Default Methode, welche bei einem Service erstellt wird. Falls eine Komponente an den Service gebunden werden soll
    // in diesem Fall wird es nicht benutzt --> return null
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public  void stopService (){
        stopSelf();

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String CHANNEL_ID = "Erster_Channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager == null) throw new AssertionError();
            notificationManager.createNotificationChannel(channel);
        }
    }
}