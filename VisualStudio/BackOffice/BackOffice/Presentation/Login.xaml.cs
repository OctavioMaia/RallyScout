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
            //path = "C:\\Users\\Documents\\GitHub\\RallyScout\\ExemplosJson\\config.json";
            try
            {
               /* System.Diagnostics.Debug.WriteLine(" Comecar 0....");
                Console.Error.WriteLine(" Comecar 1....");*/
              //  Console.WriteLine(" Comecar 2....");

                this.backoffice = new BackOfficeAPP(path);
                InitializeComponent();
            }
            catch(Exception e)
            {
                System.Windows.Forms.MessageBox.Show(e.Message.ToString(), "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
                this.Close();
                
            }
        }

        private void buttonLogin_Click(object sender, RoutedEventArgs e)
        {

            /*String user = textBoxUsername.Text;
            String pw = passwordBox.Password.ToString();
             
            if(user.Equals(backoffice.email) && pw.Equals(backoffice.passMail)) {
                RegistoAtividade ra = new RegistoAtividade(backoffice);
                ra.Visibility = Visibility.Visible;
            }
            else
            {
                System.Windows.Forms.MessageBox.Show("Credenciais incorretas!", "Warning",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);

            }*/
            /* Atividade a = new Atividade(1, "equipateste", "teste", "C:\\Users\\Joao\\Desktop\\Mapas\\map.gpx", new List<Veiculo>(), new Equipa("equipa1","mail1"), new Batedor("mailbatedor","batedor","123"));
             a.addNota(new Nota(1,"abc", 41.478254,-8.300161,null,null));
             a.addNota(new Nota(2, "abc", 41.443217,-8.2916261, null, null));
             a.addNota(new Nota(3, "def", 41.48096,-8.295004,null, null));
             a.addNota(new Nota(4, "ghi", 41.509005,-8.25150, null, null));*/
            if (this.mainW == null)
            {
                this.mainW = new MainWindow(this.backoffice, this);
            }
             

            //vm.carregaMapa(a);
            //vm.updateComboBox(a);
            this.Visibility = Visibility.Hidden;
            this.mainW.Visibility = Visibility.Visible;
        }

        private void buttonSair_Click(object sender, RoutedEventArgs e)
        {
            if (this.mainW != null)
            {
                this.mainW.Close();
            }
            this.Close();
           // this.Visibility = Visibility.Hidden;
        }
    }
}
