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

        private void buttonLogin_Click(object sender, RoutedEventArgs e)
        {

            String user = textBoxUsername.Text;
            String pw = passwordBox.Password.ToString();
             
            if(user.Equals(backoffice.email) && pw.Equals(backoffice.passMail)) {
                RegistoAtividade ra = new RegistoAtividade(backoffice);
                ra.Visibility = Visibility.Visible;
            }
            else
            {
                MessageBox.Show("Credenciais incorretas!");
            }
        }

        private void buttonSair_Click(object sender, RoutedEventArgs e)
        {
            this.Visibility = Visibility.Hidden;
        }
    }
}
