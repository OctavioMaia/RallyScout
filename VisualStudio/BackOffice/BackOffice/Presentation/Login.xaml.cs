using BackOffice.Business;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace BackOffice.Presentation
{
    /// <summary>
    /// Interaction logic for Login.xaml
    /// </summary>
    public partial class Login : Window
    {
        BackOfficeAPP backoffice;
        MainWindow mainW;
        public Login(BackOfficeAPP b)
        {
            backoffice = b;
            InitializeComponent();
            this.mainW = null;
        }

        public Login()
        {
            string localFolder = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
            string path = localFolder + "\\Config\\config.json";
           // string pathG = "Z:\\JSON";
            try
            {

                this.backoffice = new BackOfficeAPP(path);
               // this.backoffice.gerarJsonDebug(pathG); //into épara debug
                InitializeComponent();
            }
            catch(Exception e)
            {
                System.Windows.Forms.MessageBox.Show(e.Message.ToString(), "Error",MessageBoxButtons.OK, MessageBoxIcon.Error);
                this.Close();
                
            }
        }

        private void buttonLogin_Click(object sender, RoutedEventArgs e)
        {
            if (this.mainW == null)
            {
                try
                {
                    this.mainW = new MainWindow(this.backoffice, this);

                }catch(Exception ex)
                {
                    System.Windows.Forms.MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            if (this.verificaLogin() == true)
            {
                this.Visibility = Visibility.Hidden;
                this.mainW.Visibility = Visibility.Visible;
                this.textBoxUsername.Text = "";
            }
            else
            {
                System.Windows.Forms.MessageBox.Show("Dados de Login errados, por favor verifique.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }

            this.passwordBox.Password = "";
        }

        private bool verificaLogin()
        {
            if(this.textBoxUsername.Text==null || this.textBoxUsername.Text.Length.Equals(0))
            {
                return false;
            }

            if (this.passwordBox.Password == null || this.passwordBox.Password.Length.Equals(0))
            {
                return false;
            }


            if(!(this.textBoxUsername.Text.Equals(this.backoffice.email) && this.passwordBox.Password.Equals(this.backoffice.passMail)))
            {
                return false;
            }

            return true;
        }
        private void buttonSair_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            if (this.mainW != null)
            {
                this.mainW.Close();
            }
            if (this.backoffice != null)
            {
                this.backoffice.stopReceive();
            }
        }
    }
}
