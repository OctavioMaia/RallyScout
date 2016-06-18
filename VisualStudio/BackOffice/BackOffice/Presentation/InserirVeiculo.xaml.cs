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
    public partial class InserirVeiculo : Window
    {
        List<Veiculo> veiculos;
        BackOfficeAPP backoffice;
        Window anterior;
        bool cancelar;

        public InserirVeiculo(BackOfficeAPP b, List<Veiculo> l,Window w)
        {
            this.anterior = w;
            this.backoffice = b;
            this.veiculos = l;
            InitializeComponent();
            listaVeiculos.Items.Remove(listaVeiculos.Items.GetItemAt(0));
            this.cancelar = false;
        }
       
        private void buttonAdicionarVeiculo_Click(object sender, RoutedEventArgs e)
        {
            String chassi = textBoxChassi.Text;
            String marca = textBoxMarca.Text;
            String modelo = textBoxModelo.Text;
            String carateristicas = textBoxCarateristicas.Text;

            String[] split = carateristicas.Split(';');
            List<String> carateristicasSplit = new List<string>();
            for (int i = 0; i < split.Length; i++)
            {
                //MessageBox.Show(split[i]);
                carateristicasSplit.Add(split[i]);
            }

            Veiculo v = new Veiculo(modelo, marca, chassi, carateristicasSplit);
            veiculos.Add(v);

            if (!tudoPreenchido())
            {
                System.Windows.Forms.MessageBox.Show("Unico campo permitido vazio é caracteristicas.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }
            
            var lista = new String[] { chassi, marca, modelo };

            this.listaVeiculos.Items.Add(new MyItem { Chassi = chassi, Marca = marca, Modelo = modelo});
            this.clear();
        }

        private void buttonRemoverVeiculo_Click(object sender, RoutedEventArgs e)
        {
            if (listaVeiculos.SelectedItems.Count > 0)
            {
                var selectedItem = (dynamic)listaVeiculos.SelectedItems[0];
                string chassi = selectedItem.Chassi;
                for (int i=0; i < veiculos.Count; i++)
                {
                    if (veiculos[i].chassi.Equals(chassi))
                    {
                        veiculos.RemoveAt(i);
                    }
                }
                listaVeiculos.Items.Remove(listaVeiculos.SelectedItems[0]);
            }
        }

        private void buttonOK_Click(object sender, RoutedEventArgs e)
        {
            this.cancelar = true;
            this.Close();
            this.anterior.Visibility = Visibility.Visible;
        }
        private void clear()
        {
            textBoxChassi.Text="";
            textBoxMarca.Text="";
            textBoxModelo.Text="";
            textBoxCarateristicas.Text="";
        }
        private bool tudoPreenchido()
        {
            if(textBoxChassi.Text==null || textBoxChassi.Text.Equals(""))
            {
                return false;
            }
            if (textBoxMarca.Text == null || textBoxMarca.Text.Equals(""))
            {
                return false;
            }
            if (textBoxModelo.Text == null || textBoxModelo.Text.Equals(""))
            {
                return false;
            }

            return true;
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            if (!this.cancelar)
            {
                e.Cancel = true;
            }
        }
    }

    class MyItem
    {
        public string Chassi { get; set; }
        public string Marca { get; set; }
        public string Modelo { get; set; }
    }
}
