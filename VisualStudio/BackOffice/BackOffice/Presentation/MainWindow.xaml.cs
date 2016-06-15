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
        }

        private void buttonServer_Click(object sender, RoutedEventArgs e)
        {
            if (!started)
            {
                this.backoffice.startReceive();
                ellipse.Fill = new SolidColorBrush(Colors.Green);
                this.buttonServer.Content = "Stop";
            }
            else
            {
                this.backoffice.stopReceive();
                ellipse.Fill = new SolidColorBrush(Colors.Red);
                this.buttonServer.Content = "Start";
            }
        }

        private void buttonRegistaAtividade_Click(object sender, RoutedEventArgs e)
        {
            RegistoAtividade ra = new RegistoAtividade(this.backoffice);
            ra.Visibility = Visibility.Visible;
        }

        private void buttonConsultaAtividade_Click(object sender, RoutedEventArgs e)
        {
            ConsultarAtividadeConcluida cac = new ConsultarAtividadeConcluida(this.backoffice);
            cac.Visibility = Visibility.Visible;
        }

        private void buttonRegistaBatedir_Click(object sender, RoutedEventArgs e)
        {
            RegistaBatedor rb = new RegistaBatedor(this.backoffice);
            rb.Visibility = Visibility.Visible;
        }

        private void buttonConsultaBatedor_Click(object sender, RoutedEventArgs e)
        {
            ConsultaBatedor cb = new ConsultaBatedor(this.backoffice);
            cb.Visibility = Visibility.Visible;
        }

        private void buttonLogout_Click(object sender, RoutedEventArgs e)
        {
            // Login l = new Login(this.backoffice);
            this.anterior.Visibility = Visibility.Visible;
        }
    }
}
