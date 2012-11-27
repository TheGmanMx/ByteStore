package com.bytestore.rokz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class InfoApp extends SherlockActivity {
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_info_app);
		try {
			if (InetAddress.getByName("192.168.1.4").isReachable(50)) {
				new Informacion().execute();
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
		} catch (IOException e) {
			Toast.makeText(
					getApplicationContext(),
					"No se logro conectar al servidor. Verifica tu conexion al internet.",
					Toast.LENGTH_LONG).show();
		}
	}

	TextView txtViewName;
	TextView txtDesc;
	RatingBar rating;

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
	private static final String SOAP_ACTION = "http://tempuri.org/InfoApp";
	private static final String METHOD_NAME = "InfoApp";
	ArrayList<String> resultArreglo = new ArrayList<String>();

	private class Informacion extends AsyncTask<String, Integer, Void> {

		private ProgressDialog pd = new ProgressDialog(InfoApp.this);
		@Override
		protected void onPreExecute() {

			txtViewName = (TextView) findViewById(R.id.txtViewName);
			txtDesc = (TextView) findViewById(R.id.textView1);
			rating = (RatingBar) findViewById(R.id.ratingBar1);
			// se prepara y se llama al dialogo de progreso
			this.pd.setMessage("Cargando");
			this.pd.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {

			txtViewName.setText(resultArreglo.get(0));
			txtDesc.setText(resultArreglo.get(2));
			Integer estrellas = Integer.valueOf(resultArreglo.get(3));
			rating.setRating(estrellas);
			
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(String... params) {

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

				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject result1 = (SoapObject) result.getProperty(0);
				for (int i = 0; i < result1.getPropertyCount(); i++) {
					resultArreglo.add((String) result1.getProperty(i).toString());
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
				System.out.println("Error" + e);
			}
			return null;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_info_app, menu);
		return true;
	}

	private ProgressDialog pd;

	public void downloadApp(View view) {
		try {
			final String result = resultArreglo.get(1);
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
			pd.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();

			if (rutaarchivo != null && rutaarchivo != "") {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(rutaarchivo)),
						"application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				// startActivityForResult(intent,1);
				Toast.makeText(getApplicationContext(),
						"Aplicacion instalada: " + rutaarchivo,
						Toast.LENGTH_LONG).show();
				rutaarchivo = "";
			}
		}

	}
}
