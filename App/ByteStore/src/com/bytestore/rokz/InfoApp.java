package com.bytestore.rokz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

public class InfoApp extends SherlockActivity {
	String[] actions = new String[] { "Top 10", "Sistema", "Oficina", "Juegos" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_app);
		try {
			if (InetAddress.getByName("192.168.1.4").isReachable(25)) {
				cargarDetalles();
			} else {
				Toast.makeText(
						getApplicationContext(),
						"No se logro conectar al servidor. Intenda de nuevo mas tarde.",
						Toast.LENGTH_LONG).show();
			}
		} catch (UnknownHostException e) {
			Toast.makeText(
					getApplicationContext(),
					"No se logro conectar al servidor. Verifica tu conexion al internet.",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					getApplicationContext(),
					"No se logro conectar al servidor. Verifica tu conexion al internet.",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	TextView txtViewName;
	TextView txtDesc;
	RatingBar rating;
	String nom = "appName", desc = "appDesc", rate = "appRate";

	public void cargarDetalles() {

		txtViewName = (TextView) findViewById(R.id.txtViewName);
		txtDesc = (TextView) findViewById(R.id.textView1);
		rating = (RatingBar) findViewById(R.id.ratingBar1);

		SoapObject request = new SoapObject(NAMESPACE, nom);
		SoapObject request2 = new SoapObject(NAMESPACE, desc);
		SoapObject request3 = new SoapObject(NAMESPACE, rate);

		request.addProperty("idApp", getIntent().getStringExtra("idApp")
				.toString());
		request2.addProperty("idApp", getIntent().getStringExtra("idApp")
				.toString());
		request3.addProperty("idApp", getIntent().getStringExtra("idApp")
				.toString());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope2.dotNet = true;
		envelope2.setOutputSoapObject(request2);

		SoapSerializationEnvelope envelope3 = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope3.dotNet = true;
		envelope3.setOutputSoapObject(request3);

		HttpTransportSE ht = new HttpTransportSE(URL);
		HttpTransportSE ht2 = new HttpTransportSE(URL);
		HttpTransportSE ht3 = new HttpTransportSE(URL);

		try {

			ht.call(NAMESPACE + nom, envelope);
			ht2.call(NAMESPACE + desc, envelope2);
			ht3.call(NAMESPACE + rate, envelope3);

			SoapPrimitive so = (SoapPrimitive) envelope.getResponse();
			SoapPrimitive so2 = (SoapPrimitive) envelope2.getResponse();
			SoapPrimitive so3 = (SoapPrimitive) envelope3.getResponse();

			String result = so.toString();
			txtViewName.setText(result);

			result = so2.toString();
			txtDesc.setText(result);

			result = so3.toString();
			Integer estrellas = Integer.valueOf(result);
			rating.setRating(estrellas);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
			System.out.println("Error" + e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_info_app, menu);
		return true;
	}

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
	private static final String SOAP_ACTION = "http://tempuri.org/downUrl";
	private static final String METHOD_NAME = "downUrl";

	private ProgressDialog pd;

	public void downloadApp(View view) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("idApp", getIntent().getStringExtra("idApp")
				.toString());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(URL);

		try {
			ht.call(SOAP_ACTION, envelope);
			SoapPrimitive so = (SoapPrimitive) envelope.getResponse();
			final String result = so.toString();

			new LoadViewTask().execute(result);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
			System.out.println("Error" + e);
		}
	}

	public void getKey(View view) {
		Toast.makeText(getApplicationContext(),
				getIntent().getStringExtra("idApp").toString(),
				Toast.LENGTH_LONG).show();
	}

	public void goHome(View view) {
		Intent refresh = new Intent(this, MainActivity.class);
		this.finish();
		startActivity(refresh);
	}

	private class LoadViewTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(InfoApp.this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setTitle("Descargando archivo...");
			pd.setMessage("Espera un momento mientras tu aplicacion se descarga...");
			pd.setCancelable(false);
			pd.setIndeterminate(false);
			pd.setMax(100);
			pd.setProgress(0);
			pd.show();
		}

		@Override
		protected Void doInBackground(String... apkurl) {
			try {
				String urlString = apkurl[0];
				URL url = new URL(urlString);
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();
				String PATH = Environment.getExternalStorageDirectory()
						+ "/AppStore/rokz/";
				File file = new File(PATH);
				file.mkdirs();
				File outputFile = new File(file, "app.apk");
				FileOutputStream fos = new FileOutputStream(outputFile);
				InputStream is = c.getInputStream();
				byte[] buffer = new byte[1024];
				int len1 = 0;
				int lenghtOfFile = c.getContentLength();
				long total = 0;
				while ((len1 = is.read(buffer)) != -1) {
					total += len1;
					pd.setProgress((int) ((total * 100) / lenghtOfFile));
					fos.write(buffer, 0, len1);
				}
				fos.close();
				is.close();
				rutaarchivo = PATH + "app.apk";

			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Error al descargar",
						Toast.LENGTH_LONG).show();
			}
			return null;
		}

		private String rutaarchivo;

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values) {
			// set the current progress of the progress dialog
			pd.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			pd.dismiss();

			if (rutaarchivo != null && rutaarchivo != "") {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(rutaarchivo)),
						"application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);	
				//startActivityForResult(intent,1);
				Toast.makeText(getApplicationContext(),
						"Aplicacion instalada: " + rutaarchivo, Toast.LENGTH_LONG)
						.show();
				rutaarchivo = "";
			}
			// initialize the View
			// setContentView(R.layout.main);
		}

	}
}
