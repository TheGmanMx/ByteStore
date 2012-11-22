package com.bytestore.rokz;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

	String[] actions = new String[] { "Top 10", "Instalados", "Sistema",
			"Oficina", "Juegos" };

	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);

		super.onCreate(savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.sherlock_spinner_item, actions);

		getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST);
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				cargarCategoria(0);
				return false;
			}
		};

		getSupportActionBar().setListNavigationCallbacks(adapter,
				navigationListener);
		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
	}

	public void elementosLista(String[] lista) {
		ListView mlistView = (ListView) findViewById(R.id.listView1);

		mlistView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lista));

		mlistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String s = ((TextView) view).getText().toString();
				if (AppsLista.contains(s)) {
					Intent intent = new Intent(MainActivity.this, InfoApp.class);
					intent.putExtra("idApp", idAppsLista.get(Integer
							.valueOf(AppsLista.indexOf(String.valueOf(s)))));
					startActivity(intent);
				}
			}
		});
	}

	public List<String> idAppsLista = new ArrayList<String>();
	public List<String> AppsLista = new ArrayList<String>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			// Instalados
			return true;
		case R.id.item2:
			// TopApps
			return true;
		case R.id.item3:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void goHome(View view) {
		finish();
		startActivity(getIntent());
	}

	public void cargarCategoria(Integer idCat) {
		switch (idCat) {
		case 1:
			// temporal
			elementosLista(new String[] { "Lista de", "Aplicaciones",
					"Instaladas" });
			break;
		case 2:
			elementosLista(new String[] { "Lista de", "Aplicaciones de",
					"Sistema" });
			break;
		case 3:
			elementosLista(new String[] { "Lista de", "Aplicaciones de",
					"Oficina" });
			break;
		case 4:
			elementosLista(new String[] { "Lista de", "Aplicaciones de",
					"Juegos" });
			break;
		default:
			try {
				if (InetAddress.getByName("192.168.1.4").isReachable(100)) {
					// se llama a ejecutar hilo que carga la lista. se debe
					// enviar el nombre del metodo en el WS
					new Lista().execute("listaApps");
				} else {
					Toast.makeText(
							getApplicationContext(),
							"No se logro conectar al servidor o no se tiene conexion al internet. Intenta mas tarde.",
							Toast.LENGTH_LONG).show();
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";

	class Lista extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPostExecute(Object result) {
			// si el dialogo de progreso esta abierto, se elimina
			if (pd.isShowing()) {
				pd.dismiss();
			}
			// se crea instancia de lista
			ListView mlistView = (ListView) findViewById(R.id.listView1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					MainActivity.this, android.R.layout.simple_list_item_1,
					AppsLista);
			mlistView.setAdapter(adapter);

			mlistView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String s = ((TextView) view).getText().toString();
					if (AppsLista.contains(s)) {
						Intent intent = new Intent(MainActivity.this,
								InfoApp.class);
						intent.putExtra("idApp", idAppsLista.get(Integer
								.valueOf(AppsLista.indexOf(String.valueOf(s)))));
						startActivity(intent);
					}
				}
			});

			super.onPostExecute(result);
		}

		private ProgressDialog pd = new ProgressDialog(MainActivity.this);

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
			envelope.setOutputSoapObject(request);

			HttpTransportSE ht = new HttpTransportSE(URL);

			try {
				ht.call(NAMESPACE + (String) arg0[0], envelope);
				// Recibe la respuesta
				SoapObject result = (SoapObject) envelope.bodyIn;
				// Recibe los arreglos
				SoapObject result1 = (SoapObject) result.getProperty(0);
				// Se separan los arreglons en dos
				SoapObject resultId = (SoapObject) result1.getProperty(0);
				SoapObject resultApp = (SoapObject) result1.getProperty(1);

				// Se limpia las listas
				idAppsLista.clear();
				AppsLista.clear();
				for (int i = 0; i < resultId.getPropertyCount(); i++) {
					idAppsLista
							.add((String) resultId.getProperty(i).toString());
					AppsLista.add((String) resultApp.getProperty(i).toString());
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
