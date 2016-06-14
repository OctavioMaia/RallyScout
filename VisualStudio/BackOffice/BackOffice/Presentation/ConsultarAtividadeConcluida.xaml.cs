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

        public ConsultarAtividadeConcluida(BackOfficeAPP b)
        {
            this.backoffice = b;
            InitializeComponent();
            UpdateComboBox();
        }

        private void UpdateComboBox()
        {
            List<Atividade> l = this.backoffice.getAtividadesTerminadas();//.getVeiculos();

            foreach (Atividade v in l)
            {
                comboBox.Items.Add(v);
            }

        }
    }
}
