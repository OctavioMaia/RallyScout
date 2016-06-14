﻿using System;
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
using BackOffice.Presentation;

namespace BackOffice
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            //Mapa m = new Mapa("teste", 1, "C:\\Users\\Joao\\Desktop\\map.gpx");

            BackOfficeAPP b = new BackOfficeAPP("C:\\Users\\Joao\\Documents\\GitHub\\RallyScout\\ExemplosJson\\config.json",1);
          //  MessageBox.Show(b.email);
           // MessageBox.Show(b.passMail);
            /*
            Dictionary<string, string> d = BackOfficeAPP.simbolos;

            
            Atividade a = new Atividade(1, "jms", "Teste", "C:\\Users\\Joao\\Desktop\\map.gpx", new List<Veiculo>(), new Equipa("jmsEquipa", "jms@a"), new Batedor("jms","jms","123"));
            b.atividadeFE.Add(a.idAtividade, a);

            /*
            MessageBox.Show("Vou gerar Json");
            b.enviarAtividade(a.idAtividade);
            MessageBox.Show("gerado Json");
            */
            MessageBox.Show("ler Json");
            string json = System.IO.File.ReadAllText("C:\\Users\\Joao\\Desktop\\test.json");
            b.formJson(json);
            MessageBox.Show("li Json");
            for (int i=0; i < 100; i++)
            {
                Voz v = new Voz(null);
                v.texto = "Texto Voz " + i;
                Nota n = new Nota(i, "Nota " + i, 0, 0, new List<System.Drawing.Image>(), null);
                n.notasVoz = v;
                a.addNota(n);
            }

            //gerar pdf
            //a.generateReportCopiloto("C:\\Users\\Joao\\Desktop\\Teste.pdf");
           

            /*teste enviar mail
             * List<String> at = new List<string>();
            at.Add("C:\\Users\\Joao\\Desktop\\Teste.pdf");
            at.Add("C:\\Users\\Joao\\Desktop\\map.gpx");

            MessageBox.Show("Vou mandar");
            b.email_send(b.email, "Subject test", "body Teste", at);

            MessageBox.Show("Mandei");*/

            /*foreach (string chave in d.Keys)
            {
                MessageBox.Show(chave + " --> " + d[chave]);
            }*/
            //MessageBox.Show();
            this.Visibility = Visibility.Hidden;
            InserirVeiculo l = new InserirVeiculo();
            l.Visibility = Visibility.Visible;


            // InitializeComponent();
        }
    }
}
