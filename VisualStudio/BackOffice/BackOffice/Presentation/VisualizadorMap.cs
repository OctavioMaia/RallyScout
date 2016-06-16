using BackOffice.Business;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Device.Location;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Media;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BackOffice.Presentation
{
    public partial class VisualizadorMap : Form
    {
        public Atividade ativ;
        Nota selecionada;
        List<Image> imagens;
        int indice = 0;
        GeoCoordinate anterior;

        public VisualizadorMap()
        {
            InitializeComponent();
            this.button1.Enabled = false;
            this.button2.Enabled = false;
            this.button3.Enabled = false;
        }

        public void updateComboBox(Atividade a)
        {
            this.ativ = a;
            foreach(Nota n in this.ativ.notas)
            {
                comboBox1.Items.Add(n.idNota);
            }
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedItem != null)
            {
                this.button1.Enabled = true;
                this.button2.Enabled = true;
                this.button3.Enabled = true;
                int id = int.Parse(comboBox1.SelectedItem.ToString());
                List<Nota> l = ativ.notas;

                foreach (Nota n in l)
                {
                    if (n.idNota == id)
                    {
                        this.selecionada = n;
                        this.richTextBox1.Text = this.selecionada.notaTextual;
                        this.richTextBox2.Text = this.selecionada.notasVoz.texto;
                        GeoCoordinate gc = this.selecionada.localRegisto;

                        if (this.anterior != null)
                            removeMarker();

                        this.anterior = gc;
                        addMarker(gc); //add marker ao mapa

                        this.imagens = this.selecionada.imagens;
                        if (this.imagens != null)
                            pictureBox1.Image = imagens[this.indice];
                        break;
                    }
                }
            }
            else
            {
                System.Windows.Forms.MessageBox.Show("Erro parse", "Warning!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
             
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (this.imagens != null &&  indice > 0) {
                indice--;
                pictureBox1.Image = imagens[this.indice];
            }
            else
            {
                System.Windows.Forms.MessageBox.Show("Não existem imagens anteriores a esta!", "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (this.imagens!=null &&  indice < this.imagens.Count)
            {
                indice++;
                pictureBox1.Image = imagens[this.indice];
            }
            else
            {
                System.Windows.Forms.MessageBox.Show("Não existem imagens posteriores a esta!", "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            byte[] sound = this.selecionada.voice;
            if (sound != null)
            {
                using (MemoryStream ms = new MemoryStream(sound))
                {
                    SoundPlayer player = new SoundPlayer(ms);
                    player.Play();
                }
            }
            else
            {
                System.Windows.Forms.MessageBox.Show("Ficheiro áudio nulo", "Warning!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }
    }
}
