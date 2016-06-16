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
    /// Interaction logic for RegistaBatedor.xaml
    /// </summary>
    public partial class RegistaBatedor : Window
    {
        BackOfficeAPP backoffice;
        Window anterior;

        public RegistaBatedor(BackOfficeAPP b,Window w)
        {
            this.anterior = w;
            this.backoffice = b;
            InitializeComponent();
        }

        private void buttonOK_Click(object sender, RoutedEventArgs e)
        {
            String email = textBoxEmail.Text;
            String nome = textBoxNome.Text;
            String password = passwordBox.Password.ToString();

            if(email.Length>0 && nome.Length>0 && password.Length > 0)
            {
                if (!backoffice.existeBatedor(email))
                {
                    Batedor b = new Batedor(email, nome, password);
                    backoffice.registarBatedor(b);
                    this.Close();
                    this.anterior.Visibility = Visibility.Visible;
                }
                else
                {
                    MessageBox.Show("Já existe um batedor com esse email!");
                }
            }
            else
            {
                MessageBox.Show("Verifique se introduziu todos os parametros!");
            }

           
        }

        private void button_Copy_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
            this.anterior.Visibility = Visibility.Visible;
        }
    }
}
