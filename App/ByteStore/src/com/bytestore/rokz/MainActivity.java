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
	String[] actions = new String[] { "Categorias", "Sistema", "Oficina",
	"Juegos" };
	
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		try {
			if(InetAddress.getByName("192.168.1.4").isReachable(25)){
			ListView mlistView = (ListView) findViewById(R.id.listView1);
			mlistView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, BuscarWS()));

			mlistView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String s = ((TextView) view).getText().toString();

					Intent intent = new Intent(MainActivity.this, InfoApp.class);
					intent.putExtra("idApp", idAppsLista.get(Integer
							.valueOf(AppsLista.indexOf(String.valueOf(s)))));
					startActivity(intent);
				}
			});
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No se logro conectar al servidor. Intenda de nuevo mas tarde.", Toast.LENGTH_LONG).show();
			}
		} catch (UnknownHostException e) {
			Toast.makeText(getApplicationContext(), "No se logro conectar al servidor. Verifica tu conexion al internet.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "No se logro conectar al servidor. Verifica tu conexion al internet.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		
		/** Create an array adapter to populate dropdownlist */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.sherlock_spinner_item, actions);

        /** Enabling dropdown list navigation for the action bar */
        getSupportActionBar().setNavigationMode(com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST);

        /** Defining Navigation listener */
        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                Toast.makeText(getBaseContext(), "Current Action : " + actions[itemPosition]  , Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        /** Setting dropdown items and item navigation listener for the actionbar */
        getSupportActionBar().setListNavigationCallbacks(adapter, navigationListener);
        adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
	}

	public List<String> idAppsLista = new ArrayList<String>();
	public List<String> AppsLista = new ArrayList<String>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void goHome(View view){
		finish();
		startActivity(getIntent());
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
/*
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Categorias");
		menu.add(0, v.getId(), 0, "Sistema");
		menu.add(0, v.getId(), 0, "Oficina");
		menu.add(0, v.getId(), 0, "Juegos");
	}

	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Sistema") {
			funcSist(item.getItemId());
		} else if (item.getTitle() == "Oficina") {
			funcOfic(item.getItemId());
		} else if (item.getTitle() == "Juegos") {
			funcOfic(item.getItemId());
		} else {
			return false;
		}
		return true;
	}

	public void funcSist(int id) {
		Toast.makeText(this, "Mostrar apps de sistema", Toast.LENGTH_SHORT)
				.show();
	}

	public void funcOfic(int id) {
		Toast.makeText(this, "Mostrar apps de oficina", Toast.LENGTH_SHORT)
				.show();
	}

	public void funcJuegos(int id) {
		Toast.makeText(this, "Mostrar apps de juegos", Toast.LENGTH_SHORT)
				.show();
	}*/
}
