package com.bytestore.rokz;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
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
import org.ksoap2.serialization.SoapPrimitive;
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
				cargarCategoria(itemPosition);
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
			return true;
		case R.id.item2:
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

		Toast.makeText(getApplicationContext(),
				"Se carga lista de aplicaciones con ID de categoria: " + idCat,
				Toast.LENGTH_LONG).show();
		switch (idCat) {
		case 1:
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
					//elementosLista(BuscarWS());
					new Lista().execute();
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
	private static final String SOAP_ACTION = "http://tempuri.org/listaApps";
	private static final String METHOD_NAME = "listaApps";
/*
	public String[] BuscarWS() {

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAMEname);
		SoapObject requestId = new SoapObject(NAMESPACE, METHOD_NAMEid);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapSerializationEnvelope envelopeId = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(request);
		envelopeId.setOutputSoapObject(requestId);

		HttpTransportSE ht = new HttpTransportSE(URL);
		HttpTransportSE htId = new HttpTransportSE(URL);
		
		try {
			ht.call(SOAP_ACTIONname, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

			htId.call(SOAP_ACTIONid, envelopeId);
			SoapPrimitive responseId = (SoapPrimitive) envelopeId.getResponse();

			String result = response.toString();
			String resultId = responseId.toString();

			String[] listaresults = result.split(";;;");
			String[] listaResultsId = resultId.split(";;;");

			idAppsLista = Arrays.asList(listaResultsId);
			AppsLista = Arrays.asList(listaresults);

			return listaresults;
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
			System.out.println("Error" + e);
		}
		return null;
	}
	*/
	//ArrayList<String> lista = new ArrayList<String>();
	
	
	
	class Lista extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPostExecute(Object result) {

			ListView mlistView = (ListView) findViewById(R.id.listView1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					MainActivity.this, android.R.layout.simple_list_item_1,
					AppsLista);
			mlistView.setAdapter(adapter);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE ht = new HttpTransportSE(URL);

			try {
				ht.call(SOAP_ACTION, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject result1 = (SoapObject) result.getProperty(0);

				SoapObject resultId= (SoapObject)result1.getProperty(0);
				SoapObject resultApp= (SoapObject)result1.getProperty(1);
				
				for (int i = 0; i < resultId.getPropertyCount(); i++) {
					idAppsLista.add((String) resultId.getProperty(i).toString());
					AppsLista.add((String) resultApp.getProperty(i).toString());
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

			return null;
		}

	}
	
	
	
}
