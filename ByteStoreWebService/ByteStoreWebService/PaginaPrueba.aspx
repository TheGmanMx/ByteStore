<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="PaginaPrueba.aspx.cs" Inherits="ByteStoreWebService.PaginaPrueba" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <asp:MultiView ID="MultiViewMain" runat="server">
        <asp:View ID="ViewGral" runat="server" onactivate="ViewGral_Activate">
            
            <table align = center>
                <tr>
                    <td align = "center">
                        <asp:Button ID="btnUsuarios" runat="server" Text="Usuarios" Height="54px" 
                            Width="102px" onclick="btnUsuarios_Click"/>
                    </td>
                    <td  align = "center">
                        <asp:Button ID="btnDevelopers" runat="server" Text="Developers" Height="54px" 
                            Width="102px" onclick="btnDevelopers_Click"/>
                    </td>
                    <td  align = "center">
                        <asp:Button ID="btnApps" runat="server" Text="Apps" Height="54px" 
                            Width="102px" onclick="btnApps_Click"/>
                    </td>
                    <td align = "center">
                        <asp:Button ID="btnEtc" runat="server" Text="Etc" Height="54px" 
                            Width="102px"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:DataGrid ID="DataGridUsuarios" runat="server">
                        </asp:DataGrid>
                    </td>
                    <td>
                        <asp:DataGrid ID="DataGridDevelopers" runat="server">
                        </asp:DataGrid>
                    </td>
                    <td>
                        <asp:DataGrid ID="DataGridApps" runat="server">
                        </asp:DataGrid>
                    </td>
                    <td>
                        <asp:DataGrid ID="DataGridHistorial" runat="server">
                        </asp:DataGrid>
                    </td>
                </tr>
            </table>
        </asp:View>
        
        <asp:View ID="ViewUsuarios" runat="server">
            
            <table align = center>
                <tr>
                    <td>
                        <asp:Label ID="Label1" runat="server" Text="Usuario"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtUsuario" runat="server" Width="250px" MaxLength="16"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label2" runat="server" Text="Contraseña"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtPassword" runat="server" Width="250px" MaxLength="8" 
                            TextMode="Password"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label3" runat="server" Text="Nombre"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtNombre" runat="server" Width="250px" MaxLength="25"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label4" runat="server" Text="Correo"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtCorreo" runat="server" Width="250px" MaxLength="40"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td colspan = "2" align = "right">
                        <asp:Button ID="btnAltaUsuario" runat="server" Text="Enlistarse" 
                            onclick="btnAltaUsuario_Click" />
                    </td>
                </tr>
            </table>

        </asp:View>
        <asp:View ID="ViewDevelopers" runat="server">
            <table align = center>
                <tr>
                    <td>
                        <asp:Label ID="Label6" runat="server" Text="Password"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtPasswordDev" runat="server" Width="250px" MaxLength="8" 
                            TextMode="Password"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label7" runat="server" Text="Nombre"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtNombreDev" runat="server" Width="250px" MaxLength="20"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label8" runat="server" Text="Apellido"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtApellidoDev" runat="server" Width="250px" MaxLength="50"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label5" runat="server" Text="Correo"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtCorreoDev" runat="server" Width="250px" MaxLength="40"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label9" runat="server" Text="Grupo"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtGrupo" runat="server" Width="250px" MaxLength="30"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="lblRespuestaDev" runat="server" Text=""></asp:Label>
                    </td>
                    <td align = "right">
                        <asp:Button ID="btnEnlistarseDev" runat="server" Text="Enlistarse" 
                            onclick="btnAltaDeveloper_Click" />
                    </td>
                </tr>
            </table>

        </asp:View>

        <asp:View ID="ViewApps" runat="server">
            <table align = center>
                <tr>
                    <td>
                        <asp:Label ID="Label10" runat="server" Text="Nombre"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtNombreApp" runat="server" Width="250px" MaxLength="25"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label11" runat="server" Text="Versión"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtVersionApp" runat="server" Width="250px" MaxLength="10"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label12" runat="server" Text="Url"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtUrlApp" runat="server" Width="250px" MaxLength="50"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label13" runat="server" Text="Descripción"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="txtDescripcionApp" runat="server" Width="252px" MaxLength="100" 
                            Height="69px" TextMode="MultiLine"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label15" runat="server" Text="Icono"></asp:Label>
                    </td>
                    <td>
                        <asp:FileUpload ID="FileUploadIcono" runat="server" Width="252" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label16" runat="server" Text="Imagen 1"></asp:Label>
                    </td>
                    <td>
                        <asp:FileUpload ID="FileUploadImg1" runat="server" Width="252"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label17" runat="server" Text="Imagen 2"></asp:Label>
                    </td>
                    <td>
                        <asp:FileUpload ID="FileUploadImg2" runat="server" Width="252"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Label ID="Label18" runat="server" Text="Imagen 3"></asp:Label>
                    </td>
                    <td>
                        <asp:FileUpload ID="FileUploadImg3" runat="server" Width="252"/>
                    </td>
                </tr>
                <tr>
                    <td colspan = "2" align = "right">
                        <asp:Button ID="btnSubirApp" runat="server" Text="Subir" 
                            onclick="btnSubirApp_Click" />
                    </td>
                </tr>
            </table>

        </asp:View>
    </asp:MultiView>
    <div>
    </div>
    </form>
</body>
</html>
