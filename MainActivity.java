package com.example.patrick.service_client;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mmjoshi.binderservice.IMyAidlInterface;


public class MainActivity extends Activity implements View.OnClickListener {

    EditText etValue1, etValue2;
    Button bAdd;
    ServiceConnection AddServiceConnection;
    protected IMyAidlInterface iMyAidlInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etValue1 = (EditText)findViewById(R.id.txtinput);
        bAdd = (Button)findViewById(R.id.btnCalc);
        bAdd.setOnClickListener(this);
        initConnection();
    }

    void initConnection() {
        AddServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                iMyAidlInterface = IMyAidlInterface.Stub.asInterface((IBinder) service);
                Toast.makeText(getApplicationContext(),
                        "Service Connected", Toast.LENGTH_SHORT)
                        .show();
                Log.d("IRemote", "Binding is done - Service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                iMyAidlInterface = null;
                Toast.makeText(getApplicationContext(), "Service Disconnected",
                        Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding - Service disconnected");


            }
        };
        if (iMyAidlInterface == null) {   //     TROCAR ESSES TRECHOS DE CÓDIGO AKI !!!!!!!!!!!!!!!!!!!!!!!
            Intent it = new Intent("com.example.patrick.binderservice.MyService");
            it.setPackage("com.example.patrick");
            it.setAction("com.example.patrick.binderservice.MyService.IMyAidlInterface");
            it.setComponent(new ComponentName("com.example.patrick", "com.example.patrick.binderservice.MyService"));
            // binding to remote service
            startService(it);
            Log.d("Service-MJ","Service is going to start");
            bindService(it, AddServiceConnection, Service.BIND_AUTO_CREATE);
            Log.d("Service-MJ", "Service is started");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v)
    {
        int num = Integer.parseInt(etValue1.getText().toString());
        Toast.makeText(getApplicationContext(), "num is " + num,
                Toast.LENGTH_LONG).show();
        int result = 0;
        try {
            result = iMyAidlInterface.findFactorialService(num);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Result" + result,Toast.LENGTH_SHORT).show();
    }


}
