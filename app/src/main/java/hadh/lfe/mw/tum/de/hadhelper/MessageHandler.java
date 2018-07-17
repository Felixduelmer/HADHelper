package hadh.lfe.mw.tum.de.hadhelper;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MessageHandler extends Handler {

    public MessageHandler(Looper looper){
        super(looper);
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int startid = msg.arg1;
        Object object = msg.obj;

    }
}
