using BackOffice.Business;
using Microsoft.Win32;
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
    /// Interaction logic for GerirRelatorio.xaml
    /// </summary>
    public partial class GerirRelatorio : Window
    {
        BackOfficeAPP backoffice;
        Atividade current;
        Boolean gerado;

        public GerirRelatorio(BackOfficeAPP b)
        {
            this.backoffice = b;
            InitializeComponent();
        }

        private void buttonProcurar_Click(object sender, RoutedEventArgs e)
        {
            gerado = false;
            try {
                FolderBrowserDialog fbd = new FolderBrowserDialog();
                System.Windows.Forms.DialogResult dr = fbd.ShowDialog();
                if (dr == System.Windows.Forms.DialogResult.OK)
                {
                    textBoxPath.Text = fbd.SelectedPath;
                }
            } catch (Exception) {
            }
        }

        private void buttonGerarRelatorio_Click(object sender, RoutedEventArgs e)
        {
            String path = textBoxPath.Text;
            this.backoffice.gerarRelatorios(path,current.idAtividade);
            gerado = true;
        }

        private void buttonEnviar_Click(object sender, RoutedEventArgs e)
        {
            String destino = this.current.equipa.email;
            String assunto = textBoxAssunto.Text;
            TextRange textRange = new TextRange(richTextBox.Document.ContentStart,richTextBox.Document.ContentEnd);
            String texto = textRange.Text;
            String path = textBoxPath.Text;

            if (destino.Length > 0 && assunto.Length>0 && texto.Length>0 && path.Length>0)
            {
                String piloto = path + "copiloto.pdf";
                String general = path + "general.pdf";

                System.Windows.MessageBox.Show("piloto path: " + piloto + " general path: " + general);

                List<String> anexos = new List<String>();
                anexos.Add(piloto);
                anexos.Add(general);

                this.backoffice.email_send(current.equipa.email, assunto, texto,anexos);

                System.Windows.Forms.MessageBox.Show("Email enviado com sucesso.", "Success!", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else
            {
                System.Windows.Forms.MessageBox.Show("Por favor preencha todos os campos.", "Warning!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
            
        }

        private void buttonRegressar_Click(object sender, RoutedEventArgs e)
        {
            this.Visibility = Visibility.Hidden;
        }
    }
}
