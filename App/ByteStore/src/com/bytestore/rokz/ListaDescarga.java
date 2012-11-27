package com.bytestore.rokz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ListaDescarga extends SherlockActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_lista_descarga);
		new Lista().execute("ListaEsperaPorId");
	}

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

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
	public List<String> idAppsLista = new ArrayList<String>();
	public List<String> AppsLista = new ArrayList<String>();
	public List<String> bannerAppsLista = new ArrayList<String>();

	LazyAdapter adapter;

	class Lista extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPostExecute(Object result) {
			// si el dialogo de progreso esta abierto, se elimina
			if (pd.isShowing()) {
				pd.dismiss();
			}
			// se crea instancia de lista
			ListView mlistView = (ListView) findViewById(R.id.listView1);
			adapter = new LazyAdapter(ListaDescarga.this, AppsLista,
					bannerAppsLista);
			mlistView.setAdapter(adapter);
			mlistView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getApplicationContext(), String.valueOf(id),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ListaDescarga.this,
							InfoApp.class);
					Collections.sort(idAppsLista);
					intent.putExtra("idApp", idAppsLista.get((int) id));
					startActivity(intent);
				}
			});
			super.onPostExecute(result);
		}

		private ProgressDialog pd = new ProgressDialog(ListaDescarga.this);

		@Override
		protected void onPreExecute() {
			// se prepara y se llama al dialogo de progreso
			this.pd.setMessage("Cargando");
			this.pd.show();
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			SoapObject request = new SoapObject(NAMESPACE, (String) arg0[0]);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			// Crea bundle que recibe los extras
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			// del bundle, saco el extra que ocupo
			ArrayList<String> ids = extras.getStringArrayList("ids");

			// Creo Objeto soap para crear "arreglo"
			SoapObject soapCompanies = new SoapObject(NAMESPACE, "ids");
			// le agrego los valores al arreglo
			for (String i : ids) {
				soapCompanies.addProperty("string", i);
			}
			// agrego el arreglo al request
			request.addSoapObject(soapCompanies);
			envelope.setOutputSoapObject(request);

			HttpTransportSE ht = new HttpTransportSE(URL);

			try {
				// hace llamada al WS
				ht.call(NAMESPACE + (String) arg0[0], envelope);
				// Recibe la respuesta
				SoapObject result = (SoapObject) envelope.bodyIn;
				// Recibe los arreglos
				SoapObject result1 = (SoapObject) result.getProperty(0);
				// Se separan los arreglons en dos
				SoapObject resultApp = (SoapObject) result1.getProperty(0);
				SoapObject resultBan = (SoapObject) result1.getProperty(1);

				idAppsLista.clear();
				AppsLista.clear();
				bannerAppsLista.clear();
				idAppsLista = ids;
				for (int i = 0; i < resultApp.getPropertyCount(); i++) {
					AppsLista.add((String) resultApp.getProperty(i).toString());
					bannerAppsLista.add((String) resultBan.getProperty(i)
							.toString());
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}

	}
}