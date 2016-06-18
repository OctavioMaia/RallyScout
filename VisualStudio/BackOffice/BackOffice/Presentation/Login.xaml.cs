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
            string pathG = "Z:\\JSON";
            try
            {
               /* System.Diagnostics.Debug.WriteLine(" Comecar 0....");
                Console.Error.WriteLine(" Comecar 1....");*/
              //  Console.WriteLine(" Comecar 2....");

                this.backoffice = new BackOfficeAPP(path);
                this.backoffice.gerarJsonDebug(pathG); //into épara debug
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
                this.mainW = new MainWindow(this.backoffice, this);
            }
            this.Visibility = Visibility.Hidden;
            this.mainW.Visibility = Visibility.Visible;
        }

        private void buttonSair_Click(object sender, RoutedEventArgs e)
        {
            if (this.mainW != null)
            {
                this.mainW.Close();
            }
            if (this.backoffice != null)
            {
                this.backoffice.stopReceive();
            }
            this.Close();
        }
    }
}
