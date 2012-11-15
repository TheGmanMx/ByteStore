package com.bytestore.rokz;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
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

public class MainActivity extends SherlockActivity {

	/** An array of strings to populate dropdown list */
	String[] actions = new String[] { "Top 10", "Sistema", "Oficina", "Juegos" };

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		try {
			if (InetAddress.getByName("192.168.1.4").isReachable(25)) {
				elementosLista(BuscarWS());
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

		/** Create an array adapter to populate dropdownlist */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.sherlock_spinner_item, actions);

		/** Enabling dropdown list navigation for the action bar */
		getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST);

		/** Defining Navigation listener */
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				cargarCategoria(itemPosition);
				return false;
			}
		};

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
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
			elementosLista(new String[] { "Lista de", "Aplicaciones de",
					"Sistema" });
			break;
		case 2:
			elementosLista(new String[] { "Lista de", "Aplicaciones de",
					"Oficina" });
			break;
		case 3:
			elementosLista(new String[] { "Lista de", "Aplicaciones de",
					"Juegos" });
			break;
		default:
			elementosLista(BuscarWS());
			break;
		}
	}

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
	private static final String SOAP_ACTIONname = "http://tempuri.org/TodosAppNom";
	private static final String METHOD_NAMEname = "TodosAppNom";
	private static final String SOAP_ACTIONid = "http://tempuri.org/TodosAppId";
	private static final String METHOD_NAMEid = "TodosAppId";

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
}
