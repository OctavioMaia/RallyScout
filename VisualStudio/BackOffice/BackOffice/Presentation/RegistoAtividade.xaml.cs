using BackOffice.Business;
using BackOffice.Business.Exceptions;
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
using System.Windows.Forms;
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
        Window anterior;

        public RegistoAtividade(BackOfficeAPP b, Window w)
        {
            this.anterior = w;
            this.backoffice = b;
            InitializeComponent();
            UpdateComboBox();
        }

        private void UpdateComboBox()
        {
            List<String> l = this.backoffice.getBatedoresMails();

            foreach(String s in l)
            {
                comboBox.Items.Add(s);
            }
            
        }

        private void buttonAdicionarVeiculo_Click(object sender, RoutedEventArgs e)
        {
            l = new List<Veiculo>();
            InserirVeiculo i = new InserirVeiculo(backoffice,l,this);
            i.Visibility = Visibility.Visible;
            this.Visibility = Visibility.Hidden;
        }

        private void buttonProcurarFicheiro_Click(object sender, RoutedEventArgs e)
        {
            Microsoft.Win32.OpenFileDialog openFileDialog = new Microsoft.Win32.OpenFileDialog();
            if (openFileDialog.ShowDialog() == true)
                textBoxPath.Text = openFileDialog.FileName;
        }

        private void buttonOK_Click(object sender, RoutedEventArgs e)
        {
            String trajeto = textBoxPath.Text;
            String nomeProva = textBoxNomeProva.Text;
            String nomeEquipa = textBoxNomeEquipa.Text;
            String emailEquipa = textBoxEmailEquipa.Text;
            string mailBatedor = comboBox.SelectedItem as string;
            try {
                if (trajeto.Length > 0 && nomeProva.Length > 0 && nomeEquipa.Length > 0 && emailEquipa.Length > 0 && mailBatedor.Length > 0) {
                    if (System.IO.Path.GetExtension(trajeto).Equals(".gpx"))
                    {
                        backoffice.registarAtividade(mailBatedor, trajeto, nomeProva, nomeEquipa, emailEquipa, l);
                        this.Close();
                        this.anterior.Visibility = Visibility.Visible;
                    }
                    else
                    {
                        System.Windows.Forms.MessageBox.Show("Apenas são permitidos ficheiros .gpx!", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    }
                }
                else
                {
                    System.Windows.Forms.MessageBox.Show("Verifique se introduziu todos os parâmetros.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
            }catch(System.Xml.XmlException)
            {
                System.Windows.Forms.MessageBox.Show("Ficheiro .gpx inválido!", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                
            }catch(MapaVazioException ex)
            {
                System.Windows.Forms.MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void buttonCancelar_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
            this.anterior.Visibility = Visibility.Visible;
        }
    }
}

