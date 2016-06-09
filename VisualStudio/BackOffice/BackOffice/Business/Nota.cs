using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;
using System.Drawing;

namespace BackOffice.Business
{
    class Nota
    {
        public int idNota { get; set; }
        private string notaTextual { get; set; }
        private GeoCoordinate localRegisto { get; set; }
        private List<Image> imagens { get; set; }
        private byte[] voice { get; set; }
        private Voz notasVoz { get; set; }

        public Nota(int id, string texto, double lat, double longt, List<Image> imgs, byte[] voz)
        {
            this.idNota = id;
            this.notaTextual = texto;
            this.localRegisto = new GeoCoordinate(lat, longt);
            this.imagens = imgs;
            this.voice = voz;
            this.notasVoz = new Voz(voz);
            if (this.voice != null)
            {
                this.notasVoz.convertAudio();
            }

        }
        public bool asVoice()
        {
            return this.voice != null;
        }

        public string getToPiloto()
        {
            return this.notasVoz.texto;
        }
    }

    
}
