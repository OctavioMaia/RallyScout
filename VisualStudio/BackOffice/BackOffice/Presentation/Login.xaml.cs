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

        public Login(BackOfficeAPP b)
        {
            backoffice = b;
            InitializeComponent();
        }

        public Login()
        {
            string pathO = "C:\\Users\\Octávio\\Documents\\GitHub\\RallyScout\\ExemplosJson\\config.json";
            string pathJ = "C:\\Users\\Joao\\Documents\\GitHub\\RallyScout\\ExemplosJson\\config.json";


            //BackOfficeAPP b = new BackOfficeAPP(pathJ);
           // backoffice = b;
            InitializeComponent();
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
                MessageBox.Show("Credenciais incorretas!");
            }*/
            Atividade a = new Atividade(1, "equipateste", "teste", "C:\\Users\\Joao\\Desktop\\Mapas\\map.gpx", new List<Veiculo>(), new Equipa("equipa1","mail1"), new Batedor("mailbatedor","batedor","123"));
            a.addNota(new Nota(1,"abc", 41.478254,-8.300161,null,null));
            a.addNota(new Nota(2, "abc", 41.443217,-8.2916261, null, null));
            a.addNota(new Nota(3, "def", 41.48096,-8.295004,null, null));
            a.addNota(new Nota(4, "ghi", 41.509005,-8.25150, null, null));
            VisualizadorMap vm = new VisualizadorMap();
            vm.carregaMapa(a);
            vm.updateComboBox(a);
            vm.Visible =true;
        }

        private void buttonSair_Click(object sender, RoutedEventArgs e)
        {
            this.Visibility = Visibility.Hidden;
        }
    }
}
