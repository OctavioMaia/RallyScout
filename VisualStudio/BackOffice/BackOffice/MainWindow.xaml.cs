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
using System.Windows.Navigation;
using System.Windows.Shapes;
using BackOffice.Business;

namespace BackOffice
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {

            BackOfficeAPP b = new BackOfficeAPP("C:\\Users\\Joao\\Documents\\GitHub\\RallyScout\\ExemplosJson\\config.json");
            MessageBox.Show(b.email);
            MessageBox.Show(b.passMail);

            Dictionary<string, string> d = b.simbolos;

            foreach (string chave in d.Keys)
            {
                MessageBox.Show(chave + " --> " + d[chave]);
            }
            //MessageBox.Show();
            

            InitializeComponent();
        }
    }
}
