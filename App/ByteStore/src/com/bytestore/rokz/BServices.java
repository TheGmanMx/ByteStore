package com.bytestore.rokz;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BServices extends Service {

	private Timer updateTimer = new Timer();
	TimerTask tarea;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		user = intent.getStringExtra("user");
		Toast.makeText(this, "Se inicio servicio de ByteStore",
				Toast.LENGTH_LONG).show();
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		showNotification();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Bye! :( -ByteStore", Toast.LENGTH_LONG).show();

	}

	public class LocalBinder extends Binder {
		BServices getService() {
			return BServices.this;
		}
	}

	private NotificationManager mNM;
	private int NOTIFICATION = R.string.inicioBackService;
	public String user;
	boolean espera = false;
	Integer numAppEspera = 0;
	static Integer numEsperaAnterior = 0;

	private void showNotification() {

		tarea = new TimerTask() {

			private static final String NAMESPACE = "http://tempuri.org/";
			private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
			private static final String SOAP_ACTION = "http://tempuri.org/ListaEspera";
			private static final String METHOD_NAME = "ListaEspera";

			@Override
			public void run() {
				ArrayList<String> resultArreglo = new ArrayList<String>();

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				request.addProperty("user", user);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE ht = new HttpTransportSE(URL);

				try {
					ht.call(SOAP_ACTION, envelope);

					SoapObject result = (SoapObject) envelope.bodyIn;
					SoapObject result1 = (SoapObject) result.getProperty(0);
					numAppEspera = result1.getPropertyCount();

					for (int i = 0; i < numAppEspera; i++) {
						resultArreglo.add((String) result1.getProperty(i)
								.toString());
					}
					if (numAppEspera > 0) {
						if (numAppEspera != numEsperaAnterior) {
							numEsperaAnterior = numAppEspera;
							espera = true;
						}
					}

				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}

				if (espera) {
					ExisteEspera();
				}
			}
		};

		updateTimer.scheduleAtFixedRate(tarea, 0, 5000);

	}

	public void ExisteEspera() {

		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = "Se tiene " + numAppEspera + " apps en espera";

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.icon, text,
				System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, ListaDescarga.class), 0);

		notification.flags=Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this, "Descarga tus aplicaciones!",
				text, contentIntent);
		espera = false;
		mNM.notify(NOTIFICATION, notification);

	}

	private static final String TAG = "Background Service ByteStore";
}
