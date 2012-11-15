using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using ByteStoreWebService;
using System.Data;
using System.Xml;
using System.Web.Services.Protocols;
using System.IO;
using System.Text;

namespace ByteStoreWebService
{
    public partial class PaginaPrueba : System.Web.UI.Page
    {
        bytestoreBD servicio = new bytestoreBD();

        protected void Page_Load(object sender, EventArgs e)
        {
            Actualizar();
            this.Response.Write(Directory.GetCurrentDirectory());
            MultiViewMain.SetActiveView(ViewGral);
        }

        protected void btnAltaUsuario_Click(object sender, EventArgs e)
        {
            if(txtUsuario.Text.Length > 0 &&
                txtNombre.Text.Length > 0 && 
                txtPassword.Text.Length > 0 &&
                txtCorreo.Text.Length > 0)
            {
                servicio.AltaUsuario(txtUsuario.Text, txtPassword.Text, txtNombre.Text, txtCorreo.Text);
                txtUsuario.Text = "";
                txtPassword.Text = "";
                txtNombre.Text = "";
                txtCorreo.Text = "";
                MultiViewMain.SetActiveView(ViewGral);
            }
        }
        
        protected void ViewGral_Activate(object sender, EventArgs e)
        {
            Actualizar();
        }

        private void Actualizar()
        {

        }

        protected void btnUsuarios_Click(object sender, EventArgs e)
        {
            MultiViewMain.SetActiveView(ViewUsuarios);
        }

        protected void btnAltaDeveloper_Click(object sender, EventArgs e)
        {
            if (txtPasswordDev.Text.Length > 0 &&
                txtNombreDev.Text.Length > 0 &&
                txtApellidoDev.Text.Length > 0 &&
                txtCorreoDev.Text.Length > 0)
            {
                string id = servicio.AltaDeveloper(txtPasswordDev.Text, txtNombreDev.Text, txtApellidoDev.Text, 
                    txtCorreoDev.Text, txtGrupo.Text);
                txtPasswordDev.Text = "";
                txtNombreDev.Text = "";
                txtApellidoDev.Text = "";
                txtCorreoDev.Text = "";
                txtGrupo.Text = "";
                lblRespuestaDev.Text = "Tu idDeveloper es: " + id;
                MultiViewMain.SetActiveView(ViewDevelopers);
            }
        }

        protected void btnDevelopers_Click(object sender, EventArgs e)
        {
            MultiViewMain.SetActiveView(ViewDevelopers);
        }

        protected void btnSubirApp_Click(object sender, EventArgs e)
        {
            if (txtNombreApp.Text.Length > 0 &&
                txtUrlApp.Text.Length > 0 &&
                txtVersionApp.Text.Length > 0 &&
                txtDescripcionApp.Text.Length > 0)
            {
                this.Response.Write(servicio.AltaApp(txtNombreApp.Text, txtVersionApp.Text, txtUrlApp.Text, txtDescripcionApp.Text,
                    FileUploadIcono.FileName, FileUploadIcono.FileBytes, FileUploadImg1.FileName, FileUploadImg1.FileBytes, 
                    FileUploadImg2.FileName, FileUploadImg2.FileBytes, FileUploadImg3.FileName, FileUploadImg3.FileBytes));
            }
            txtNombreApp.Text = "";
            txtUrlApp.Text = "";
            txtVersionApp.Text = "";
            txtDescripcionApp.Text = "";
        }

        protected void btnApps_Click(object sender, EventArgs e)
        {
            MultiViewMain.SetActiveView(ViewApps);
        }
    }
}