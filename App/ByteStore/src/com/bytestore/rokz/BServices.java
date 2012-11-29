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
		// Toast.makeText(this, "Se inicio servicio de ByteStore",
		// Toast.LENGTH_LONG).show();
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
	ArrayList<String> resultArreglo = new ArrayList<String>();
	Sesion sesion = new Sesion();

	private void showNotification() {

		// Se crea la tarea a realizar, que en este caso
		// checa si existe una app en espera para el usuario
		tarea = new TimerTask() {
			// variables del WS
			private static final String NAMESPACE = "http://tempuri.org/";
			private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
			private static final String SOAP_ACTION = "http://tempuri.org/ListaEspera";
			private static final String METHOD_NAME = "ListaEspera";

			@Override
			public void run() {
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

		// Se realiza la tarea cada 5 segundos
		updateTimer.scheduleAtFixedRate(tarea, 0, 5000);

	}

	public void ExisteEspera() {

		// Mensaje de cuantas aplicaciones se tiene en espera
		CharSequence text = "Se tiene " + numAppEspera
				+ " apps en espera para descargar";

		// Se crea la instancia de la notificacion
		Notification notification = new Notification(R.drawable.icon, text,
				System.currentTimeMillis());

		// El PendingIntent nos dice que actividad abrir cuando se le de click
		// a la notificacion
		Intent intent = new Intent(BServices.this, ListaDescarga.class);
		intent.putStringArrayListExtra("ids", resultArreglo);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Se cancela o se cierra la notificacion despues de dar click
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		// Mensaje
		notification.setLatestEventInfo(this, "Descarga tus aplicaciones!",
				text, contentIntent);
		// Ya no volvera a abrirse hasta que cambie la cantidad de aplicaciones
		// en espera
		espera = false;
		mNM.notify(NOTIFICATION, notification);

	}

	// a fuerzas me pedia un TAG asi que le di este
	private static final String TAG = "Background Service ByteStore";
}
