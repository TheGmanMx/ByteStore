package com.bytestore.rokz;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

public class Sesion {

	public static String user, pass;

	public static boolean SesionActiva = false;

	public Sesion() {
	}

	public boolean IniciarSesion(String user, String pass) {
		Sesion.user = user;
		Sesion.pass = pass;
		// reviso si esta correcta la informacion
		new LogIn().execute();
		return true;
	}

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://192.168.1.4/AppStore/Service1.asmx";
	private static final String SOAP_ACTION = "http://tempuri.org/LogIn";
	private static final String METHOD_NAME = "LogIn";
	public boolean ExisteUsuario;

	private class LogIn extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(String... params) {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("user", user);
			request.addProperty("pass", pass);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE ht = new HttpTransportSE(URL);
			try {
				ht.call(SOAP_ACTION, envelope);

				SoapObject result = (SoapObject) envelope.bodyIn;
				// SoapObject result1 = (SoapObject) result.getProperty(0);
				ExisteUsuario = Boolean.parseBoolean(result
						.getPropertyAsString("LogInResult"));
				Sesion.SesionActiva = ExisteUsuario;
			} catch (Exception e) {
				System.out.println("Error" + e);
			}
			return null;
		}

	}

	public String terminarSesion() {
		String userBye = Sesion.user;
		Sesion.user = "";
		Sesion.pass = "";
		Sesion.SesionActiva = false;
		return "La sesion del usuario " + userBye + " ha terminado!";
	}

	public boolean SesionActiva() {
		return Sesion.SesionActiva;
	}

}
