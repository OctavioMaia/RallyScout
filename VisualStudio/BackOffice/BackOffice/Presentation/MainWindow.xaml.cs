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
    
    public partial class MainWindow : Window
    {
        Boolean started = false;
        BackOfficeAPP backoffice;
        Window anterior;

        public MainWindow(BackOfficeAPP b, Window w)
        {
            this.backoffice = b;
            this.anterior = w;
            InitializeComponent();
            List<String> li = b.IP;
            updateComboBox(li);
            this.textBoxPorta.Text = b.port.ToString();
            this.ellipse.Fill = new SolidColorBrush(Colors.Red);
        }

        private void updateComboBox(List<String> l)
        {
            String aux = null;
            if (l.Count > 0)
            {
                aux = l[0];
                foreach (String s in l)
                {
                    comboBox.Items.Add(s);
                }
            }
            else
            {
                aux = "IP Desconhecido";
                comboBox.Items.Add(aux);
                // comboBox.AllowDrop = false;
            }
            comboBox.SelectedItem = aux;
        }

        private void buttonServer_Click(object sender, RoutedEventArgs e)
        {
            if (!started)
            {

                    ellipse.Fill = new SolidColorBrush(Colors.Green);
                    this.buttonServer.Content = "Stop";
                this.buttonServer.IsEnabled = false;
                    this.backoffice.startReceive();
                    
                    started = true;

                
            }
            else
            {

                    this.backoffice.stopReceive();
                    ellipse.Fill = new SolidColorBrush(Colors.Red);
                    this.buttonServer.Content = "Start";
                    started = false;

            }
        }

        private void buttonRegistaAtividade_Click(object sender, RoutedEventArgs e)
        {
            if (this.backoffice.getBatedores().Count > 0)
            {
                RegistoAtividade ra = new RegistoAtividade(this.backoffice,this);
                ra.Visibility = Visibility.Visible;
                this.Visibility = Visibility.Hidden;
            }
            else
            {
                //MessageBox.Show();
                System.Windows.Forms.MessageBox.Show("Não existem batedores no sistema!", "Warning",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        private void buttonConsultaAtividade_Click(object sender, RoutedEventArgs e)
        {
            if (this.backoffice.getAtividadesTerminadas().Count > 0)
            {
                ConsultarAtividadeConcluida cac = new ConsultarAtividadeConcluida(this.backoffice,this);
                cac.Visibility = Visibility.Visible;
                this.Visibility = Visibility.Hidden;
            }
            else
            {
               // MessageBox.Show();
                System.Windows.Forms.MessageBox.Show("Não existem atividades terminadas no sistema!", "Warning",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        private void buttonRegistaBatedir_Click(object sender, RoutedEventArgs e)
        {
            RegistaBatedor rb = new RegistaBatedor(this.backoffice,this);
            rb.Visibility = Visibility.Visible;
            this.Visibility = Visibility.Hidden;
        }

        private void buttonConsultaBatedor_Click(object sender, RoutedEventArgs e)
        {
            if (this.backoffice.getBatedores().Count > 0) {
                ConsultaBatedor cb = new ConsultaBatedor(this.backoffice,this);
                cb.Visibility = Visibility.Visible;
                this.Visibility = Visibility.Hidden;
            }
            else
            {
               // MessageBox.Show("Não existem batedores no sistema!");
                System.Windows.Forms.MessageBox.Show("Não existem batedores no sistema!", "Warning",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        private void buttonLogout_Click(object sender, RoutedEventArgs e)
        {
            this.Visibility = Visibility.Hidden;
            this.anterior.Visibility = Visibility.Visible;
        }

        private void comboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            
        }
    }
}
