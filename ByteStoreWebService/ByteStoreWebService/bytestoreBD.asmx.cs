using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Data.SqlClient;
using System.Data;
using System.Text;
using System.Xml;
using MySql.Data.MySqlClient;
using System.Drawing;
using System.IO;
using System.Drawing.Imaging;

namespace ByteStoreWebService
{
    /// <summary>
    /// Summary description for bytestoreBD
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class bytestoreBD : System.Web.Services.WebService
    {
        static string MyConString = "SERVER=localhost;" +
                "DATABASE=Bytestore;" +
                "UID=root;" +
                "PASSWORD=nandito3555;";
        MySqlConnection cn = new MySqlConnection(MyConString);
        MySqlCommand sqm;
        MySqlDataReader sdr;
        MySqlDataAdapter sad;
        DataSet ds;
        DataTable dt;

        [WebMethod(CacheDuration = 30,
            Description = "Da de alta un usuario recibiendo como parámetros idusuario," +
            " password, nombre,  y correo. Regresa el resultado de la operación.")]
        public string AltaUsuario(string usuario, string password,
            string nombre, string correo)
        {
            try
            {
                //Set insert query
                string qry = "insert into Usuarios values('" + usuario + "','" + password + "','" + nombre + 
                    "','" + correo + "')";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "Operación exitosa";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Da de baja un usuario recibiendo como parámetro el idUsuario.")]
        public string BajaUsuario(string usuario)
        {
            try
            {
                //Set insert query
                string qry = "delete Usuarios where idUsuario = '" + usuario + "'";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Actualiza el password de un usuario recibiendo como parámetro su idUsuario y la nueva password.")]
        public string ActualizaPasswordUsuario(string usuario, string password)
        {
            try
            {
                //Set insert query
                string qry = "update Usuarios set password = '" + password + "' where idUsuario = '" + usuario + "'";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }
        [WebMethod(CacheDuration = 30,
            Description = "No sirve esta mugre")]
        public string ConsultarUsuario(string usuario)
        {
            try
            {
                //Llenar la tabla con los datos
                sqm = new MySqlCommand("SELECT * FROM Usuarios where idUsuario = '" + usuario + "'", cn);
                MySqlDataReader sqr = sqm.ExecuteReader();
                if (sqr.HasRows)
                {
                    while (sqr.Read())
                    {
                        List<string> registro = new List<string>();
                        registro.Add(sqr["nombre_usuario"].ToString());
                        registro.Add(sqr["password"].ToString()); 
                        registro.Add(sqr["nombre"].ToString());
                        registro.Add(sqr["correo"].ToString());
                    }
                }
                return "no sirve";
            }
            catch (Exception ex)
            {
                return null;
            }
        }
        [WebMethod(CacheDuration = 30,
            Description = "Da de alta un developer recibiendo como parámetros password," + 
            " nombre, apellido, correo y un parámetro opcional grupo. Regresa el idDeveloper generado.")]
        public string AltaDeveloper(string password,
            string nombre, string apellido, string correo, string grupo = "n/a")
        {
            try
            {
                //Set insert query
                string qry;
                qry = "insert into Developers values(0,'" + password + "','" + nombre + "','" + apellido +
                        "','" + correo + "','" + grupo + "')";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                string idDev = "";
                //Buscar el id que se acaba de insertar
                sqm = new MySqlCommand("SELECT MAX(idDeveloper) as id from Developers", cn);
                MySqlDataReader sqr = sqm.ExecuteReader();
                if (sqr.HasRows)
                {
                    while (sqr.Read())
                    {
                        idDev = sqr["id"].ToString();
                    }
                }
                cn.Close();

                return idDev;
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Da de baja un developer recibiendo como parámetro su idDeveloper.")]
        public string BajaDeveloper(string idDeveloper)
        {
            try
            {
                //Set insert query
                string qry = "delete Usuarios where idDeveloper = " + idDeveloper + "";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "baja satisafactoria";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Actualiza el password de un developer recibiendo como parámetro su idDeveloper y la nueva password.")]
        public string ActualizaPasswordDeveloper(string idDeveloper, string password)
        {
            try
            {
                //Set insert query
                string qry = "update Developers set password = '" + password + "' where idDeveloper = '" + idDeveloper + "'";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "contraseña actualizada";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Da de alta un App recibiendo como parámetros nombre, version, url," +
            " descripcion, y parámetros opcionales icon, img1, img2, img3 (nombres) y grupo. " + 
            "Regresa el idApp.")]
        public string AltaApp(string nombre, string version, string url,
            string descripcion, string iconName = "default.gif", byte[] icon = null, string img1Name = "default.gif",
            byte[] img1 = null, string img2Name = "default.gif", byte[] img2 = null, string img3Name = "default.gif", 
            byte[] img3 = null)
        {
            try//"~/Resources/default.gif"
            {
                string serverPath = Server.MapPath("ByteStoreWebService");
                //Insertar prefijo para la url final
                iconName = "/" + nombre + "/" + iconName;
                img1Name = "/" + nombre + "/" + img1Name;
                img2Name = "/" + nombre + "/" + img2Name;
                img3Name = "/" + nombre + "/" + img3Name;
                //Insertar el registro
                //Set insert query
                string qry = "insert into Apps values(0,'" + nombre + "','" + version + "','" + url +
                        "','" + descripcion + "',0,'" + iconName + "','" + img1Name + "','" + img2Name + "','" + img3Name + "')";
                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);
                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                //Guardar las imagenes
                System.Drawing.Image newImage;
                if(icon != null)
                {
                    string strFileName = Server.MapPath("Resources") + iconName;
                    using (MemoryStream stream = new MemoryStream(icon))
                    {
                        newImage = System.Drawing.Image.FromStream(stream);

                        newImage.Save(strFileName);

                        //img.Attributes.Add("src", strFileName);
                    }

                    /*using (Image image = Image.FromStream(new MemoryStream(icon)))
                    {
                        image.Save(iconName, ImageFormat.Jpeg);  
                    }*/
                }
                if (img1 != null)
                {
                    string strFileName = Server.MapPath("Resources") + img1Name;
                    using (MemoryStream stream = new MemoryStream(img1))
                    {
                        newImage = System.Drawing.Image.FromStream(stream);

                        newImage.Save(strFileName);

                        //img.Attributes.Add("src", strFileName);
                    }
                    /*using (Image image = Image.FromStream(new MemoryStream(img1)))
                    {
                        image.Save(img1Name, ImageFormat.Jpeg);  
                    }*/
                }
                if (img2 != null)
                {
                    string strFileName = Server.MapPath("Resources") + img2Name;
                    using (MemoryStream stream = new MemoryStream(img2))
                    {
                        newImage = System.Drawing.Image.FromStream(stream);

                        newImage.Save(strFileName);

                        //img.Attributes.Add("src", strFileName);
                    }
                    /*using (Image image = Image.FromStream(new MemoryStream(img2)))
                    {
                        image.Save(img2Name, ImageFormat.Jpeg);  
                    }*/
                }
                if (img3 != null)
                {
                    string strFileName = Server.MapPath("Resources") + img3Name;
                    using (MemoryStream stream = new MemoryStream(img3))
                    {
                        newImage = System.Drawing.Image.FromStream(stream);

                        newImage.Save(strFileName);

                        //img.Attributes.Add("src", strFileName);
                    }
                    /*using (Image image = Image.FromStream(new MemoryStream(img3)))
                    {
                        image.Save(img3Name, ImageFormat.Jpeg); 
                    }*/
                }

                return "";
            }
            catch (Exception ex)
            {
                return Server.MapPath("Resources") + " " + ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Da de baja un App recibiendo como parámetro su idApp.")]
        public string BajaApp(string idApp)
        {
            try
            {
                //Set insert query
                string qry = "delete Apps where idApp = " + idApp + "";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "baja satisafactoria";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Actualiza el ícono principl de una App recibiendo como parámetro su idApp y el url del ícono.")]
        public string ActualizaIconoApp(string idApp, string icono)
        {
            try
            {
                //Set insert query
                string qry = "update Apps set icono = " + icono + " where idApp = '" + idApp + "'";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "icono actualizado";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }
        
        [WebMethod(CacheDuration = 30,
            Description = "Actualiza la imágen 1 de una App recibiendo como parámetro su idApp y el url de la imágen.")]
        public string ActualizaImg1(string idApp, string img1)
        {
            try
            {
                //Set insert query
                string qry = "update Apps set img1 = " + img1 + " where idApp = '" + idApp + "'";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "imagen actualizada";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Actualiza la imágen 2 de una App recibiendo como parámetro su idApp y el url de la imágen.")]
        public string ActualizaImg2(string idApp, string img2)
        {
            try
            {
                //Set insert query
                string qry = "update Apps set img2 = " + img2 + " where idApp = '" + idApp + "'";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "imagen actualizada";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }

        [WebMethod(CacheDuration = 30,
            Description = "Actualiza la imágen 2 de una App recibiendo como parámetro su idApp y el url de la imágen.")]
        public string ActualizaImg3(string idApp, string img3)
        {
            try
            {
                //Set insert query
                string qry = "update Apps set img3 = " + img3 + " where idApp = '" + idApp + "'";

                //Initialize SqlCommand object for insert.
                MySqlCommand SqlCom = new MySqlCommand(qry, cn);

                //Open connection and execute insert query.
                cn.Open();
                SqlCom.ExecuteNonQuery();
                cn.Close();

                return "imagen actualizada";
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
        }
    }

        
}
