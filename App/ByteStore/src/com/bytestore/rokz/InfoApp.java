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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class InfoApp extends SherlockActivity {
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String SOAP_ACTION = "http://tempuri.org/InfoApp";
	private static final String METHOD_NAME = "InfoApp";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";

	TextView txtDesc;
	RatingBar rating;
	MenuItem itemMenu;

	ArrayList<String> ImagenesArray = new ArrayList<String>();
	ArrayList<String> resultArreglo = new ArrayList<String>();

	Gallery g;
	Sesion sesion = new Sesion();

	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Toast.makeText(getApplicationContext(),
				Boolean.valueOf(sesion.SesionActiva()).toString(),
				Toast.LENGTH_SHORT).show();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.item1:
			if (sesion.SesionActiva()) {
				Toast.makeText(this, sesion.terminarSesion(), Toast.LENGTH_LONG)
						.show();
				item.setTitle("Iniciar sesion");
			} else {
				Intent intentLog = new Intent(this, LoginActivity.class);
				startActivityForResult(intentLog, 0);
				itemMenu = item;
			}
			return true;
		case R.id.item3:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				itemMenu.setTitle(data.getStringExtra("result"));
			}
		}
	}

	private class Informacion extends AsyncTask<String, Integer, Void> {

		private ProgressDialog pd = new ProgressDialog(InfoApp.this);

		@Override
		protected void onPreExecute() {

			txtDesc = (TextView) findViewById(R.id.textView1);
			rating = (RatingBar) findViewById(R.id.ratingBar1);
			// se prepara y se llama al dialogo de progreso
			this.pd.setMessage("Cargando");
			this.pd.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			setTitle(resultArreglo.get(0));
			txtDesc.setText(resultArreglo.get(2));
			Integer estrellas = Integer.valueOf(resultArreglo.get(3));
			rating.setRating(estrellas);

			if (!ImagenesArray.isEmpty()) {
				g = (Gallery) findViewById(R.id.gallery1);
				// g.setSpacing(1);
				g.setAdapter(new ImageAdapter(InfoApp.this));
			}
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(String... params) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			String idApp = getIntent().getStringExtra("idApp").toString();
			request.addProperty("idApp", idApp);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE ht = new HttpTransportSE(URL);
			try {
				ht.call(SOAP_ACTION, envelope);

				resultArreglo.clear();
				ImagenesArray.clear();

				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject result1 = (SoapObject) result.getProperty(0);
				SoapObject resultA1 = (SoapObject) result1.getProperty(0);
				SoapObject resultA2 = (SoapObject) result1.getProperty(1);
				for (int i = 0; i < resultA1.getPropertyCount(); i++) {
					resultArreglo.add((String) resultA1.getProperty(i)
							.toString());
				}
				for (int i = 0; i < resultA2.getPropertyCount(); i++) {
					ImagenesArray.add((String) resultA2.getProperty(i)
							.toString());
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

		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		itemMenu = menu.findItem(R.id.item1);
		if (sesion.SesionActiva()) {
			itemMenu.setTitle("Salir de sesion");
		} else {
			itemMenu.setTitle("Iniciar sesion");
		}

		return true;
	}

	public void downloadApp(View view) {
		try {
			if (sesion.SesionActiva()) {
				final String result = resultArreglo.get(1);
				new LoadViewTask().execute(result);
			} else {
				iniciarSesion();
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
			System.out.println("Error" + e);
		}
	}

	public void iniciarSesion() {
		AlertDialog ad = new AlertDialog.Builder(InfoApp.this)
				.setMessage(
						"Aun no haz iniciado sesion. A continuacion ingresa tus datos")
				.setPositiveButton("Ok", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(InfoApp.this,
								LoginActivity.class);
						startActivityForResult(intent, 0);
					}
				}).create();
		ad.show();
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

		@Override
		protected void onProgressUpdate(Integer... values) {
			pd.setProgress(values[0]);
		}

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
						"Acepta la instalacion a continuacion.",
						Toast.LENGTH_LONG).show();
				rutaarchivo = "";
			}
		}

	}

	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return ImagenesArray.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			try {
				// arreglar esto
				if (!ImagenesArray.isEmpty()) {
					i.setImageBitmap(getBitmapFromURL(ImagenesArray
							.get(position)));
					i.setScaleType(ImageView.ScaleType.FIT_XY);
					i.setLayoutParams(new Gallery.LayoutParams(250, 250));
					return i;
				} else {
					i.setImageResource(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

		public Bitmap getBitmapFromURL(String src) {
			try {
				Log.e("src", src);
				URL url = new URL(src);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				Log.e("Bitmap", "returned");
				return myBitmap;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("Exception", e.getMessage());
				return null;
			}
		}

		private Context mContext;

	}

}
