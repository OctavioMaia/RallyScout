using BackOffice.Business;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.IO;
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
    /// Interaction logic for RegistoAtividade.xaml
    /// </summary>
    public partial class RegistoAtividade : Window
    {
        List<Veiculo> l { get; set; }
        BackOfficeAPP backoffice;

        public RegistoAtividade(BackOfficeAPP b)
        {
            backoffice = b;
            InitializeComponent();
        }

        private void buttonAdicionarVeiculo_Click(object sender, RoutedEventArgs e)
        {
            l = new List<Veiculo>();
            InserirVeiculo i = new InserirVeiculo(backoffice,l);
            i.Visibility = Visibility.Visible;
        }

        private void buttonProcurarFicheiro_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            if (openFileDialog.ShowDialog() == true)
                textBoxPath.Text = openFileDialog.FileName;
        }

        private void buttonOK_Click(object sender, RoutedEventArgs e)
        {
            String trajeto = textBoxPath.Text;//.Replace('/', '\\');
            String nomeProva = textBoxNomeProva.Text;
            String nomeEquipa = textBoxNomeEquipa.Text;
            String emailEquipa = textBoxEmailEquipa.Text;
            int index = comboBox.SelectedIndex;

            //List<Batedor> batedores = backoffice.getBatedores();
            String mailBatedor = "";// = backoffice.getBatedor().email();

            backoffice.registarAtividade(mailBatedor, trajeto, nomeProva, nomeEquipa, emailEquipa, l); 
            this.Visibility = Visibility.Hidden;
        }

        private void buttonCancelar_Click(object sender, RoutedEventArgs e)
        {
            this.Visibility = Visibility.Hidden;
        }
    }
}

