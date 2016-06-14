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

        public RegistoAtividade()
        {
            InitializeComponent();
        }

        private void buttonAdicionarVeiculo_Click(object sender, RoutedEventArgs e)
        {
            l = new List<Veiculo>();
            InserirVeiculo i = new InserirVeiculo(l);
            i.Visibility = Visibility.Visible;
        }

        private void buttonProcurarFicheiro_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            if (openFileDialog.ShowDialog() == true)
                textBoxPath.Text = openFileDialog.FileName;
        }

    }
}

