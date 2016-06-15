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
    /// Interaction logic for ConsultarAtividadeConcluida.xaml
    /// </summary>
    public partial class ConsultarAtividadeConcluida : Window
    {
        BackOfficeAPP backoffice;
        List<Atividade> atividades;
        Atividade selecionada;

        public ConsultarAtividadeConcluida(BackOfficeAPP b)
        {
            this.backoffice = b;
            InitializeComponent();
            UpdateComboBox();
            this.atividades = this.backoffice.getAtividadesTerminadas();
        }

        private void UpdateComboBox()
        {
            foreach (Atividade a in atividades)
            {
                int id = a.idAtividade;
                String nome = a.percurso.nomeProva;
                comboBox.Items.Add("ID: " +id+" Nome Prova: "+nome);
            }

        }

        private void comboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)  
        {
            List<Veiculo> veiculos;
            listViewVeiculos.Items.Clear();
            string Atividade = (comboBox.SelectedItem as string);
            string split = Atividade.Split(new string[] { " " }, StringSplitOptions.None)[0];

            MessageBox.Show(split);

            foreach (Atividade a in atividades)
            {
                int id = a.idAtividade;

                if (split.Equals(id))
                {
                    this.selecionada = a; 
                    veiculos = a.veiculos;
                    this.textBoxNomeEquipa.Text = a.nomeEquipa;
                    this.textBoxNomeBatedor.Text = a.batedor.nome + " | " + a.batedor.email;
                    this.textBoxDataInicio.Text = a.inicioReconhecimento.ToString();
                    this.textBoxDataFim.Text = a.fimReconhecimento.ToString();

                    foreach (Veiculo veic in veiculos)
                    {
                        String chassi = veic.chassi;
                        String marca = veic.marca;
                        String modelo = veic.modelo;
                        
                        var lista = new String[] { chassi, marca, modelo };
                        this.listViewVeiculos.Items.Add(new MyItem { Chassi = chassi, Marca = marca, Modelo = modelo });
                    }
                    break;
                }
            }


        }

        private void listView_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            List<Veiculo> veiculos = this.selecionada.veiculos;

            listViewCarateristicas.Items.Clear();

            if (listViewVeiculos.SelectedItems.Count > 0)
            {
                var selectedItem = (dynamic)listViewVeiculos.SelectedItems[0];
                string chassi = selectedItem.Chassi;
                foreach (Veiculo v in veiculos)
                {
                    if (v.chassi.Equals(chassi))
                    {
                        List<String> carateristicas = v.caracteristicas;
                        foreach(String s in carateristicas)
                        {
                            var lista = new String[] { s };
                            this.listViewCarateristicas.Items.Add(new MyItem2 { Carateristica = s });
                        }
                    }
                }
                
            }
        }

        private void buttonVerMapa_Click(object sender, RoutedEventArgs e)
        {
            VisualizadorMap vm = new VisualizadorMap();
            vm.carregaMapa(this.selecionada);
            vm.Visible = true;
        }
    }

    class MyItem2
    {
        public string Carateristica { get; set; }
    }

}
