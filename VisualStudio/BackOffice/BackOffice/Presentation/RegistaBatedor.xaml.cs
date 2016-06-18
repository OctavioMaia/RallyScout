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
    /// Interaction logic for RegistaBatedor.xaml
    /// </summary>
    public partial class RegistaBatedor : Window
    {
        BackOfficeAPP backoffice;
        Window anterior;
        Boolean cancelar;

        public RegistaBatedor(BackOfficeAPP b,Window w)
        {
            this.anterior = w;
            this.backoffice = b;
            this.cancelar = false;
            InitializeComponent();
        }

        private void buttonOK_Click(object sender, RoutedEventArgs e)
        {
            String email = textBoxEmail.Text;
            String nome = textBoxNome.Text;
            String password = passwordBox.Password.ToString();
            try
            {
                if (email.Length > 0 && nome.Length > 0 && password.Length > 0)
                {
                    if (!backoffice.existeBatedor(email))
                    {
                        Batedor b = new Batedor(email, nome, password);
                        backoffice.registarBatedor(b);
                        this.cancelar = true;
                        this.Close();
                        this.anterior.Visibility = Visibility.Visible;
                    }
                    else
                    {
                        System.Windows.Forms.MessageBox.Show("Já existe um batedor com esse email!", "Warning!", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                    }
                }
                else
                {
                    System.Windows.Forms.MessageBox.Show("Verifique se introduziu todos os parametros!", "Warning!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
            }
            catch (Exception ex)
            {
                System.Windows.Forms.MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void button_Copy_Click(object sender, RoutedEventArgs e)
        {
            this.anterior.Visibility = Visibility.Visible;
            this.cancelar = true;
            this.Close();
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            if (!this.cancelar)
            {
                e.Cancel = true;
            }
        }
    }
}
