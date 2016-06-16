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
    /// Interaction logic for ConsultaBatedor.xaml
    /// </summary>
    public partial class ConsultaBatedor : Window
    {
        BackOfficeAPP backoffice;
        List<Batedor> lista;
        Window anterior;

        public ConsultaBatedor(BackOfficeAPP b,Window w)
        {
            this.anterior = w;
            this.backoffice = b;
            InitializeComponent();
            UpdateComboBox();
        }

        private void UpdateComboBox()
        {
            List<String> l = this.backoffice.getBatedoresMails();
            this.lista = this.backoffice.getBatedores(); 

            foreach (String s in l)
            {
                comboBox.Items.Add(s);
            }

        }

        private void comboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            String email = comboBox.SelectedItem as string;
            Batedor b = null;

            foreach (Batedor b1 in lista)
            {
                if (b1.email.Equals(email))
                {
                    b = b1;
                    break;
                }
            }

            textBoxNome.Text = b.nome;
            textBoxEmail.Text= b.email;
            textBoxHoras.Text = b.ficha.horasEmReConhecimento.ToString();
            textBoxAtividades.Text = b.ficha.nAtividades.ToString();

        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
            this.anterior.Visibility = Visibility.Visible;
        }
    }
}
